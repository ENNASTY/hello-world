pipeline {
    agent any

    tools {
        // Specify JDK version here
        jdk 'JDK 17'
    }

    environment {
        // Set any environment variables, e.g., MAVEN_HOME
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=true'
    }

    stages {
        stage('Clone') {
            steps {
                // Cloning the repository
                git 'https://github.com/hamza-bousalih/hello-world.git'
            }
        }

        stage('Build') {
            steps {
                // Use Maven to clean and package the project
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                // Run tests with Maven
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                // This could be where you deploy the app
                echo 'Deploying the application...'
                // Example: running the app locally
                sh 'java -jar target/*.jar &'
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
