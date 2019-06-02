#!/bin/bash

curl -u admin:admin123 -X GET http://localhost:8070/api/v2/policies | python -m json.tool
