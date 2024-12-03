FROM debian:bullseye
LABEL Author="Lendar"

USER root

# Actualizar el sistema e instalar dependencias
RUN echo "Iniciando actualización del sistema..." && \
    apt-get update && apt-get upgrade -y && \
    apt-get install -y apt-transport-https wget procps openjdk-11-jdk mariadb-server \
    openssh-server vsftpd net-tools nginx vim iputils-ping curl && \
    curl -sL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    groupadd tomcat && \
    useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat && \
    cd /tmp && wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.33/bin/apache-tomcat-10.1.33.tar.gz && \
    tar -zxvf apache-tomcat-10.1.33.tar.gz -C /usr/local && \
    mv /usr/local/apache-tomcat-10.1.33 /usr/local/tomcat && \
    rm -f /tmp/apache-tomcat-10.1.33.tar.gz && \
    apt-get clean && rm -rf /var/lib/apt/lists/* && \
    echo "Dependencias instaladas y sistema actualizado."

# Crear usuarios del sistema con contraseñas
RUN mkdir -p /scripts
COPY ./init_users.sh /scripts/init_users.sh
RUN chmod +x /scripts/init_users.sh && /scripts/init_users.sh

# Hacer directorio Tomcat accesible
RUN chmod -R 755 /usr/local/tomcat && \
    chown -R tomcat:tomcat /usr/local/tomcat

# Configurar SSH (recomendación de seguridad)
RUN echo "PermitRootLogin no" >> /etc/ssh/sshd_config && \
    echo "PasswordAuthentication no" >> /etc/ssh/sshd_config && \
    mkdir -p /run/sshd && \
    echo "Configuración SSH actualizada."

# Crear directorios necesarios
RUN mkdir -p /home/estelcon/{activity,error,log,outbox,plantillas,sent} && \
    chown -R tomcat:tomcat /home/estelcon

# Copiar el archivo web.xml modificado
COPY files/web.xml /usr/local/tomcat/conf/web.xml

# Exponer los puertos necesarios
EXPOSE 8080 80 8443 2121 2122 22 40000 3306

# Comando de inicio con registro
CMD ["/usr/local/bin/start-services.sh"]