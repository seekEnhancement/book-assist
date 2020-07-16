pipeline {
  agent any

  tools {
    jdk 'jdk-11'
    gradle 'DefaultGradle'
  }

  options {
    timeout(time: 1, unit: 'HOURS')
  }

  parameters {
    string(name: 'branch', defaultValue: 'master', description: 'checkout branch')
    string(name: 'SLACK_CHANNEL', defaultValue: '#general', description: 'woos slack channel')
    string(name: 'SLACK_TEAM_DOMAIN', defaultValue: 'woos-sre', description: 'woos slack team domain')
    string(name: 'SLACK_TOKEN_CREDENTIALID', defaultValue: 'slack-jenkins-ci-credentials', description: 'slack token credential id' )
  }

  stages {
    stage('Prepare & Checkout') {

      steps {
        checkout([$class: 'GitSCM',
                  branches: [[name: "*/${params.branch}"]],
                  doGenerateSubmoduleConfigurations: false,
                  extensions: [[$class: 'LocalBranch', localBranch: params.branch]],
                  submoduleCfg: [],
                  userRemoteConfigs: [[url: 'https://bitbucket.org/seekenhancement/book-assist.git']]])

        gradlew 'clean'
      }
    }

    stage('Compile') {
      steps {
        gradlew 'compileJava'
      }
    }

    stage('Junit Test') {
      steps {
        gradlew 'test'
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'build/test-results/test/TEST-*.xml'
        }
      }
    }

    stage('Verification') {
      steps {
        gradlew 'check jacocoTestReport -x test'
        step([$class: 'CheckStylePublisher', canComputeNew: false, pattern: 'build/reports/checkstyle/main.xml'])
        step([$class: 'PmdPublisher', canComputeNew: false, pattern: 'build/reports/pmd/main.xml'])
        step([$class: 'DryPublisher', canComputeNew: false, pattern: 'build/reports/cpd/cpdCheck.xml'])
        step([$class: 'JacocoPublisher', classPattern: 'build/classes', execPattern: 'build/jacoco/test.exec'])
        step([$class: 'AnalysisPublisher', canComputeNew: false])
      }
    }

    stage('SonarQube analysis') {
      steps {
        script {
          def scannerHome = tool 'SonarqubeScanner';
          def branchName = "${params.branch}"
          withSonarQubeEnv('SonarqubeServer') {
            sh """
              \"${scannerHome}/bin/sonar-scanner"  -Dsonar.projectKey=book-assist:${branchName} \
                                                -Dsonar.projectName=book-assist \
                                                -Dsonar.projectVersion=${branchName} \
                                                -Dsonar.dynamicAnalysis=reuseReports \
                                                -Dsonar.sources=./src/main \
                                                -Dsonar.java.binaries=./build/classes \
                                                -Dsonar.tests=./src/test \
                                                -Dsonar.junit.reportPaths=./build/test-results/test \
                                                -Dsonar.jacoco.reportPath=./build/jacoco/test.exec \
                                                -Dsonar.java.coveragePlugin=jacoco
            """
          }
        }
      }
    }

    stage('Packaging') {
      steps {
        gradlew 'build -x test -x check'
      }
    }

  }

  post {
    always {
      archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
    }
    success {
      slack('good', 'Success')
    }
    failure {
      slack('danger', 'Failure')
    }
  }
}

def gradlew(String tasks) {
  sh "./gradlew ${tasks}"
}

def slack(String color, String prefixMessage) {
  slackSend channel: "${params.SLACK_CHANNEL}",
            color: color,
            message: "${prefixMessage}\nJob:${env.JOB_NAME}\nBuild:${env.BUILD_NUMBER}\nBranch:${params.branch}\n${env.BUILD_URL}",
            teamDomain: "${params.SLACK_TEAM_DOMAIN}",
            tokenCredentialId: "${params.SLACK_TOKEN_CREDENTIALID}"
}