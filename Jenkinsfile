def agentLabel
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
                branch 'main'
            }
            steps {
                echo 'Deploying....'
                sh './gradlew jibBuildTar'
                sh 'mkdir -p /srv/Backend/mediaservice'
                sh 'rm -rf /srv/Backend/mediaservice/*'
                sh 'cd ./build && mv jib-image.tar /srv/Backend/mediaservice/mediaservice.tar'
                sh 'touch /srv/Backend/mediaservice/deploy'
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
