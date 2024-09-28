## eCommerce Application with Microservices Architecture
The system manages user orders, payments, and notifications.
- Architecture separates large structures into small parts. Services communicate with each other via messageQueue. Serving large, complex systems, separated and implemented individually without affecting other parts.
- Pros: easy to read code, easy to scale; Use a lot of technology
- Cons: difficult to code and debug; Extreme management

## Spring Cloud
- api gateway, load balancer
- Distributed logging, tracing, metric: **OpenTelemetry**, **Prometheus**, **Grafana**, **Zipkin**
- resilient microservices: SAGA, circuit breaker, bulkHead

## Features
- Order Service: Handles order creation, confirmation, and status updates.
- Payment Service: Processes payments and sends payment confirmations.
- Notification Service: Sends email notifications to customers for order confirmation and payment success.
- Microservices Architecture: Each service is independently deployable and scalable.
- Spring Cloud: Utilized for service discovery, load balancing, and resilience in microservices.

## Domain Driven Design 
![image](https://github.com/user-attachments/assets/aef291cb-7310-4552-9183-7ff8deef6399)


## Technologies Used
- Spring Boot.
- Spring Cloud for service discovery and management.
- MailDev for email service simulation.
- Docker 

- Order Confirmation Email
- The application sends an order confirmation email upon successful order placement.


- Payment Confirmation Email
- After a successful payment, the user receives a confirmation email with the payment details.


## API Documentation
![image](https://github.com/user-attachments/assets/cfae8af9-5eb0-4b4a-99e8-7581588d8496) ![image](https://github.com/user-attachments/assets/d3e22e18-9334-4b9c-82f3-bacb474d19ed) ![image](https://github.com/user-attachments/assets/23dd7e75-a630-42a3-93f7-f81e478a7847) ![image](https://github.com/user-attachments/assets/e04640d2-8065-4088-b6ab-ebc6683d81b9)

---


## Trace flow of requests by Zipkin
![image](https://github.com/user-attachments/assets/4c010f17-4311-4a4f-8c81-09fbdbd4a3ef) ![image](https://github.com/user-attachments/assets/0d0611cc-4463-4363-86ff-0b2442c5d42b)

---

## Email notification
![Screenshot 2024-09-28 141028](https://github.com/user-attachments/assets/13465a88-5d79-4cab-b5f8-6b879743b5f7)
![Screenshot 2024-09-28 140911](https://github.com/user-attachments/assets/2ba71b9b-e8d8-479e-be3c-597779a89b78)


How to Run
- Clone the repository.
- Use Docker Compose to run the microservices.
- config server Spring (provide store & serve distributed configs on apps and env)
- implement Discovery service (config client, eureka server)
- implement customer, product, order, payment, noti services
- setup Kafka & Zookeeper
- ensure all apps start correct port and configs
- Test the email notifications using MailDev.

![Screenshot 2024-09-28 143302](https://github.com/user-attachments/assets/c0bbca70-cbe8-443b-a2eb-9fdfd1b41546) ![Screenshot 2024-09-28 143243](https://github.com/user-attachments/assets/56eae9f4-c230-4522-8341-b2253f0699d7)

@Copyright by jam-dev #thien25078@gmail.com
