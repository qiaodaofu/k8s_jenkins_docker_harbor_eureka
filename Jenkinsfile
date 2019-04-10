pipeline {
    agent any
    environment {
        HARBOR_CREDS = credentials('jenkins-harbor-creds')
        K8S_CONFIG = credentials('jenkins-k8s-config')
        GIT_TAG = sh(returnStdout: true,script: 'git describe --tags').trim()
    }
    parameters {
        string(name: 'HARBOR_HOST', defaultValue: '192.168.137.134:5000', description: 'harbor仓库地址')
        string(name: 'DOCKER_IMAGE', defaultValue: 'library/test', description: 'docker镜像名')
        string(name: 'APP_NAME', defaultValue: 'test', description: 'k8s中标签名')
        string(name: 'K8S_NAMESPACE', defaultValue: 'deployment', description: 'k8s的namespace名称')
		text(name: 'DEPLOY_TEXT', defaultValue: 'One\nTwo\nThree\n', description: '测试')
		choice(name: 'CHOICES', choices: [GIT_TAG], description: '下拉列表')
		file(name: "FILE", description: "Choose a file to upload")
    }
    stages {
        stage('Maven Build') {
            when { expression { env.GIT_TAG != null } }
            agent {
                docker {
                    image 'maven:3-jdk-8-alpine'
                    args '-v $HOME/.m2:/root/.m2'
                }
            }
            steps {
                echo  env.GIT_TAG
                sh 'mvn clean package -D file.encoding=UTF-8 -D skipTests=true'
                stash includes: 'target/*.jar', name: 'app'
            }
        }
        stage('Docker Build') {
            when {
                allOf {
                    expression { env.GIT_TAG != null }
                }
            }
            agent any
            steps {
                unstash 'app'
                sh "docker login -u ${HARBOR_CREDS_USR} -p ${HARBOR_CREDS_PSW} ${params.HARBOR_HOST}"
                sh "docker build --build-arg JAR_FILE=`ls target/*.jar |cut -d '/' -f2` -t ${params.HARBOR_HOST}/${params.DOCKER_IMAGE}:${GIT_TAG} ."
                sh "docker push ${params.HARBOR_HOST}/${params.DOCKER_IMAGE}:${GIT_TAG}"
                sh "docker rmi ${params.HARBOR_HOST}/${params.DOCKER_IMAGE}:${GIT_TAG}"
            }
        }
        stage('Deploy') {
            when {
                allOf {
                    expression { env.GIT_TAG != null }
                }
            }
            agent {
                docker {
                    image 'lwolf/helm-kubectl-docker'
                }
            }
            steps {
                sh "mkdir -p ~/.kube"
                sh "echo ${K8S_CONFIG} | base64 -d > ~/.kube/config"
                sh "sed -e 's#{IMAGE_URL}#${params.HARBOR_HOST}/${params.DOCKER_IMAGE}#g;s#{IMAGE_TAG}#${GIT_TAG}#g;s#{APP_NAME}#${params.APP_NAME}#g;s#{SPRING_PROFILE}#k8s-test#g' k8s-deployment.tpl > k8s-deployment.yml"
			    sh "kubectl delete -n ${params.K8S_NAMESPACE} ${params.K8S_NAMESPACE}.apps/${params.APP_NAME}-deployment"
				sh "kubectl apply -f k8s-deployment.yml --namespace=${params.K8S_NAMESPACE}"
            }
        }
    }
}