pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        bat 'mvn package'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git status'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git add . https://HristoVenelinovIvanov:Prodcho99325@github.com/HristoVenelinovIvanov/Project.git'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git commit -m "Push from Jenkinsfile" https://HristoVenelinovIvanov:Prodcho99325@https:github.com/HristoVenelinovIvanov/Project.git'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git push https://HristoVenelinovIvanov:Prodcho99325@github.com/HristoVenelinovIvanov/Project.git'
        bat 'cd C:\\Users\\hristo.ivanov\\TechnomarketProject\\Project && git status'
      }
    }
  }
}