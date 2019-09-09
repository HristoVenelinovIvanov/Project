pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        bat 'mvn package'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git status'
        bat 'git status'
        bat 'git add .'
        bat 'git commit -m "Push from Jenkinsfile"'
        bat 'git push'
        bat 'git status'
      }
    }
  }
}