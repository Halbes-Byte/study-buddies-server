docker compose down
docker rm -f study-buddies-backend
docker compose up -d
sleep 30
docker run \
  --name study-buddies-backend \
  --network host \
  -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KEYCLOAK_CLIENT_ID=sb-backend \
  -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KEYCLOAK_SCOPE=openid \
  -e SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KEYCLOAK_ISSUER_URI=http://localhost:7070/realms/study-buddies \
  -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://localhost:7070/realms/study-buddies \
  panderu/study-buddies-backend
