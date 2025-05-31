#!/bin/bash

# SourDoughTree CloudFormation Deletion Script

# Parse command line arguments
NO_PROMPT=false
while [[ $# -gt 0 ]]; do
  case $1 in
    --no-prompt)
      NO_PROMPT=true
      shift
      ;;
    *)
      shift
      ;;
  esac
done

# Set variables
REGION="eu-west-2"
STACK_NAME="sourdoughtree-stack"
S3_BUCKET="sourdoughtree-cfn-templates"

# Delete the CloudFormation stack
echo "Deleting CloudFormation stack: $STACK_NAME..."
aws cloudformation delete-stack --stack-name $STACK_NAME --region $REGION

# Wait for stack deletion to complete
echo "Waiting for stack deletion to complete..."
aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME --region $REGION

# Optional: Delete the S3 bucket used for templates
if [ "$NO_PROMPT" = false ]; then
  read -p "Do you want to delete the S3 bucket ($S3_BUCKET) as well? (y/n): " DELETE_BUCKET
  if [[ $DELETE_BUCKET == "y" || $DELETE_BUCKET == "Y" ]]; then
    echo "Emptying and deleting S3 bucket: $S3_BUCKET..."
    aws s3 rm s3://$S3_BUCKET --recursive --region $REGION
    aws s3api delete-bucket --bucket $S3_BUCKET --region $REGION
    echo "S3 bucket deleted."
  fi
fi

echo "Stack deletion complete!"