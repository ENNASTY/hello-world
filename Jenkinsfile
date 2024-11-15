    pipeline {
        agent any

        tools {
            maven 'Maven' 
            jdk 'JDK'  
        }

        environment {
            MAVEN_OPTS = '-Dmaven.test.failure.ignore=true'
            DOCKER_REGISTRY = 'ennasty'
            DOCKER_IMAGE_NAME = 'product'
            DOCKER_IMAGE_TAG = 'latest'
        }

        stages {
            stage('Clone') {
                steps {
                    git branch: 'main', url: 'https://github.com/ENNASTY/hello-world.git'
                }
            }

            stage('Build') {
                steps {
                    // Build the project
                    sh 'mvn clean package'
                }
            }

            stage('Test') {
                steps {
                    // Run tests
                    sh 'mvn test'
                }
            }

            stage('Build Docker Image') {
                steps {
                    script {
                        // Build the Docker image
                        sh """
                        docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .
                        """
                    }
                }
            }

            stage('Push Docker Image to Docker Hub') {
                steps {
                    script {
                        // Log in to Docker Hub (ensure Jenkins has credentials set up for Docker Hub)
                        withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                            sh """
                            docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}
                            docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                            """
                        }
                    }
                }
            }

            stage('Deploy') {
                steps {
                    // Optional: Deploy the app using Docker (assuming Docker is used for deployment)
                    echo 'Deploying the application...'
                    sh """
                    docker run -d -p 8080:8080 ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                    """
                }
            }
        }

        post {
            always {
                echo 'Pipeline finished.'
            }
            success {
                echo 'Pipeline completed successfully!'
            }
            failure {
                echo 'Pipeline failed.'
            }
        }
    }