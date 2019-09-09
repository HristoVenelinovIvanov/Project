pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        bat 'mvn package'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git status'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git add . https://github.com/HristoVenelinovIvanov/Project.git'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git commit -m "Push from Jenkinsfile" https://github.com/HristoVenelinovIvanov/Project.git'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git push https://github.com/HristoVenelinovIvanov/Project.git'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git status'
      }
    }
  }
}