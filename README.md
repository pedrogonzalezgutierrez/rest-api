# User API

Rest API what allows you to create users and roles and manage them. In order to talk with UserAPI consumers first need to get a JSON Web Token supplying its credentials. Then you need to provide that token in every request in order authenticate.
 
##### Technologies
- Gradle
- Spring Boot 2.1,
- JPA 2.2 and Hibernate 5.3.7 with Hikari pool running in PostgreSQL 9.5 
- Mapstruct 1.2
- Spock Framework 1.2

##### Pacts
**1) Create user:** This endpoint is public and everyone can hit it in order to add a new user to the database 
```
POST /user
Body:
{
	"name": "Pedro",
	"email": "pedro@myemail.com"
	"password": "Betis"
}
```
> 400: User already exists

> 400: Validation Error

> 200: User created

**2) Obtain a JSON Web Token:** Get a token valid for 1 hour providing username and password/secret
```
POST /user/jwt
Body:
{
	"name": "Pedro",
	"password": "Pedro"
}
```
> 400: User does not exist

> 400: Validation Error

> 200: 
```
Response Body:
{
	"jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
}
```