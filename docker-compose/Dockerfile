FROM jboss/keycloak:11.0.3
VOLUME /tmp/export

#COPY export/master-realm.json /tmp/export/master-realm.json
#COPY export/master-users-0.json /tmp/export/master-users-0.json

ENV KEYCLOAK_USER=admin \
    KEYCLOAK_PASSWORD=admin \
    DB_VENDOR=postgres \
    DB_ADDR=postgres \
    DB_PORT=5432 \
    DB_DATABASE=postgres \
    DB_USER=keycloak \
    DB_PASSWORD=password

LABEL traefik.enable=true \
      traefik.port=8080 \
      traefik.protocol=http \
      traefik.frontend.rule=Host:localhost \
      traefik.frontend.passHostHeader=true

EXPOSE 8080