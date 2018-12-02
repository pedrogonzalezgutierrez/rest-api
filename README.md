User API

This is an example of how to build a Rest API using Spring Boot 2.1, JPA 2.2 and Hibernate, Json Web Token.

POST /user
```
Body:
{
	"name": "Pedro",
	"password": "Pedro"
}
```
> 400: User already exists

> 400: Validation Error

> 200: User created