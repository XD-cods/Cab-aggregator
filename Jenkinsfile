pipeline {
    agent any

    tools {
        maven
    }

    environment {
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
            script {
                echo 'Сборка успешно завершена!'
                def commiters = sh(script: 'git log --pretty=format:"%ae"', returnStdout: true).trim().split("\n").unique()
                def emailRecipients = commiters.join(', ')

                emailext (
                    subject: "${EMAIL_SUBJECT} - УСПЕХ",
                    body: 'Сборка и деплой прошли успешно. Детали: ${BUILD_URL}',
                    to: emailRecipients
                )
            }
        }
        failure {
            script {
                echo 'Сборка завершена с ошибками!'
                def commiters = sh(script: 'git log --pretty=format:"%ae"', returnStdout: true).trim().split("\n").unique()
                def emailRecipients = commiters.join(', ')

                emailext (
                    subject: "${EMAIL_SUBJECT} - ОШИБКА",
                    body: 'Сборка завершена с ошибками. Детали: ${BUILD_URL}',
                    to: emailRecipients
                )
            }
        }
        unstable {
            script {
                echo 'Сборка нестабильна (например, тесты провалились).'
                def commiters = sh(script: 'git log --pretty=format:"%ae"', returnStdout: true).trim().split("\n").unique()
                def emailRecipients = commiters.join(', ')

                emailext (
                    subject: "${EMAIL_SUBJECT} - НЕСТАБИЛЬНО",
                    body: 'Сборка нестабильна. Детали: ${BUILD_URL}',
                    to: emailRecipients
                )
            }
        }
    }
}