import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class ApplicationPolicyViolations {

    static void main(String[] args){

        def repositoryUrl = args[0]
        def applicationName = args[1]
        def policyId = args[2]

        def endpoint = repositoryUrl + '/api/v2/policyViolations'
        def query = '?p=' + policyId
        def repoAddress = endpoint + query

        def url = new URL(repoAddress)
        def violationsConnection = url.openConnection()

        violationsConnection.requestMethod = 'GET'
        violationsConnection.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")

        if (violationsConnection.responseCode == 200) {
            def violationsContent = violationsConnection.content.text

            def jsonSlurper = new JsonSlurper()
            def violationsJsonObject = jsonSlurper.parseText(violationsContent)

            violationsJsonObject.applicationViolations.each {


                String currentApplicationName = it.application.name
                String currentApplicationId = it.application.id

                if (currentApplicationName.equals(applicationName)){

                    println ''
                    println 'Application:'
                    println JsonOutput.prettyPrint(JsonOutput.toJson(it))

                    it.policyViolations.each {

                        println ''
                        println 'Remediation Details'
                        println '-------------------'

                        String violationScanStage = it.stageId
                        String violationComponentFormat = it.component.componentIdentifier.format

                        if (violationComponentFormat.equals('maven')){

                            Object coordinates = it.component.componentIdentifier.coordinates
                            Object coordinatesJson = JsonOutput.toJson(coordinates)
                            String componentDetails = '{"componentIdentifier": {"format":"maven", "coordinates":' + coordinatesJson + '}}'

                            def remediationAddress = 'http://localhost:8070/api/v2/components/remediation/application/' + currentApplicationId + '?stageId=' + violationScanStage
                            def remediationUrl = new URL(remediationAddress)

                            def remediationConnection = remediationUrl.openConnection()

                            remediationConnection.requestMethod = 'POST'
                            remediationConnection.setDoOutput(true)
                            remediationConnection.setRequestProperty("Content-Type", "application/json")
                            remediationConnection.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4xMjM=")

                            remediationConnection.getOutputStream().write(componentDetails.getBytes("UTF-8"))

                            if(remediationConnection.responseCode.equals(200)) {
                                def remediationDetails = remediationConnection.getInputStream().getText()
                                println JsonOutput.prettyPrint(remediationDetails)
                            }
                        }
                    }
                }
            }
        }
    }
}
