#!/bin/bash

# Set variables
REGION="eu-west-2"
CUSTOM_DOMAIN="butter-api.sourdoughtree.com"

# Check custom domain mappings
echo "Checking custom domain mappings for $CUSTOM_DOMAIN..."
aws apigateway get-base-path-mappings \
  --domain-name $CUSTOM_DOMAIN \
  --region $REGION

# Get API ID from stack outputs
STACK_NAME="sourdoughtree-stack"
API_ID=$(aws cloudformation describe-stacks \
  --stack-name $STACK_NAME \
  --region $REGION \
  --query 'Stacks[0].Outputs[?OutputKey==`ApiId`].OutputValue' \
  --output text)

echo "API Gateway ID: $API_ID"

# List all APIs to confirm
echo "Listing all API Gateways:"
aws apigateway get-rest-apis --region $REGION

echo "To manually verify in AWS Console:"
echo "1. Go to API Gateway Console: https://eu-west-2.console.aws.amazon.com/apigateway/main/apis?region=eu-west-2"
echo "2. Select 'Custom Domain Names' from the left navigation"
echo "3. Click on '$CUSTOM_DOMAIN' to view its mappings"
echo "4. Verify that it maps to your API Gateway with stage 'prod'"