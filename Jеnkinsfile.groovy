pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        bat 'mvn package'
      }
    }
    stage('build') {
      steps {
        bat 'mvn package'
      }
    }
  }
}