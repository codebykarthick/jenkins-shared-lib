def call(Map config = [:]) {
    /**
    Attempt deploying the manifest to the local kubernetes cluster. Only proceeds if the deployment
    is successful (by default which can be overriden),
    */
    def manifestPath = config.get('manifests', 'deploy/template.yaml')
    def containerName = config.get('container', 'kubectl')
    def namespace     = config.get('namespace', 'apps')
    def waitForRollout = config.get('wait', true)   // default: true

    stage("Kubectl deploy: ${manifestPath} -> ${namespace}") {
        container(containerName) {
            sh """
              echo "Applying manifests from ${manifestPath} to namespace ${namespace}"
              kubectl apply -f ${manifestPath} -n ${namespace}
            """

            if (waitForRollout) {
                // check every Deployment in the manifestPath
                def deployments = sh(
                    script: "kubectl get -f ${manifestPath} -o name | grep deployment/ || true",
                    returnStdout: true
                ).trim()

                if (deployments) {
                    deployments.split("\\n").each { dep ->
                        sh """
                          echo "Waiting for rollout of ${dep} in ${namespace}"
                          kubectl rollout status ${dep} -n ${namespace} --timeout=120s
                        """
                    }
                } else {
                    echo "No Deployments found in ${manifestPath}, skipping rollout wait"
                }
            }
        }
    }
}
