pipeline {
    agent any
    
    environment {
        // Docker Hub credentials (configure in Jenkins credentials)
        DOCKERHUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIAL')
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
        jdk 'Java21'
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
        
        stage('Docker Build and Push') {
              steps {
                  bat 'echo %DOCKERHUB_CREDENTIALS_PSW% | docker login -u %DOCKERHUB_CREDENTIALS_USR% --password-stdin'
                  bat 'docker build -t arkvemuri/restaurant-listing-service:%VERSION% .'
                  bat 'docker push arkvemuri/restaurant-listing-service:%VERSION%'
              }
         }
         stage('Update Image Tag in GitOps') {
               steps {
                  checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[ credentialsId: 'local-git-ssh', url: 'git@github.com:arkvemuri/deployment-folder.git']])
                  script {
                      bat '''
                       powershell -Command "(Get-Content aws/restaurant-manifest.yml) -replace 'image:.*', 'image: arkvemuri/restaurant-listing-service:%VERSION%' | Set-Content aws/restaurant-manifest.yml"
                     '''
                       bat 'git checkout master'
                       bat 'git add .'
                       bat 'git commit -m "Update image tag"'
                       sshagent(['local-git-ssh'])
                       {
                               bat('git push')
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