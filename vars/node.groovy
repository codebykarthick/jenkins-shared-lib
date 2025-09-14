def call(String type, Closure body) {
    def yamlPath

    if (type == "python3") {
        yamlPath = 'yaml/python3-agent.yaml'
    } else {
        error "Unknown agent type: ${type}. Only 'python3' are supported."
    }

    def podYaml = libraryResource(yamlPath)

    podTemplate(yaml: podYaml, inheritFrom: '', namespace: 'apps') {
        node(POD_LABEL) {
            body()
        }
    }
}
