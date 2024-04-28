pipeline{
    agent any
    stages{
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