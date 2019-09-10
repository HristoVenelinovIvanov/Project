pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        bat 'mvn package'
      }
    }
    stage('Test') {
      steps {
        bat 'mvn test'
      }
    }
  }
}