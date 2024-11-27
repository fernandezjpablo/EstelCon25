#!/bin/bash

echo "Iniciando servicios..."

# Iniciar MariaDB
service mysql start

# Iniciar SSH
service ssh start

# Iniciar vsftpd
service vsftpd start

# Iniciar Nginx
service nginx start

# Iniciar Tomcat
/usr/local/tomcat/bin/catalina.sh start

echo "Servicios iniciados. Contenedor listo."

# Mantener el contenedor en ejecución
tail -f /dev/null