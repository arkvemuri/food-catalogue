# Jenkins CI/CD Pipeline Setup Guide

This guide explains how to set up the Jenkins pipeline for the Food Catalogue Microservice.

## Prerequisites

### 1. Jenkins Installation
- Jenkins server with Docker support
- Required plugins installed (see Plugin Requirements below)

### 2. SonarQube Setup
- Local SonarQube server running
- SonarQube configured in Jenkins

### 3. Docker Hub Account
- Docker Hub account for image repository
- Docker Hub credentials configured in Jenkins

## Plugin Requirements

Install the following Jenkins plugins:

```
- Pipeline
- Git
- Maven Integration
- SonarQube Scanner
- Docker Pipeline
- Email Extension
- JUnit
- Jacoco
```

## Jenkins Configuration

### 1. Global Tool Configuration

Navigate to `Manage Jenkins` → `Global Tool Configuration`:

#### Maven Configuration
- **Name**: `Maven`
- **Version**: Maven 3.9.x or later
- **Install automatically**: ✅

#### JDK Configuration
- **Name**: `JDK-21`
- **Version**: OpenJDK 21
- **Install automatically**: ✅

#### SonarQube Scanner Configuration
- **Name**: `SonarQubeScanner`
- **Version**: Latest
- **Install automatically**: ✅

### 2. System Configuration

Navigate to `Manage Jenkins` → `Configure System`:

#### SonarQube Servers
- **Name**: `SonarQube`
- **Server URL**: `http://localhost:9000` (or your SonarQube server URL)
- **Server authentication token**: Add your SonarQube token

#### Email Configuration (Optional)
Configure SMTP settings for build notifications.

### 3. Credentials Setup

Navigate to `Manage Jenkins` → `Manage Credentials`:

#### Docker Hub Credentials
- **Kind**: Username with password
- **ID**: `dockerhub-credentials`
- **Username**: Your Docker Hub username
- **Password**: Your Docker Hub password or access token

## Pipeline Setup

### 1. Create New Pipeline Job

1. Click `New Item`
2. Enter job name: `food-catalogue-pipeline`
3. Select `Pipeline`
4. Click `OK`

### 2. Configure Pipeline

#### General Settings
- **Description**: Food Catalogue Microservice CI/CD Pipeline
- **GitHub project**: `https://github.com/arkvemuri/food-catalogue`

#### Build Triggers (Optional)
- ✅ **GitHub hook trigger for GITScm polling**
- ✅ **Poll SCM**: `H/5 * * * *` (every 5 minutes)

#### Pipeline Configuration
- **Definition**: Pipeline script from SCM
- **SCM**: Git
- **Repository URL**: `https://github.com/arkvemuri/food-catalogue.git`
- **Branch**: `*/master`
- **Script Path**: `Jenkinsfile`

### 3. Save and Build

1. Click `Save`
2. Click `Build Now` to test the pipeline

## Pipeline Stages

The Jenkinsfile includes the following stages:

1. **Checkout** - Get code from GitHub
2. **Build** - Compile the application
3. **Test** - Run unit tests
4. **Package** - Create JAR file
5. **SonarQube Analysis** - Code quality analysis
6. **Quality Gate** - Wait for SonarQube quality gate
7. **Build Docker Image** - Create Docker image
8. **Push to Docker Hub** - Push image to registry
9. **Clean Up** - Remove local Docker images

## Environment Variables

The pipeline uses these environment variables:

```groovy
DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
DOCKER_IMAGE = 'arkvemuri/food-catalogue'
DOCKER_TAG = "${BUILD_NUMBER}"
SONAR_PROJECT_KEY = 'food-catalogue'
SONAR_PROJECT_NAME = 'Food Catalogue Microservice'
```

## SonarQube Quality Gates

The pipeline will fail if the SonarQube quality gate fails. Default quality gate checks:

- **Coverage**: > 80%
- **Duplicated Lines**: < 3%
- **Maintainability Rating**: A
- **Reliability Rating**: A
- **Security Rating**: A

## Docker Image

The pipeline builds and pushes Docker images with:

- **Repository**: `arkvemuri/food-catalogue`
- **Tags**: 
  - `latest` (always updated)
  - `{BUILD_NUMBER}` (specific build version)

## Notifications

The pipeline sends email notifications on:

- ✅ **Success**: Build completed successfully
- ❌ **Failure**: Build failed
- ⚠️ **Unstable**: Tests failed but build succeeded

## Troubleshooting

### Common Issues

1. **Maven not found**
   - Ensure Maven is configured in Global Tool Configuration
   - Check PATH environment variable

2. **SonarQube connection failed**
   - Verify SonarQube server is running
   - Check authentication token
   - Verify network connectivity

3. **Docker push failed**
   - Verify Docker Hub credentials
   - Check Docker daemon is running
   - Ensure proper permissions

4. **Quality Gate timeout**
   - Increase timeout in Jenkinsfile
   - Check SonarQube server performance
   - Review project complexity

### Logs and Debugging

- Check Jenkins console output for detailed logs
- Review SonarQube project dashboard
- Verify Docker Hub repository for pushed images

## Security Considerations

1. **Credentials Management**
   - Use Jenkins credentials store
   - Never hardcode passwords in Jenkinsfile
   - Rotate credentials regularly

2. **Docker Security**
   - Use official base images
   - Scan images for vulnerabilities
   - Keep images updated

3. **SonarQube Security**
   - Configure proper authentication
   - Use HTTPS for production
   - Regular security updates

## Customization

### Modify Docker Image Name
Update the `DOCKER_IMAGE` environment variable in Jenkinsfile:

```groovy
DOCKER_IMAGE = 'your-dockerhub-username/your-image-name'
```

### Change SonarQube Project Settings
Update SonarQube environment variables:

```groovy
SONAR_PROJECT_KEY = 'your-project-key'
SONAR_PROJECT_NAME = 'Your Project Name'
```

### Add Additional Stages
Add new stages before the `post` section:

```groovy
stage('Deploy to Staging') {
    steps {
        // Your deployment steps
    }
}
```

## Support

For issues and questions:
1. Check Jenkins logs
2. Review SonarQube documentation
3. Consult Docker Hub documentation
4. Check GitHub repository issues