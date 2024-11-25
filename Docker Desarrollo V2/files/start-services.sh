#!/bin/bash

# Arrancar servicios
service ssh start
service vsftpd start
service nginx start

# Arrancar MariaDB
sh /etc/init.d/mariadb.sh

# Arrancar Tomcat
sh /usr/local/tomcat/bin/catalina.sh start

# Mostrar logs de Tomcat
tail -f /usr/local/tomcat/logs/catalina.out
