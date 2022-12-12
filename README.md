# Getting Started

## Overview

You’re implementing a part of a shopping platform. Design and implement a service 
that will provide a REST API for calculating a price for a given product and its 
amount. Products in the system are identified by UUID. There should be the 
possibility of applying discounts based on two policies – amount based (the more 
pieces of the product are ordered, the bigger the discount is) and percentage 
based. Policies should be configurable.

## How to run the application

To run application you have to execute the following command:

```
mvn spring-boot:run
```

## Api Documentation

```
http://localhost:8080/swagger-ui/
```