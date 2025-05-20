#!/bin/bash

# SourDoughTree CloudFormation Deployment Script

# Set variables
S3_BUCKET="sourdoughtree-cfn-templates"
REGION="eu-west-2"
STACK_NAME="sourdoughtree-stack"
CUSTOM_DOMAIN="butter-api.sourdoughtree.com"

# Change to the CloudFormation directory
cd "$(dirname "$0")/../CloudFormation" || exit

# Create S3 bucket if it doesn't exist
aws s3api head-bucket --bucket $S3_BUCKET 2>/dev/null || aws s3 mb s3://$S3_BUCKET --region $REGION

# Upload nested template to S3
aws s3 cp ./APIGateway/api-gateway.yaml s3://$S3_BUCKET/api-gateway.yaml

# Check if stack exists and is in ROLLBACK_COMPLETE state
STACK_STATUS=$(aws cloudformation describe-stacks --stack-name $STACK_NAME --region $REGION --query 'Stacks[0].StackStatus' --output text 2>/dev/null || echo "STACK_NOT_FOUND")

if [ "$STACK_STATUS" == "ROLLBACK_COMPLETE" ]; then
  echo "Stack is in ROLLBACK_COMPLETE state. Deleting stack before redeploying..."
  aws cloudformation delete-stack --stack-name $STACK_NAME --region $REGION
  echo "Waiting for stack deletion to complete..."
  aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME --region $REGION
fi

# Deploy the CloudFormation stack
aws cloudformation deploy \
  --region $REGION \
  --template-file main-template.yaml \
  --stack-name $STACK_NAME \
  --parameter-overrides \
    ApiStageName=prod \
    CustomDomainName=$CUSTOM_DOMAIN \
  --capabilities CAPABILITY_IAM

echo "Deployment complete!"

# Get the API Gateway URL from the stack outputs
API_URL=$(aws cloudformation describe-stacks --stack-name $STACK_NAME --region $REGION --query 'Stacks[0].Outputs[?OutputKey==`ApiGatewayUrl`].OutputValue' --output text)
CUSTOM_URL=$(aws cloudformation describe-stacks --stack-name $STACK_NAME --region $REGION --query 'Stacks[0].Outputs[?OutputKey==`CustomDomainUrl`].OutputValue' --output text)

echo "API Gateway URL: $API_URL"
echo "Custom Domain URL: $CUSTOM_URL"