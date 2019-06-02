#!/bin/bash

applicationId=$1
stageId=$2

repoHost=http://localhost:8070
endPoint=/api/v2/components/remediation/application
repoUser=admin
repoPasswd=admin123

curl -u ${repoUser}:${repoPasswd} -X POST ${repoHost}/${endPoint}/${applicationId}?stageId=${stageId} | python -m json.tool

