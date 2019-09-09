pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        bat 'mvn package'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git add .'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git config core.autocrlf true'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git status'
        bat 'git commit -m "Push from Jenkinsfile"'
        bat 'git push'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git status'
      }
    }
  }
}