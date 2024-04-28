pipeline{
    agent any
    stages{
        stage("Run Tests with Docker Compose") {
            steps {
                script {
                    sh "docker compose -f docker-compose.test.yml up --build --abort-on-container-exit"

                    // Check the exit status of the test containers
                    def result = sh(script: "docker inspect -f \"\{\{.State.ExitCode\}\}\" \$(docker compose -f docker-compose.test.yml ps -q)", returnStdout: true).trim()
                    if (result != "0") {
                        error("Unit-Tests failed")
                    }
                }
            }
        }
        stage("Build and Push Docker Image"){
            steps{
                // checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'git@github.com:EstebanGlochonUSACH/mingeso-monolithic.git', credentialsId: 'git-ssh-credentials']])
                dir("frontend"){
                    script{
                        withDockerRegistry(credentialsId: 'docker-credentials'){
                            sh "docker build -t estebanglochon/autofix-monolothic-frontend ."
                            sh "docker push estebanglochon/autofix-monolothic-frontend"
                        }
                    }                    
                }
                dir("backend"){
                    script{
                        withDockerRegistry(credentialsId: 'docker-credentials'){
                            sh "docker build -t estebanglochon/autofix-monolothic-backend ."
                            sh "docker push estebanglochon/autofix-monolothic-backend"
                        }
                    }                    
                }
            }
        }
        stage("Deploy with Docker Compose") {
            steps {
                dir("deployment"){
                    script {
                        sh "docker compose pull"
                        sh "docker compose up -d"
                    }
                }
            }
        }
    }
}