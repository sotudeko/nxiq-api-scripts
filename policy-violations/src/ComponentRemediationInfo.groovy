import groovy.json.JsonSlurper

class ComponentRemediationInfo {

    static void main(String[] args) {

//        def repositoryUrl = args[0]
//        def applicationName = args[1]
//        def policyId = args[2]

        def repositoryUrl = 'http://localhost:8070'
        def applicationName = 'webwolf'
        def stageId = '8a96e389819449cab1b3d1473fd860f1'

        def endpoint = repositoryUrl + '/api/v2/components/remediation/application'
        def query = '?stageId=' + stageId
        def repoAddress = endpoint + query

        def url = new URL(repoAddress)
        def remediationConnection = url.openConnection()

        remediationConnection.requestMethod = 'GET'
        remediationConnection.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")

        if (remediationConnection.responseCode == 200) {
            def violationsContent = remediationConnection.content.text

            def jsonSlurper = new JsonSlurper()
            def violationsJsonObject = jsonSlurper.parseText(violationsContent)
            println violationsJsonObject
        }
    }
}
