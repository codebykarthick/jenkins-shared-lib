def call(Map config = [:]) {
    def requirements = config.get('requirements', 'requirements.txt')
    def reportFile   = config.get('report', 'reports/test-results.xml')

    stage('Install dependencies') {
        sh "pip install -r ${requirements}"
    }

    stage('Run unit tests') {
        sh "pytest --junitxml=${reportFile}"
        junit reportFile
    }
}
