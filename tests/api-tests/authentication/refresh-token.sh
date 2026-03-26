#!/bin/bash

newman run Authentication.postman_collection.json \
  --environment authentication.postman_environment.json \
  --insecure \
  --folder "On demand: Refresh token" \
  --export-environment authentication.postman_environment.json \
  
cat authentication.postman_environment.json | python3 -c '
import json,sys
env = json.load(sys.stdin)
token = next(v["value"] for v in env["values"] if v["key"] == "accessToken")
print("")
print("Copy the json response below:")
print("----------------------------")
print(json.dumps({"authorization": "Bearer " + token}, indent=2))
print("----------------------------")
'
