9080/auth/realms/jhipster/protocol/openid-connect/auth?response_type=code&client_id=jhipster-registry&scope=openid address email jhipster microprofile-jwt offline_access phone profile roles web-origins&state=TXq1PI-EHpr9eqoeppu4s0NvMm6FS5eZRGd89hPnz4Y%3D&redirect_uri=http://129.69.217.173:8761/login/oauth2/code/oidc&nonce=S1U33LTpr0ccUuEikhkOzPGr_2cFmoeglPIq3vkr0tcdef agentLabel
if (BRANCH_NAME == 'main') {
    agentLabel = 'master'
} else {
    agentLabel = ''
}

pipeline {
    agent { label agentLabel }

    stages {
        stage('check java') {
            steps {
                sh 'java -version'
            }
        }

        stage('clean') {
            steps { sh 'chmod +x gradlew'
                sh './gradlew clean --no-daemon'
            }
        }

        stage('nohttp') {
            steps {
                sh './gradlew checkstyleNohttp --no-daemon'
            }
        }

        stage('backend tests') {
            steps {
                sh './gradlew check jacocoTestReport -PnodeInstall --no-daemon'
                junit '**/build/**/TEST-*.xml'
            }
        }

        stage('packaging') {
            steps {
                sh './gradlew bootJar -x test -Pprod -PnodeInstall --no-daemon'
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
            }
        }

        stage('quality analysis') {
            when {
                branch 'dev'
            }
            environment {
                scannerHome = tool 'SonarQubeScanner'
            }
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh './gradlew sonarqube --no-daemon'
                }
                timeout(time: 10, unit: 'MINUTES') {
                    // Needs to be changed to true in the real project.
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('deploy') {
            when {
                branch 'dev'
            }
            steps {
                echo 'Deploying....'
                dir('docker') {
                    sh 'docker-compose down'
                    sh 'docker-compose up -d --build'
                }
            }
        }

        stage('release') {
            when { allOf { branch 'dev'; triggeredBy 'UserIdCause' } }
            steps {
                sshagent (credentials: ['jenkins']) {
                    echo 'Pushing dev to main'
                    sh 'git push git@github.com:IT-REX-Platform/MediaService.git dev:main'
                }
            }
        }
    }
}
