# eminenceinnovation-task

# Please refer postman collection for the API's information.

---

- API endpoints
  - ```/register``` will be used to register a new user or admin based on the role passed and can be accessed without authentication.
  - ```/authenticate``` will be used to authenticate the user or admin, on successful authentication a JWT token will be received in response, that has role set and can be accessed without any authentication.
  - ```/decode-jwt``` will show the few contents of jwt like username, issuedAt and expirationTime. This endpoint requires authentication and  will be only accessible to the user with admin role.
  - ```/draw-matches``` This endpoint requires authentication and will be accessible to both user and admin. This endpoint will list all the matches that are drawn for the year passed as query param.