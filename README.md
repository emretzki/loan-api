# Loan API

## Overview
Loan API is a Spring Boot-based RESTful API that allows customers to create loans and make payments on their loans. The API dynamically adjusts interest rates based on the selected installment period and enforces payment rules accordingly.

## Technologies Used
- Java 17
- Spring Boot 3.4.2
- Maven
- Swagger UI for API documentation

## Initialization
During the initialization phase, three default customers are created. All loan creation and payment transactions must be performed using these customers.

## Features

### Create Loan
- A loan can be created based on an installment plan (6, 9, 12, or 24 months).
- Interest rates increase proportionally with the installment count.
- The total loan amount (loanAmount * interestRate) cannot exceed the customer's available credit limit.
- Early payment results in a discount on installment amounts, while late payments incur additional interest.

### Pay Loan
- Installments can only be paid within a 3-month period from the loan creation date.
- Installment payments must be made in full; partial payments are not allowed.
- The loan is marked as "paid" once all installments have been completed.

## Installation & Setup

### Clone the Repository
```sh
git clone https://github.com/emretzki/loan-api.git
cd loan-api
```

### Build the Project
```sh
mvn clean install
```

### Run the Application
```sh
mvn spring-boot:run
```

Postman Collection

You can use the provided Postman collection for easier API testing.

[loan-api.postman_collection.json](https://github.com/user-attachments/files/18647665/loan-api.postman_collection.json)

## Running the API Locally
Once the application is running, you can interact with the API via Swagger UI or directly using tools like Postman or cURL.


---


