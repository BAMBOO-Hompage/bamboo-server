#!/bin/bash

# 색상 정의
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}🐳 Docker Compose 기반 무중단 배포 시작${NC}"

cd /home/ubuntu

# 최신 이미지 Pull
echo -e "${BLUE}🔹 최신 Docker 이미지 다운로드 중...${NC}"
docker pull jinseok19/bamboo-server:latest

# 기존 Java 프로세스 종료
echo -e "${BLUE}🔹 기존 Java 프로세스 종료...${NC}"
pkill -f BambooServer || true

# Docker Compose로 재시작
echo -e "${BLUE}🚀 서비스 재시작 중...${NC}"
docker-compose -f docker-compose.prod.yml down
docker-compose -f docker-compose.prod.yml up -d

# 서비스 시작 대기
echo -e "${BLUE}⏳ 서비스 시작 대기 (45초)...${NC}"
sleep 45

# Health check
echo -e "${BLUE}🔍 Health check...${NC}"
for i in {1..15}; do
    if curl -f http://localhost:8080/health > /dev/null 2>&1; then
        echo -e "${GREEN}✅ 배포 성공!${NC}"
        docker-compose -f docker-compose.prod.yml ps
        docker-compose -f docker-compose.prod.yml logs --tail 30 bamboo
        exit 0
    fi
    echo -e "${BLUE}   재시도 ($i/15)...${NC}"
    sleep 5
done

# Health check 실패
echo -e "${RED}❌ Health check 실패!${NC}"
docker-compose -f docker-compose.prod.yml logs bamboo
exit 1
