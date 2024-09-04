# manager-service

## You have 2 options to run this project:
### 1- Run the project with docker:
Inside of manager project, run docker-compose up --build

The API will run in http://localhost:8080/

You can access de swagger: http://localhost:8080/swagger-ui/index.html

### 2- Run the project local:
To run the project, you will need the following variables:

* DATABASE_URL - the path to your database
* DATABASE - the name of your database
* DATABASE_PASSWORD - password


Example:

* DATABASE_URL=jdbc:postgresql://localhost:5432/calculator
* DATABASE=postgres
* DATABASE_PASSWORD=123
##
## For testing purposes, there are 10 users created.
### The usernames for logging in are:
* user1@user.com
* user2@user.com
* user3@user.com
* user4@user.com
* user5@user.com
* user6@user.com
* user7@user.com
* user8@user.com
* user9@user.com
* user10@user.com


### All have the same password: "password".

## Each user has already been created with a specific value in their wallet:

* 1 - 1000.0
* 2 - 500.0
* 3 - 0.0
* 4 - 10000.0
* 5 - 2000.0
* 6 - 5000.0
* 7 - 8000.0
* 8 - 15000.0
* 9 - 3000.0
* 10 - 2000.0



## To run locally, you can use Swagger to guide you:
* http://localhost:8080/swagger-ui/index.html
###
### 1- First, generate the token for the user you want to use:

curl -X POST \
http://localhost:8080/auth/login \
-H 'cache-control: no-cache' \
-H 'content-type: application/json' \
-H 'postman-token: 73442702-0770-f545-fe5a-221ccd81b242' \
-d '{
"username": "user1@user.com",
"password": "password"
}'
###
###  2-Retrieve the operations to know the name and value of each:

curl -X GET \
http://localhost:8080/operation/operations \
-H 'accept-language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7' \
-H 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcGkgY2FsY3VsYXRvciIsInN1YiI6IjEiLCJpYXQiOjE3MjUyMjE3MDQsImV4cCI6MTcyNTMwODEwNH0.Rav-YRJI--r0S8MLlcxSl6PIdzGzhexUW7TPKnTtVew' \
-H 'cache-control: no-cache' \
-H 'postman-token: a3a4cbfa-b0c1-23d2-ec59-3fcbdd6ac360'
###
### 3-Perform the operation by passing the type and numbers (for RANDOM_STRING, you don’t need to pass numbers, and for SQUARE_ROOT, only the first number is needed):

curl -X POST \
http://localhost:8080/operation/realize \
-H 'accept-language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7' \
-H 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcGkgY2FsY3VsYXRvciIsInN1YiI6IjEiLCJpYXQiOjE3MjUyMjIwMjUsImV4cCI6MTcyNTMwODQyNX0.ZKDICkz5RnKnCJpoBz7X0wbuTuq8S3lN7iHlW9RIKcs' \
-H 'cache-control: no-cache' \
-H 'content-type: application/json' \
-H 'postman-token: 16246c28-2068-12d4-dd44-769f5b256e07' \
-d '{
"number1": 0,
"number2": 0,
"type": "ADDITION"
}'
###
### 4-To know the remaining balance of the user, use the following endpoint:

curl -X GET \
http://localhost:8080/user/balance \
-H 'accept-language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7' \
-H 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcGkgY2FsY3VsYXRvciIsInN1YiI6IjEiLCJpYXQiOjE3MjUyMjIwMjUsImV4cCI6MTcyNTMwODQyNX0.ZKDICkz5RnKnCJpoBz7X0wbuTuq8S3lN7iHlW9RIKcs' \
-H 'cache-control: no-cache' \
-H 'connection: keep-alive' \
-H 'postman-token: a17f7d5c-eb35-dbc8-4a4c-3504fdfe8f09'
###
### 5-The user’s records can be found at the following endpoint, where parameters such as page, which is the current page, size, which is the number per page, and inactive=false, which indicates only active records:

curl -X GET \
'http://localhost:8080/user/records?page=0&size=10&inactive=false' \
-H 'accept-language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7' \
-H 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcGkgY2FsY3VsYXRvciIsInN1YiI6IjEiLCJpYXQiOjE3MjUyMjIwMjUsImV4cCI6MTcyNTMwODQyNX0.ZKDICkz5RnKnCJpoBz7X0wbuTuq8S3lN7iHlW9RIKcs' \
-H 'cache-control: no-cache' \
-H 'postman-token: 3a3c0f86-7e81-3463-be50-b7919b790e70'

###
### 6-To deactivate a record, use the following endpoint, where the inactive field is set to true. If you want to activate it again, just set inactive=false:

curl -X PUT \
'http://localhost:8080/user/record/1/inactivate?inactive=true' \
-H 'accept-language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7' \
-H 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcGkgY2FsY3VsYXRvciIsInN1YiI6IjEiLCJpYXQiOjE3MjUyMjIwMjUsImV4cCI6MTcyNTMwODQyNX0.ZKDICkz5RnKnCJpoBz7X0wbuTuq8S3lN7iHlW9RIKcs' \
-H 'cache-control: no-cache' \
-H 'postman-token: 8299f3e0-7861-bd8f-4292-a030dc8a7fc7'

###
### 7-To query inactive records, you can use the endpoint indicated in number 5 with the value inactive=true:

'http://localhost:8080/user/records?page=0&size=10&inactive=true' \
-H 'accept-language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7' \
-H 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcGkgY2FsY3VsYXRvciIsInN1YiI6IjEiLCJpYXQiOjE3MjUyMjIwMjUsImV4cCI6MTcyNTMwODQyNX0.ZKDICkz5RnKnCJpoBz7X0wbuTuq8S3lN7iHlW9RIKcs' \
-H 'cache-control: no-cache' \
-H 'postman-token: f45bdaa3-a113-2835-ab28-26fa7109c131'

###
### 8- Check if token is valid
curl -X GET \
http://localhost:8080/auth/check \
-H 'accept-language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7' \
-H 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcGkgY2FsY3VsYXRvciIsInN1YiI6IjEiLCJpYXQiOjE3MjUyMjIwMjUsImV4cCI6MTcyNTMwODQyNX0.ZKDICkz5RnKnCJpoBz7X0wbuTuq8S3lN7iHlW9RIKcs' \
-H 'cache-control: no-cache' \
-H 'postman-token: c8202c61-8672-34f2-7398-ea815c50760c'

###
### 9-Finally, to log out, use the endpoint:

curl -X POST \
http://localhost:8080/auth/sign-out \
-H 'accept-language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7' \
-H 'authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcGkgY2FsY3VsYXRvciIsInN1YiI6IjEiLCJpYXQiOjE3MjUyMjIwMjUsImV4cCI6MTcyNTMwODQyNX0.ZKDICkz5RnKnCJpoBz7X0wbuTuq8S3lN7iHlW9RIKcs' \
-H 'cache-control: no-cache' \
-H 'postman-token: c8202c61-8672-34f2-7398-ea815c50760c'