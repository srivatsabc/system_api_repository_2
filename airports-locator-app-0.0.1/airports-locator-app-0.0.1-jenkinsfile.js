pipeline {

  environment {
     GIT_SUBDIRECTORY = "airports-locator-app-0.0.1"
     GIT_REPO_URL = "https://github.com/srivatsabc/system_api_repository_2.git"
     OKD_APP = "airports-locator-app-v001"
     OKD_NAMESPACE = "system-api-ns"
     CONFIG_MAP = "airports-locator-app-v001-config"
     DOCKER_ID = "srivatsabc"
     DOCKER_REPO = "airports-locator-app"
     DOCKER_TAG = "os-s-api-v0.0.1"
     DOCKER_PWD = "wipro123"
     DEPLOYMENT_YAML = "airports-locator-app-0.0.1-deployment.yaml"
     SERVICE_YAML = "airports-locator-app-0.0.1-service.yaml"
     DOCKER_REGISTRY = "docker-registry.default.svc:5000"
   }

  agent {
      label "master"
  }

  stages {
     stage('Checkout') {
          steps {
            checkout([$class: 'GitSCM',
              branches: [[name: 'master']],
              doGenerateSubmoduleConfigurations: false,
              extensions: [[$class: 'SparseCheckoutPaths',  sparseCheckoutPaths:[[$class:'SparseCheckoutPath', path:"${GIT_SUBDIRECTORY}/"]]]                        ],
              submoduleCfg: [],
              userRemoteConfigs: [[credentialsId: 'srivatsabc_git_login', url: "${GIT_REPO_URL}"]]])

            sh "ls -lat"
          }
      }
	
	stage('Build in maven') {
	      steps {
	        sh "echo building $GIT_SUBDIRECTORY/pom.xml"
	        sh 'mvn -B -f $GIT_SUBDIRECTORY/pom.xml clean install'
	      }
	   }

    stage('OpenShift deployment delete') {
        steps {
          script {
            sh "echo deleting the current OpenShift deployment $OKD_APP from namespace $OKD_NAMESPACE"
            status = sh(returnStatus: true, script: "oc delete deployment $OKD_APP --namespace=$OKD_NAMESPACE")
            if (status == 0){
              stage('OpenShift service delete') {
                  script{
                    sh "echo deleting the current OpenShift service $OKD_APP from namespace $OKD_NAMESPACE"
                    status = sh(returnStatus: true, script: "oc delete service $OKD_APP --namespace=$OKD_NAMESPACE")
                    if (status == 0){
                      stage('Deleting current docker image from local repo'){
                        sh "echo deleting docker image from local $DOCKER_REGISTRY/$OKD_NAMESPACE/$DOCKER_REPO:$DOCKER_TAG"
                        status = sh(returnStatus: true, script: "docker rmi $DOCKER_REGISTRY/$OKD_NAMESPACE/$DOCKER_REPO:$DOCKER_TAG -f")
                        if (status == 0){
                          sh "echo Delete kube deployment service and docker image successfully"
                        }else{
                          stage('Nothing docker image to delete'){
                            sh "echo no docker image to delete in local repo"
                          }
                        }
                      }
                    }else{
                      stage('No OpenShift service to delete'){
                        sh "echo no service available in OpenShift"
                      }
                    }
                  }
              }
            }else{
              stage('No OpenShift deployment to delete'){
                sh "echo no deployment available in OpenShift"
              }
            }
          }
        }
      }

    stage('Build docker image') {
      steps {
        sh "echo build docker image $DOCKER_ID/$DOCKER_REPO:$DOCKER_TAG"
        sh 'docker build -t $DOCKER_ID/$DOCKER_REPO:$DOCKER_TAG $GIT_SUBDIRECTORY/.'
      }
    }

    stage('Docker Image Re-Tag') {
      steps {
        sh "echo docker tag $DOCKER_ID/$DOCKER_REPO:$DOCKER_TAG $DOCKER_REGISTRY/$OKD_NAMESPACE/$DOCKER_REPO:$DOCKER_TAG"
        sh 'docker tag $DOCKER_ID/$DOCKER_REPO:$DOCKER_TAG $DOCKER_REGISTRY/$OKD_NAMESPACE/$DOCKER_REPO:$DOCKER_TAG'
      }
    }

    stage('Docker Internal Registry login') {
      steps {
        sh 'docker login -u okd-admin -p `oc whoami -t` $DOCKER_REGISTRY'
      }
    }

    stage('Docker Create ImageStream') {
      steps {
        script {
          sh 'echo oc create is $DOCKER_REPO -n $OKD_NAMESPACE'
          statusCreate = sh(returnStatus: true, script: "oc create is $DOCKER_REPO -n $OKD_NAMESPACE")
          if (statusCreate != 0){
            sh "echo ImageStream $DOCKER_REPO already exists under $OKD_NAMESPACE ns"
          }else{
            stage('OpenShift ImageStream created'){
              sh "echo OpenShift ImageStream successfully created"
            }
          }
        }
      }
    }

    stage('Docker Push to Internal Registry') {
      steps {
        sh "echo Pushing $DOCKER_REGISTRY/$OKD_NAMESPACE/$DOCKER_REPO:$DOCKER_TAG to Internal Registry"
        sh 'docker push $DOCKER_REGISTRY/$OKD_NAMESPACE/$DOCKER_REPO:$DOCKER_TAG'
      }
    }

    stage('Delete Local Docker images') {
      steps {
        sh "docker rmi $DOCKER_REGISTRY/$OKD_NAMESPACE/$DOCKER_REPO:$DOCKER_TAG -f"
        sh 'docker rmi $DOCKER_ID/$DOCKER_REPO:$DOCKER_TAG -f'
      }
    }

    stage('OpenShift configmap') {
        steps {
          script {
            sh "echo creating oc create -n $OKD_NAMESPACE configmap $CONFIG_MAP --from-literal=RUNTIME_ENV_TYPE=k8s"
            statusCreate = sh(returnStatus: true, script: "oc create -n $OKD_NAMESPACE configmap $CONFIG_MAP --from-literal=RUNTIME_ENV_TYPE=k8s")
            if (statusCreate != 0){
              sh "echo Unable to create $CONFIG_MAP in $OKD_NAMESPACE as it already exists"
            }else{
              stage('OpenShift configmap created'){
                sh "echo OpenShift configmap successfully created"
              }
            }
          }
        }
      }

    stage('OpenShift deployment') {
      steps {
        sh 'oc whoami'
        sh 'oc apply -n $OKD_NAMESPACE -f $GIT_SUBDIRECTORY/$DEPLOYMENT_YAML'
      }
    }

    stage('OpenShift service') {
      steps {
        sh 'oc apply -n $OKD_NAMESPACE -f $GIT_SUBDIRECTORY/$SERVICE_YAML'
      }
    }
  }
}
