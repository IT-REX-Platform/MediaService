def agentLabel
if (BRANCH_NAME == 'main') {
    agentLabel = 'master'
} else {
    agentLabel = ''
}

pipeline {
    agent { label agentLabel }

    stages {
        stage('clean') {
            steps { sh 'chmod +x gradlew'
                sh './gradlew clean --no-daemon'
            }
        }

        stage('tests') {
            steps {
                sh './gradlew check jacocoTestReport -PnodeInstall --no-daemon'
                junit '**/build/**/TEST-*.xml'
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
                    sh './gradlew jibDockerBuild'
                    sh 'docker-compose rm -svf mediaservice'
                    sh 'docker-compose up -d --build --remove-orphans'
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
