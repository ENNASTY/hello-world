pipeline {
    agent any

     tools {
        maven 'Maven'
        jdk 'JDK' 
    }

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-19' // Chemin de Java 19 dans votre environnement Jenkins
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        SONARQUBE_SERVER = 'SonarQube' // Nom de la configuration SonarQube dans Jenkins
        DOCKER_IMAGE = 'ennasty/hello-world:latest'  // Nom complet de l'image Docker pour Render
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Cloner le code depuis GitHub
                git url: 'https://github.com/ENNASTY/hello-world.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                // Construire le projet avec Maven en utilisant Java 19
                sh 'mvn clean package -Djava.home=$JAVA_HOME'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Exécuter SonarQube pour l'analyse de la qualité du code
                script {
                    def scannerHome = tool 'SonarQube Scanner'
                    withSonarQubeEnv(SONARQUBE_SERVER) {
                        sh "${scannerHome}/bin/sonar-scanner"
                    }
                }
            }
        }

        stage('Run Unit Tests') {
            steps {
                // Exécuter les tests unitaires avec Maven en utilisant Java 19
                sh 'mvn test -Djava.home=$JAVA_HOME'
            }
            post {
                always {
                    // Publier les résultats des tests dans Jenkins
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                // Construire l'image Docker
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    // Connectez-vous à Docker Hub
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh """
                        docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}
                        docker push ${DOCKER_IMAGE}
                        """
                    }
                }
            }
        }

        stage('Deploy to Render') {
            steps {
                echo "The Docker image has been pushed to Docker Hub and is ready to be deployed on Render. Deployment on Render must be configured on the Render platform itself."
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check logs for details.'
        }
    }
}
