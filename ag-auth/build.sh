#!/usr/bin/env bash
docker build -t 192.168.157.130/ag-admin/ag-auth:latest .
docker push 192.168.157.130/ag-admin/ag-auth:latest