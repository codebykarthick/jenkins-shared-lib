def call(Map config = [:]) {
    /**
    Method to build a docker image for a given repository with a Dockerfile and publish it to GHCR.
    Needs the repository, path to the docker file and the context for publishing.
    */
    // required params
    def image      = config.get('image', error('You must provide image: "ghcr.io/org/repo:tag"'))
    def dockerfile = config.get('dockerfile', 'Dockerfile')
    def context    = config.get('context', '.')

    // container in the pod template
    def containerName = config.get('container', 'kaniko')

    stage("Kaniko build & push: ${image}") {
        container(containerName) {
            sh """
              /kaniko/executor \
                --context ${context} \
                --dockerfile ${dockerfile} \
                --destination ${image} \
                --snapshotMode=redo \
                --single-snapshot
            """
        }
    }
}