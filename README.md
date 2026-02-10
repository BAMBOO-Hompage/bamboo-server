# 🎋 BAMBOO | 상명대 AI 동아리 관리 홈페이지
> **BAMBOO**는 **상명대학교 휴먼지능정보공학과**의 AI 학술 동아리입니다.
> 
> 
> BAMBOO는 동아리 소개뿐만 아니라, 원활한 동아리 운영 및 스터디 관리를 위해 자체 개발된 서비스입니다.
> 
> AI에 관심 있는 학생들이 함께 학습하고 연구할 수 있도록 체계적인 관리 환경을 제공합니다.

</br>

## 🔥 Team

### 🌃 BE Developer

| 김재관 | 김희재 |
| --- | --- |
| <p align="center"><img src="https://avatars.githubusercontent.com/KJaeKwan" width="100" height="100" style="border-radius: 5%;"></p><p align="center"><a href="https://github.com/KJaeKwan"><img src="https://img.shields.io/badge/KJaeKwan-181717?style=for-the-social&logo=github&logoColor=white"/></a></p> | <p align="center"><img src="https://github.com/user-attachments/assets/49a9d6d0-2896-4696-8025-bf10e31f8e22" width="100" height="100" style="border-radius: 5%;"></p><p align="center"><a href="https://github.com/1026hz"><img src="https://img.shields.io/badge/1026hz-181717?style=for-the-social&logo=github&logoColor=white"/></a></p> |


</br>

## 🏦 Architecture

```mermaid
graph TB
    subgraph "Client"
        Browser[🌐 Browser]
    end
    
    subgraph "Cloudflare CDN"
        CF[☁️ Cloudflare<br/>DNS + SSL + CDN]
    end
    
    subgraph "Frontend - S3"
        S3[📦 S3 Static Hosting<br/>smu-bamboo.uk]
    end
    
    subgraph "Backend - EC2"
        Nginx[🔒 Nginx<br/>Reverse Proxy<br/>SSL/TLS]
        Docker[🐳 Docker Container<br/>Spring Boot:8080]
    end
    
    subgraph "Database"
        RDS[(🗄️ AWS RDS<br/>MySQL 8.0)]
        Redis[(🔴 Redis<br/>Cache)]
    end
    
    subgraph "Storage"
        S3Storage[📁 S3<br/>File Storage]
    end
    
    subgraph "CI/CD"
        GH[⚙️ GitHub Actions]
        DH[🐳 Docker Hub<br/>jinseok19/bamboo-server]
    end
    
    Browser -->|HTTPS| CF
    CF -->|HTTPS| S3
    CF -->|HTTPS| Nginx
    Nginx -->|HTTP| Docker
    Docker --> RDS
    Docker --> Redis
    Docker --> S3Storage
    
    GH -->|Build & Push| DH
    DH -->|Pull Image| Docker
    
    style CF fill:#f9a825
    style Docker fill:#0db7ed
    style S3 fill:#569A31
    style RDS fill:#527FFF
    style Redis fill:#DC382D
```

**도메인:** `https://smu-bamboo.uk` (Frontend) | `https://api.smu-bamboo.uk` (Backend)

</br>

## 🛑 Version

Java -  `21` 

Spring Boot -  `3.4.1`

Gradle - `8.12`

MySQL - `8.0`

Redis - `7`

</br>

## 💻 로컬 환경 세팅

### 📋 사전 준비

1. **Java 21** 설치 확인
   ```bash
   java -version
   # java version "21.x.x" 확인
   ```

