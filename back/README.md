# Back-end - Spring Boot MDD API

This is the back-end API of the **P6 Full-Stack Reseau Dev** project.  
It is built with **Java 17**, **Spring Boot 3.3.5**, and connected to a **MySQL** database.


##  ⚙️ Installation & Configuration

To install and run the project, follow these steps:

1. Use a local server like MAMP, XAMPP, or WAMP to host your MySQL database. Make sure to update the database connection details in the `application.properties` (or create a **.env**) file.


2. **Configure your MySQL database:**
    - Open the `application.properties` (or **.env**) file and update it with the following details:

        - **Database URL**:
          ```properties
          spring.datasource.url=${SPRING_DATASOURCE_URL}
          ```  
          *Example for MAMP*:
          ```properties
          spring.datasource.url=jdbc:mysql://localhost:3306/mdd?allowPublicKeyRetrieval=true
          ```

        - **Your DB Username**:
          ```properties
          spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
          ```

        - **Your DB Password**:
          ```properties
          spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
          ```

        - **JWT Secret Key** (Ensure the key is sufficiently long and encoded in **Base64** for it to work properly. The key should be **256 bits** long for the **HS256** algorithm):
          ```properties
          security.jwt.secret-key=${SECURITY_JWT_SECRET_KEY}
          ```
5. Install the project dependencies by running the following command:
   `   mvn clean install`
6. When the build is completed, you can start the application by running:
   ` mvn spring-boot:run`
7. The application will run and the database will then be created.

### Optional ###

#### 🗄 Database (MySQL)

You can also set up the database schema and run the SQL script

**Default admin account credentials:**
- **Email**: `celine@email.com`
- **Password**: `Azerty123!`



## **Project Structure**

- **src/main/java:** Contains the Java source code.
- **src/main/resources**: Contains configuration files (application.properties).

## Key Dependencies

- **Spring Boot 3.3.5** : Main framework for application development.
- **Spring Security** : Provides endpoint security.
- **Spring Data JPA** : Enables database interaction using JPA.
- **MySQL Connector** : Handles connexion to the MySQL database.


## API Documentation

This application uses Swagger (via Springdoc OpenAPI) to provide an interactive API documentation.

# Accessing Swagger

Once the application is running, you can access the Swagger UI at the following URL :

```
http://localhost:8080/swagger-ui/index.html
```

You will be able to :
- View all available API endpoints.
- Test API requests directly from the browser.
- Access detailed descriptions of each endpoint's parameters and responses.

# Swagger Authentication with JWT
To access the secured endpoints, you need to provide a valid JWT token.

Follow these steps:

1. **Obtain a JWT Token:** Use the authentication endpoint **`localhost:8080/api/auth/login`** (in the "**Authentication**" section in Swagger) to generate a JWT token by providing valid user credentials.

Example request :
```
{
  "usernameOrEmail": "your@email.com",
  "password": "your-password"
}   

```

You will then obtain the following response :
```
{
    "token": "your-token",
    "expiresIn": "token-expiration-time"
    "timestamp": "time"
}
```
2. **If you don't have an account** : use the endpoint **localhost:8080/api/auth/register** and create an account.

```
{
    "email": "your@email.com",
    "username": "your-username",
    "password": "your-password"
}   

```

3. **Add the Token in Swagger:** : In Swagger UI, click on the Authorize button (located at the top-right corner).

4. After authorization, you can test the secured endpoints directly from the Swagger interface.

## **Contact**

If you have any questions or comments about this project, please contact me at : *celine.intha@gmail.com*.
