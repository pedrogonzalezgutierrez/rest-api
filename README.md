# User API

This RestAPI allows you to create users and roles and manage them. Anyone can create users and get a JSON Web token which will need to provide in next requests in other to authenticate. Only admin user (a user with the role ROLE_ADMIN) can manage roles and assign existing ones to other users.
 
### Technologies
- Gradle 4.10.2
- Spring Boot 2.1
- JPA 2.2 and Hibernate 5.3.7 with Hikari pool (running dialect PostgreSQL 9.5) 
- Mapstruct 1.2
- Spock Framework 1.2 (H2 in memory database for Integration Test)
- Owasp Java Html Sanitizer to avoid Cross-site scripting (XSS)

### Pacts
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
> 400: Validation Error

> 400: User already exists

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
> 400: Validation Error

> 400: User does not exist

> 400: User is not enabled

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
> 400: Validation Error

> 400: Invalid credentials

> 400: User is not enabled

> 200: Password updated

**4) Enable or Disable user:** Only admin (any user with the role *ROLE_ADMIN*) can hit this endpoint in order to enable or disable the account of any user 
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
> 400: Validation Error

> 400: User does not exist

> 200: User enabled updated

**5) Add role to User:** Only admin (any user with the role *ROLE_ADMIN*) can hit this endpoint in order to add a role to an existing user 
```
ROLE_ADMIN

POST /user/role
Header Authorization: Bearer jwtToken
Body:
{
	"idUser": "2fb28131-e841-4c2d-9fe6-bc5ddb708704",
	"idRole": "55b7dfbe-c62a-464d-bc35-da2a324077b6"
}
```
> 400: Validation Error

> 400: User does not exist

> 400: Role does not exist

> 200: Role added to user

**6) Remove role from User:** Only admin (any user with the role *ROLE_ADMIN*) can hit this endpoint in order to remove a role from an user 
```
ROLE_ADMIN

DELETE /user/role
Header Authorization: Bearer jwtToken
Body:
{
	"idUser": "2fb28131-e841-4c2d-9fe6-bc5ddb708704",
	"idRole": "55b7dfbe-c62a-464d-bc35-da2a324077b6"
}
```
> 400: Validation Error

> 400: User does not exist

> 400: Role does not exist

> 200: Role removed from user

**7) Create role:** Only admin (any user with the role *ROLE_ADMIN*) can hit this endpoint in order to create a new role 
```
ROLE_ADMIN

POST /role
Header Authorization: Bearer jwtToken
Body:
{
	"name": "ROLE_EDITOR"
}
```
> 400: Validation Error

> 400: Role already exists

> 200: Role created

**8) Delete role:** Only admin (any user with the role *ROLE_ADMIN*) can hit this endpoint in order to delete an existing role 
```
ROLE_ADMIN

DELETE /role
Header Authorization: Bearer jwtToken
Body:
{
	"name": "ROLE_EDITOR"
}
```
> 400: Validation Error

> 400: Role does not exist

> 500: Role is in used by users

> 200: Role deleted

#### Postman Collection
There is a file called ```UserApi.postman_collection.json``` with all these request ready to import in [Postman](https://www.getpostman.com/) in order to play with them.
 
### Gradle tasks
**1) Run Unit test** 
```
./gradlew test
```
**2) Run Integration test** 
```
./gradlew integrationTest
```
**3) Run unit tests, integration tests and build jar** 
```
./gradlew clean build
```
This will create the jar in ```build/libs/user-api-X.Y.Z-SNAPSHOT.jar```

