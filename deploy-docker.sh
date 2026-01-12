#!/bin/bash

# 색상 정의
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}🐳 Docker 기반 무중단 배포 시작${NC}"

# 컨테이너 이름
CONTAINER_NAME="bamboo-server"
IMAGE_NAME="jinseok19/bamboo-server:latest"

# 현재 실행 중인 컨테이너 확인
RUNNING_CONTAINER=$(docker ps -q --filter "name=${CONTAINER_NAME}")

if [ -n "$RUNNING_CONTAINER" ]; then
    echo -e "${BLUE}🔹 현재 실행 중인 컨테이너: ${RUNNING_CONTAINER}${NC}"
    
    # 블루-그린 배포를 위한 새 컨테이너 이름
    NEW_CONTAINER="${CONTAINER_NAME}-new"
    OLD_CONTAINER="${CONTAINER_NAME}"
else
    echo -e "${BLUE}🔹 실행 중인 컨테이너 없음. 새로 시작합니다.${NC}"
    NEW_CONTAINER="${CONTAINER_NAME}"
fi

# 최신 이미지 Pull
echo -e "${BLUE}🔹 최신 Docker 이미지 다운로드 중...${NC}"
docker pull ${IMAGE_NAME}

# 환경 변수 파일 확인
if [ ! -f "/home/ubuntu/.env" ]; then
    echo -e "${RED}❌ .env 파일이 없습니다!${NC}"
    exit 1
fi

# 새 컨테이너 시작
echo -e "${BLUE}🔹 새 컨테이너 시작 중...${NC}"
docker run -d \
  --name ${NEW_CONTAINER} \
  --env-file /home/ubuntu/.env \
  -p 8080:8080 \
  --restart unless-stopped \
  ${IMAGE_NAME}

# 컨테이너 시작 대기
echo -e "${BLUE}⏳ 컨테이너 시작 대기 중...${NC}"
sleep 10

# Health check
for i in {1..10}; do
    echo -e "${BLUE}⏳ Health check 시도 ($i/10)${NC}"
    
    if curl -f http://localhost:8080/health > /dev/null 2>&1; then
        echo -e "${GREEN}✅ 새 컨테이너 정상 작동!${NC}"
        
        # 기존 컨테이너 종료
        if [ -n "$RUNNING_CONTAINER" ] && [ "$NEW_CONTAINER" != "$CONTAINER_NAME" ]; then
            echo -e "${BLUE}🛑 기존 컨테이너 종료 중...${NC}"
            docker stop ${OLD_CONTAINER}
            docker rm ${OLD_CONTAINER}
            
            # 새 컨테이너 이름 변경
            docker rename ${NEW_CONTAINER} ${CONTAINER_NAME}
        fi
        
        echo -e "${GREEN}🎉 배포 성공!${NC}"
        docker ps --filter "name=${CONTAINER_NAME}"
        exit 0
    fi
    
    sleep 3
done

# Health check 실패
echo -e "${RED}❌ Health check 실패! 롤백합니다.${NC}"
docker stop ${NEW_CONTAINER}
docker rm ${NEW_CONTAINER}

echo -e "${RED}❌ 배포 실패! 기존 컨테이너 유지.${NC}"
exit 1
