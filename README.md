![Node.js](https://img.shields.io/badge/node-18.19.8-brightgreen)
![Angular](https://img.shields.io/badge/angular-19.2.1-red)
![Java](https://img.shields.io/badge/java-17-blue)
![Spring Boot](https://img.shields.io/badge/spring--boot-3.3.5-brightgreen)
![Maven](https://img.shields.io/badge/maven-3.8-orange)
![Swagger](https://img.shields.io/badge/swagger--springdoc-2.6.0-blue)

# P6-Full-Stack-Reseau-Dev

This project is a **Full-Stack application** built with an **Angular front-end** and a **Java Spring Boot back-end**.  
Each part is contained in its own folder with its own dependencies and setup instructions.

---

## 🚀 Tech Stack

- **Front-end :** Angular 19, Node.js >= 18
- **Back-end :** Java 17, Spring Boot 3.3.5
- **Build Tools :** Maven >= 3.8
- **API Documentation :** Swagger-Springdoc 2.6.0

---

## 📄 Project Structure
```
/ project-root
│── front/       # Angular Project
│── back/        # Java Spring Boot Project
│── README.md    # This file
```

## Prerequisites

Before getting started, make sure you have installed the following tools:

### For Front-end
- [Node.js (>=18)](https://nodejs.org/)
- [Angular CLI](https://angular.dev/installation)

### For Back-end
- [JDK 11+](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- [Maven 3.8+](https://maven.apache.org/)
## Installation and Set Up

### 1. Start the project

Clone the project

> git clone https://github.com/CelineIntha/P6_Developpez-une-application-full-stack.git


### 2. Back-end (Java Spring Boot)

📌 **Detailed instructions in** [`/back/README.md`](./back/README.md)

```sh
cd back
mvn clean install
mvn spring-boot:run
```

The back-end will run on `http://localhost:8080`.

### 3. Front-end (Angular)

📌 **Detailed instructions in** [`/front/README.md`](./front/README.md)

```
cd front
npm install
ng serve
```

The Angular application will be accessible at `http://localhost:4200`.


---

📌 Check the specific README files for more instructions:
- [Front-end (Angular)](./front/README.md)
- [Back-end (Java)](./back/README.md)


## **Contact**

If you have any questions or comments about this project, please contact me at : *celine.intha@gmail.com*.
