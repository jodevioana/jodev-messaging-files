# Sending files in message driven microservices - Spring Cloud, RabbitMQ, GridFS (MongoDB)

Though messaging systems are very helpful in an asynchronous environment, sending big files through brokers might have an impact on your performance. 
Thus, using an extra database for storing files and then send in identification to it through messages is one of the solution to this problem.

This example uses GridFS (from MongoDB) to store the files that need to be sent through messages, RabbitMQ for message queues and Spring Cloud for creating the services and coordinate them. It also uses authorization through an Authorization Server with Oauth2 for the incoming messages.

It contains the following modules:
- Eureka Server for service discovery
- Authorization Server - authentication and authorization among the services using Oauth2 and JWT
- Client - will send the files to RabbitMQ and will also listen for incoming responses
- Resource Server - will listen to RabbitMQ for incoming messages with files to process and will send back responses also through messages

##Flow for Client:

### Sending
1. Client has a file that wants to send through a message to RabbitMQ using a queue for requests (files-out)
2. Client saves the file in GridFs and keeps the id from the database as identification to be sent in the message
3. Client acquires authentication token from Authorization Server that will be added to the message
4. Client fires and forgets the message to RabbitMQ that should contain: an id of the file from GridFS and the authorization token. Other fields may be added to the message depending of the needs.

### Receiving
1. Client listens to RabbitMQ for incoming messages to another queue for responses (files-in)
2. Client verifies if the message is authorized through JWT Store
3. If authorized, Client uses the id from the message to match it with its correspondent file.


## Flow for Resource Server
1. Resource Server listens to the queue for requests where the Client sent the messages (files-out)
2. Incoming message with a file identification, authentication token and other fields.
3. Resource Server check the token through the JWT Store
4. If authorized, the file is retrieved from GridFS based on the id in the message
5. The file is later processed and the response sent to the queue the Client listens to (files-in)

