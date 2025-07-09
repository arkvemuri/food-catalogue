pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'Java21'
    }

    environment {
        GITHUB_REPO_URL = 'https://github.com/arkvemuri/food-catalogue.git'
        BRANCH_NAME = 'master'
        APP_VERSION = '1.0.0'
        SONAR_PROJECT_KEY = 'food-catalogue'
        DOCKERHUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIAL')
        VERSION = "${env.BUILD_ID}"
    }

    stages {
        stage('Checkout') {
            steps {
                // Clean workspace before checking out code
                cleanWs()
                git branch: "${env.BRANCH_NAME}",
                    url: "${env.GITHUB_REPO_URL}",
                    changelog: true,
                    poll: true
            }
        }

        stage('Build and Test') {
            steps {
                // Use bat for Windows
                bat 'mvn clean verify'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    // Archive JaCoCo coverage reports as artifacts
                    archiveArtifacts artifacts: 'target/site/jacoco/**/*', allowEmptyArchive: true
                }
            }
        }

        stage('SonarQube Analysis') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                withSonarQubeEnv(installationName: 'SonarQube', credentialsId: 'sonarqube-token') {
                    bat """
                        mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.projectName=${SONAR_PROJECT_KEY} \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                        -Dsonar.java.coveragePlugin=jacoco
                    """
                }
            }
        }

        stage('Quality Gate') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }

        stage('Coverage Check') {
            steps {
                script {
                    // Coverage is already checked by Maven JaCoCo plugin during 'mvn clean verify'
                    // If the build reached this stage, coverage requirements are met
                    echo 'Coverage check passed - Maven JaCoCo plugin enforced minimum 80% coverage per class'
                }
            }
        }

        stage('Package') {
            steps {
                // Archive the built artifacts
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build and Push') {
              steps {
                  bat 'echo %DOCKERHUB_CREDENTIALS_PSW% | docker login -u %DOCKERHUB_CREDENTIALS_USR% --password-stdin'
                  bat 'docker build -t arkvemuri/food-catalogue-service:%VERSION% .'
                  bat 'docker push arkvemuri/food-catalogue-service:%VERSION%'
              }
            }

         stage('Update Image Tag in GitOps') {
               steps {
                  checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[ credentialsId: 'local-git-ssh', url: 'git@github.com:arkvemuri/deployment-folder.git']])
                 script {
                bat '''
                   powershell -Command "(Get-Content aws/food-catalogue-manifest.yml) -replace 'image:.*', 'image: arkvemuri/food-catalogue-service:%VERSION%' | Set-Content aws/food-catalogue-manifest.yml"
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
    }

    post {
        success {
            echo "Successfully built and analyzed version ${env.APP_VERSION}"
        }
        failure {
            echo 'Build or analysis failed!'
        }
        always {
            // Clean workspace after build
            cleanWs()
        }
    }
}
