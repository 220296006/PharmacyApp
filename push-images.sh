#!/bin/bash

# Variables
USERNAME=thabiso1996
BACKEND_IMAGE=backend:latest
MYSQL_IMAGE=mysql:8.1
FRONTEND_IMAGE=frontend:latest
ELASTICSEARCH_IMAGE=elasticsearch:7.17.7
BACKEND_TAG=$USERNAME/pharmacyapp-backend:latest
MYSQL_TAG=$USERNAME/pharmacyapp-mysql:8.1
FRONTEND_TAG=$USERNAME/pharmacyapp-frontend:latest
ELASTICSEARCH_TAG=$USERNAME/pharmacyapp-elasticsearch:7.17.7

# Log in to Docker Hub
echo "Logging in to Docker Hub..."
docker login

# Tag images
echo "Tagging images..."
docker tag $BACKEND_IMAGE $BACKEND_TAG
docker tag $MYSQL_IMAGE $MYSQL_TAG
docker tag $FRONTEND_IMAGE $FRONTEND_TAG
docker tag $ELASTICSEARCH_IMAGE $ELASTICSEARCH_TAG

# Push images
echo "Pushing images to Docker Hub..."
docker push $BACKEND_TAG
docker push $MYSQL_TAG
docker push $FRONTEND_TAG
docker push $ELASTICSEARCH_TAG

echo "Images pushed successfully."

# Push images to Docker Hub using command in git bash
# chmod +x push-images.sh
# ./push-images.sh
