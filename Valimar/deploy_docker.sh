#!/bin/bash

# Ejecutar Maven clean install
mvn clean install

# Copiar el contenido del directorio target al contenedor Docker
docker cp ./target/Valimar.war. estelcon-app:/usr/local/tomcat/webapps/Valimar.war

# Reiniciar el contenedor Docker
docker restart estelcon-app