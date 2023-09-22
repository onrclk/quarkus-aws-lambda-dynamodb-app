#!/bin/sh

echo "Creating table"
awslocal dynamodb create-table --cli-input-json file:///etc/localstack/init/ready.d/create-dynamo-table.json --region eu-central-1

echo "Put Item"
awslocal dynamodb put-item --table-name WeatherReport --item '{"id":{"N":"1"},"name":{"S":"Antalya"},"country":{"S":"Türkiye"},"weeklyReports":{"L":[{"M":{"temperature":{"N":"35.5"},"description":{"S":"Sunny Day"},"time":{"S":"2023-07-03T10:15:30.00Z"}}}]}}' --region eu-central-1


