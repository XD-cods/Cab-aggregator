pipeline {
    agent any

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
            echo 'Сборка успешно завершена!'
            script {
                def recipients = getGitCommitAuthors()
                emailext (
                    subject: "${EMAIL_SUBJECT} - УСПЕХ",
                    body: 'Сборка и деплой прошли успешно. Детали: ${BUILD_URL}',
                    to: recipients
                )
            }
        }
        failure {
            echo 'Сборка завершена с ошибками!'
            script {
                def recipients = getGitCommitAuthors()
                emailext (
                    subject: "${EMAIL_SUBJECT} - ОШИБКА",
                    body: 'Сборка завершена с ошибками. Детали: ${BUILD_URL}',
                    to: recipients
                )
            }
        }
        unstable {
            echo 'Сборка нестабильна (например, тесты провалились).'
            script {
                def recipients = getGitCommitAuthors()
                emailext (
                    subject: "${EMAIL_SUBJECT} - НЕСТАБИЛЬНО",
                    body: 'Сборка нестабильна. Детали: ${BUILD_URL}',
                    to: recipients
                )
            }
        }
    }
}

def getGitCommitAuthors() {
    def commitAuthors = sh(script: 'git log --pretty="%ae"', returnStdout: true).trim().split("\n").unique()

    return commitAuthors.join(',')
}