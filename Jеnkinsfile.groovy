pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        bat 'mvn package'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git status'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git add .'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git commit -m "Push from Jenkinsfile"'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git push'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git status'
      }
    }
  }
}