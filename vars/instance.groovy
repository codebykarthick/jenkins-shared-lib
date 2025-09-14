def AGENTS = [
    python3: 'yaml/python3-agent.yml'
]

def call(String type, Closure body) {
    /**
    Method to wrap around node method for abstracting the YAML file. This is to prevent
    manual updation of a pod-template everytime I refien the YAML. 

    This can also be extended by just adding more agents under resources/yaml/*.
    */
    if (!AGENTS.containsKey(type)) {
        error "Unknown agent type: ${type}. Supported: ${AGENTS.keySet().join(', ')}"
    }

    def podYaml = libraryResource(AGENTS[type])


    podTemplate(yaml: podYaml, namespace: 'apps') {
        node(POD_LABEL) {
            body()
        }
    }
}
