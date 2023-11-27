# eprom

This project uses HashiCorp Vault for secure management of sensitive information, such as usernames and passwords.

Download and Install HashiCorp Vault:

1. Download and install HashiCorp Vault from the official website.

2. Start HashiCorp Vault in Server Mode (Locally for Development):

3. Start HashiCorp Vault in development mode with the following command:

open cmd :

 a - vault server --dev --dev-root-token-id="mytoken"
 
 copy paste the token in bootstrap.yml
 
open another cmd and Set the Vault address using the following command:

 c - set VAULT_ADDR=http://127.0.0.1:8200

4. Store Secrets in HashiCorp Vault:

Store sensitive information in HashiCorp Vault using the following command:

vault kv put secret/eprom mail.userName=username mail=password

and replace username and password with the actual credentials you want to store.

------------Docker--------------------------
## Running the Application Locally with Docker

When you want to set up and run the application locally, follow these steps:

1. **Ensure Docker is Installed:**
   Make sure you have Docker installed on your system. If not, you can download and install it from [https://www.docker.com/get-started](https://www.docker.com/get-started).

2. **Pull Docker Images:**
   Open your terminal and run the following commands to pull the Docker images for the ePROM UI and API:


   docker pull dqlick/eprom-api:10.23
   docker pull dqlick/ui-eprom:10.23
   
3.Start ePROM Application with Docker Compose:

Open a terminal and navigate to the directory where the Docker Compose file is located.
Run the following command to start the ePROM application:

  docker-compose up -d
  
  
This command will start the ePROM UI and API containers in the background.


4. Once the containers are up and running, you can access the ePROM UI by opening your web browser and navigating to http://localhost:4200.
The ePROM API should be accessible at http://localhost:8100.



5. When you're done using the application, you can stop and remove the containers using the following command:

docker-compose down

