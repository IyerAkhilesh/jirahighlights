JiraHighlights Chatbot -
  This project is a microservices-based chatbot designed to interact with a user and provide key insights from Jira tickets using Natural Language Processing (NLP). The architecture leverages a Eureka Server for robust and scalable service discovery.

Project Overview - 
  The main goal of this microservice is to simplify the process of getting information from Jira tickets. Instead of navigating a complex interface, users can simply ask the chatbot questions in natural language. The system processes these requests, extracts relevant information from the Jira tickets, and presents a concise
  summary. This is achieved through a decoupled architecture where the chatbot, NLP, and other potential services can run and scale independently.

Key Features -
  1. Chatbot Functionality: Provides a user-friendly interface for conversational interaction.
  2. Natural Language Processing (NLP): The core of the system, this service is responsible for understanding user queries and extracting key entities and information from Jira tickets.
  3. Microservice Architecture: The project is composed of multiple, independently deployable services, which enhances scalability and maintainability.
  4. Eureka Service Discovery: A central Eureka server allows microservices to register themselves and discover other services dynamically, ensuring high availability and resilience.
  5. Version Control: Used Git/Bitbucket for project management and version control.
  6. Core Development: The services are built using the SpringBoot framework.

Architecture -
  The project's architecture follows a standard microservices pattern. All services, including the chatbot and the NLP module, register themselves with the Eureka Server. When the chatbot needs to process a user's request, it queries the Eureka server to find the location of the NLP service. This allows for seamless  
  communication and ensures the system can handle a growing number of services without hardcoding their locations.

To run this project locally, you will need to - 
  1. Clone the repository.
  2. Run "mvnw.cmd clean install" on the terminal within the home directory to build and install all Maven dependencies 
  3. Set up the Eureka Server and API Gateway.
  4. Configure the Jira URL, username and API token in the main/resources/application.properties file in the JiraIntegratorService so that the service can communicate with your Jira environment.
  5. Configure and run the Chatbot and NLP microservices.

Technologies Used - 
  1. Core Framework: SpringBoot
  2. Service Discovery: Eureka Server
  3. Version Control: Git
  4. NLP: To be decided - Terraform and OpenNLP are under consideration.