2. **Docker Desktop** 설치
   - [Docker Desktop 다운로드](https://www.docker.com/products/docker-desktop/)
   - 설치 후 Docker가 실행 중인지 확인
   ```bash
   docker --version
   docker ps
   ```

### 🚀 실행 방법

#### 1단계: 환경 변수 설정
```bash
# env.template 파일을 .env로 복사
copy env.template .env

# .env 파일을 열어 필요한 값 수정 (대부분 기본값으로 사용 가능)
```

#### 2단계: Docker 컨테이너 시작
```bash
# MySQL, Redis, Grafana, Prometheus 실행
docker-compose up -d

# 컨테이너 상태 확인 (healthy 확인)
docker-compose ps
```

#### 3단계: 프로젝트 빌드 및 실행
```bash
# 빌드 (테스트 제외)
gradlew.bat clean build -x test

# 애플리케이션 실행
gradlew.bat bootRun
```

#### 4단계: 확인
- API 문서: http://localhost:8080/swagger-ui/index.html
- Health Check: http://localhost:8080/health
- Grafana: http://localhost:3000
- Prometheus: http://localhost:9090

### 🛠️ 유용한 명령어

```bash
# Docker 컨테이너 관리
docker-compose start    # 시작
docker-compose stop     # 중지
docker-compose restart  # 재시작
docker-compose down     # 중지 및 삭제
docker-compose logs -f  # 로그 확인

# MySQL 접속
docker exec -it bamboo-mysql mysql -u bamboo_user -p
# 비밀번호: bamboo_password

# Redis 접속
docker exec -it bamboo-redis redis-cli
```

### 🐛 문제 해결

**포트 충돌 시:**
```bash
netstat -ano | findstr :8080
netstat -ano | findstr :3306
netstat -ano | findstr :6379
```

**빌드 오류 시:**
```bash
gradlew.bat clean
rmdir /s /q .gradle
rmdir /s /q build
gradlew.bat clean build -x test
```

</br>

## 🚀 Tech Stack
#### 📌 Framework
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-social&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/QueryDSL-005571?style=for-the-social&logo=apachekafka&logoColor=white">

#### 📌 Build Tool
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-social&logo=Gradle&logoColor=white">

#### 📌 Database
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-social&logo=Databricks&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-social&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-social&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/Redis-%23DC382D.svg?style=for-the-social&logo=redis&logoColor=white" />

#### 📌 Security
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-social&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=for-the-social&logo=JSON Web Tokens&logoColor=white">

#### 📌 Testing
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-social&logo=junit5&logoColor=white">

#### 📌 Cloud & Infrastructure
<img src ="https://img.shields.io/badge/EC2-FF9900?style=for-the-social&logo=amazonec2&logoColor=white"> <img src ="https://img.shields.io/badge/S3-69A31?style=for-the-social&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/RDS-527FFF?style=for-the-social&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/Cloudflare-F38020?style=for-the-social&logo=cloudflare&logoColor=white">

#### 📌 Containerization
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-social&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/Docker Hub-2496ED?style=for-the-social&logo=docker&logoColor=white"> 

#### 📌 API Documentation
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-social&logo=swagger&logoColor=white">

#### 📌 CI/CD
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-social&logo=githubactions&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-social&logo=docker&logoColor=white">

#### 📌 Team Collaboration
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-social&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/Git-F05032?style=for-the-social&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-social&logo=notion&logoColor=white" /> <img src="https://img.shields.io/badge/Discord-%237289DA.svg?style=for-the-social&logo=discord&logoColor=white" /> <img src="https://img.shields.io/badge/Figma-%23F24E1E.svg?style=for-the-social&logo=figma&logoColor=white" />

</br>

## 🚀 배포 프로세스

### Docker 기반 자동 배포

```mermaid
sequenceDiagram
    participant Dev as 👨‍💻 Developer
    participant GH as GitHub
    participant GA as GitHub Actions
    participant DH as Docker Hub
    participant EC2 as EC2 Server
    participant User as 🌐 Users
    
    Dev->>GH: git push origin develop
    GH->>GA: Trigger Workflow
    GA->>GA: Build Docker Image
    GA->>DH: Push Image
    GA->>EC2: SSH & Deploy
    EC2->>DH: Pull Latest Image
    EC2->>EC2: Stop Old Container
    EC2->>EC2: Start New Container
    EC2->>EC2: Health Check
    EC2-->>User: Service Available
```

### 배포 단계

1. **코드 푸시**: `develop` 브랜치에 push
2. **자동 빌드**: GitHub Actions가 Docker 이미지 빌드
3. **이미지 푸시**: Docker Hub에 업로드
4. **EC2 배포**: 자동으로 최신 이미지 pull & 실행
5. **무중단 배포**: Health check 후 이전 버전 교체
6. **완료**: 약 5-8분 소요

### Docker Hub
- **Repository**: `jinseok19/bamboo-server`
- **Tags**: `latest` (최신), `<commit-sha>` (버전별)

### 도메인

| 타입 | 도메인 | 용도 |
|------|--------|------|
| Frontend | https://smu-bamboo.uk | 프론트엔드 웹사이트 |
| Frontend | https://www.smu-bamboo.uk | 프론트엔드 (www) |
| Backend | https://api.smu-bamboo.uk | REST API 서버 |
| Docs | https://api.smu-bamboo.uk/swagger-ui/ | API 문서 |

</br>

## 🗂 **ERD**  - [자세히 보기](https://www.erdcloud.com/d/MYP2hedjYyGPKtGsL)

![Image](https://github.com/user-attachments/assets/af19ed66-ebe6-47fa-bacd-e47119c11007)


#진석