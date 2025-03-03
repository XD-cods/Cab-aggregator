pipeline {
    agent any

    tools {
        maven '3.9.9'
    }

    environment {
        EMAIL_RECIPIENTS = 'vkuzir7@gmail.com'
        EMAIL_SUBJECT = 'Результат сборки проекта'
    }

    stages {
        stage('Checkstyle') {
            steps {
                script {
                    echo 'Запуск Checkstyle...'
                    sh 'mvn checkstyle:check'
                }
            }
        }

        stage('Tests') {
            steps {
                script {
                    echo 'Запуск unit-тестов...'
                    sh 'mvn test'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Сборка проекта...'
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
    }

    post {
        success {
            echo 'Сборка успешно завершена!'
            emailext (
                subject: "${EMAIL_SUBJECT} - УСПЕХ",
                body: 'Сборка и деплой прошли успешно. Детали: ${BUILD_URL}',
                to: "${EMAIL_RECIPIENTS}"
            )
        }
        failure {
            echo 'Сборка завершена с ошибками!'
            emailext (
                subject: "${EMAIL_SUBJECT} - ОШИБКА",
                body: 'Сборка завершена с ошибками. Детали: ${BUILD_URL}',
                to: "${EMAIL_RECIPIENTS}"
            )
        }
        unstable {
            echo 'Сборка нестабильна (например, тесты провалились).'
            emailext (
                subject: "${EMAIL_SUBJECT} - НЕСТАБИЛЬНО",
                body: 'Сборка нестабильна. Детали: ${BUILD_URL}',
                to: "${EMAIL_RECIPIENTS}"
            )
        }
    }
}
