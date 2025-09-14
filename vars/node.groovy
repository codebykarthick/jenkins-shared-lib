def call(String type, Closure body) {
    if (type != "python3") {
        error "Unknown agent type: ${type}. Only 'python3' is supported right now."
    }

    if (type == "python3") {
        def podYaml = libraryResource('yaml/python3-agent.yaml')
    }

    podTemplate(yaml: podYaml, inheritFrom: '', namespace: 'apps') {
        node(POD_LABEL) {
            body()
        }
    }
}