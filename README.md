# AwsS3BucketApp
Project that make spring boot interacts with s3 bucket


# How to build a docker image and run it in docker container.

1. Run this command to build the image ->   docker build --tag=aws-bucket-app:latest .
2. Run this command to run the image ->   docker run -p8887:8888 aws-bucket-app:latest


This will start our application in Docker, and we can access it from the host machine at localhost:8887/messages.
Here it's important to define the port mapping, which maps a port on the host (8887) to the port inside Docker (8888).
This is the port we defined in the properties of the Spring Boot application.
Note: Port 8887 might not be available on the machine where we launch the container. In this case, the mapping might not work and we need to choose a port that's still available.
