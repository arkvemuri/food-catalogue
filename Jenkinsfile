pipeline {
    agent any
    
    environment {
        // Docker Hub credentials (configure in Jenkins credentials)
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKER_IMAGE = 'arkvemuri/food-catalogue'
        DOCKER_TAG = "${BUILD_NUMBER}"
        
        // SonarQube environment
        SONAR_SCANNER_HOME = tool 'SonarQubeScanner'
        SONAR_PROJECT_KEY = 'food-catalogue'
        SONAR_PROJECT_NAME = 'Food Catalogue Microservice'
        
        // Maven environment
        MAVEN_HOME = tool 'Maven'
        PATH = "${MAVEN_HOME}/bin:${SONAR_SCANNER_HOME}/bin:${PATH}"
    }
    
    tools {
        maven 'Maven'
        jdk 'JDK-21'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from GitHub...'
                git branch: 'master',
                    url: 'https://github.com/arkvemuri/food-catalogue.git'
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the application...'
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    // Publish test results
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                    
                    // Archive test reports
                    archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging the application...'
                sh 'mvn package -DskipTests'
            }
            post {
                success {
                    // Archive the built artifacts
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.projectName='${SONAR_PROJECT_NAME}' \
                        -Dsonar.projectVersion=${BUILD_NUMBER} \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.java.test.binaries=target/test-classes \
                        -Dsonar.junit.reportPaths=target/surefire-reports \
                        -Dsonar.jacoco.reportPaths=target/site/jacoco/jacoco.exec \
                        -Dsonar.java.coveragePlugin=jacoco
                    """
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                script {
                    // Build the Docker image
                    def dockerImage = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                    
                    // Also tag as latest
                    dockerImage.tag("latest")
                }
            }
        }
        
        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-credentials') {
                        // Push both tagged and latest versions
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                        docker.image("${DOCKER_IMAGE}:latest").push()
                    }
                }
            }
        }
        
        stage('Clean Up') {
            steps {
                echo 'Cleaning up local Docker images...'
                sh """
                    docker rmi ${DOCKER_IMAGE}:${DOCKER_TAG} || true
                    docker rmi ${DOCKER_IMAGE}:latest || true
                    docker system prune -f
                """
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed.'
            
            // Clean workspace
            cleanWs()
        }
        
        success {
            echo 'Pipeline executed successfully!'
            
            // Send success notification (configure email/Slack as needed)
            emailext (
                subject: "✅ SUCCESS: Food Catalogue Pipeline #${BUILD_NUMBER}",
                body: """
                    <h2>Build Successful!</h2>
                    <p><strong>Project:</strong> Food Catalogue Microservice</p>
                    <p><strong>Build Number:</strong> ${BUILD_NUMBER}</p>
                    <p><strong>Docker Image:</strong> ${DOCKER_IMAGE}:${DOCKER_TAG}</p>
                    <p><strong>Build URL:</strong> ${BUILD_URL}</p>
                    
                    <h3>Stages Completed:</h3>
                    <ul>
                        <li>✅ Code Checkout</li>
                        <li>✅ Build & Test</li>
                        <li>✅ SonarQube Analysis</li>
                        <li>✅ Quality Gate Passed</li>
                        <li>✅ Docker Image Built</li>
                        <li>✅ Pushed to Docker Hub</li>
                    </ul>
                """,
                to: "${env.CHANGE_AUTHOR_EMAIL}",
                mimeType: 'text/html'
            )
        }
        
        failure {
            echo 'Pipeline failed!'
            
            // Send failure notification
            emailext (
                subject: "❌ FAILED: Food Catalogue Pipeline #${BUILD_NUMBER}",
                body: """
                    <h2>Build Failed!</h2>
                    <p><strong>Project:</strong> Food Catalogue Microservice</p>
                    <p><strong>Build Number:</strong> ${BUILD_NUMBER}</p>
                    <p><strong>Build URL:</strong> ${BUILD_URL}</p>
                    <p><strong>Console Output:</strong> ${BUILD_URL}console</p>
                    
                    <p>Please check the build logs for more details.</p>
                """,
                to: "${env.CHANGE_AUTHOR_EMAIL}",
                mimeType: 'text/html'
            )
        }
        
        unstable {
            echo 'Pipeline is unstable!'
        }
    }
}