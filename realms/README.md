# How to setup a new Keycloak instance
*summary of [this Tutorial](https://medium.com/@nishada/keycloak-spring-boot-rbac-e8732a91909a)*

1. Create Realm
2. Ressource file: `sb-backend.json` (may take a minute or two)
3. Users &rarr; Add User &rarr; **fill all fields** (Username, Email, First name, Last name) otherwise there will be an error &rarr; Create
4. Users &rarr; Click on User &rarr; Credentials &rarr; Set password &rarr; **NOT TEMPORARY**
5. Users &rarr; Click on User &rarr; Role mapping &rarr; Add Admin / Student role

# How to request a JWT Token using Postman
1. Create Post Request to `http://localhost:7070/realms/study-buddies/protocol/openid-connect/token`
2. Set `Body` &rarr; `x-www-form-urlencoded`:

|Key|Value|
|:-:|:-:|
|client_id|sb-backend|
|grant_type|password|
|username|User created above|
|password|Password of user created above|

3. *Optional:* View content of `access_token` [here](https://jwt.io/).

# How to use a restricted endpoint using Postman
1. Create Request
2. Authorization &rarr; Auth Type: Bearer Token &rarr; paste full content of `access_token` from JWT request above into `Token` field.

