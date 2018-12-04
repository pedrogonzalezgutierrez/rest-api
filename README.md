# User API

Rest API allows you to create users and roles and manage them. Anyone can create users and get a JSON Web token which will need to provide in next requests in other to authenticate. Only admin user (a user with the role ROLE_ADMIN) can manage roles and assign existing ones to other users.
 
##### Technologies
- Gradle
- Spring Boot 2.1,
- JPA 2.2 and Hibernate 5.3.7 with Hikari pool running in PostgreSQL 9.5 
- Mapstruct 1.2
- Spock Framework 1.2 (H2 in memory database for Integration Test)

##### Pacts
**1) Create user:** This endpoint is public and everyone can hit it in order to add a new user to the database 
```
POST /user
Body:
{
	"name": "Pedro",
	"email": "pedro@universidad.com",
	"password": "Betis"
}
```
> 400: User already exists

> 400: Validation Error

> 200: User created

**2) Obtain a JSON Web Token:** Get a token valid for 1 hour providing email and password/secret
```
POST /user/jwt
Body:
{
	"email": "pedro@universidad.com",
	"password": "Betis"
}
```
> 400: User does not exist or user is not enabled

> 400: Validation Error

> 200: Token created
```
Response:
Header Authorization: Bearer jwtToken
```

**3) Update password:** Allows the user to change its password 
```
PATCH /user/password
Body:
{
	"email": "pedro@universidad.com",
	"password": "Betis",
	"newPassword": "Heliopolis"
}
```
> 400: Invalid credentials or user is not enabled

> 400: Validation Error

> 200: Password updated

**4) Enable or Disable user:** Only admin user (any user with the role *ROLE_ADMIN*) can hit this endpoint in order to enable or disable the account of any user 
```
ROLE_ADMIN

PATCH /user/enable
Header Authorization: Bearer jwtToken
Body:
{
	"email": "pedro@universidad.com",
	"enable": false
}
```
> 400: User does not exist

> 400: Validation Error

> 200: User enabled updated

**5) Create role:** Only admin user (any user with the role *ROLE_ADMIN*) can hit this endpoint in order to create a new role 
```
ROLE_ADMIN

POST /role
Header Authorization: Bearer jwtToken
Body:
{
	"name": "ROLE_EDITOR"
}
```
> 400: Role already exists

> 400: Validation Error

> 200: Role created
