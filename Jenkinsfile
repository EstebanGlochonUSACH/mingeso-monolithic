pipeline{
    agent any
    stages{
        stage("Build and Push Docker Image"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/EstebanGlochonUSACH/mingeso-monolithic', credentialsId: 'git-ssh-credentials']])
                dir("frontend"){
                    script{
                        withDockerRegistry(credentialsId: 'docker-credentials'){
                            sh "docker build -t estebanglochon/autofix-monolothic-frontend ."
                            sh "docker push estebanglochon/autofix-monolothic-frontend"
                        }
                    }                    
                }
            }
        }
    }
}