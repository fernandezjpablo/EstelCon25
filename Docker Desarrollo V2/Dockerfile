FROM debian:bullseye
LABEL Author="Lendar"

USER root

# Paso 1: Actualizar el sistema
RUN echo "Iniciando actualización del sistema..." && \
    apt-get update && apt-get upgrade -y && \
    echo "Actualización del sistema completada."

# Paso 2: Instalar dependencias principales
RUN echo "Instalando dependencias principales..." && \
    apt-get install -y apt-transport-https wget procps openjdk-11-jdk mariadb-server \
    openssh-server vsftpd net-tools nginx vim && \
    echo "Dependencias principales instaladas correctamente."

RUN mkdir -p /run/mysqld && chown mysql:mysql /run/mysqld

# Paso 3: Configurar usuario y grupo para Tomcat
RUN echo "Creando grupo y usuario Tomcat..." && \
    groupadd tomcat && \
    useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat && \
    echo "Grupo y usuario Tomcat configurados."

# Paso 4: Descargar e instalar Tomcat
RUN echo "Descargando Apache Tomcat..." && \
    cd /tmp && wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.33/bin/apache-tomcat-10.1.33.tar.gz && \
    tar -zxvf apache-tomcat-10.1.33.tar.gz -C /usr/local && \
    mv /usr/local/apache-tomcat-10.1.33 /usr/local/tomcat && \
    rm -f /tmp/apache-tomcat-10.1.33.tar.gz && \
    echo "Apache Tomcat instalado correctamente."

# Paso 5: Crear usuarios del sistema con contraseñas
RUN mkdir -p /scripts
COPY ./init_users.sh /scripts/init_users.sh
RUN chmod +x /scripts/init_users.sh && /scripts/init_users.sh

# Hacer el directorio de Tomcat accesible para todos los usuarios
RUN chmod -R 777 /usr/local/tomcat && \
    chown -R adminusr:adminusr /usr/local/tomcat

# Paso 6: Configurar SSH
RUN echo "PermitRootLogin yes" >> /etc/ssh/sshd_config && \
    echo "PasswordAuthentication yes" >> /etc/ssh/sshd_config && \
    mkdir -p /run/sshd && \
    echo "Configuración SSH actualizada."
RUN apt-get update && apt-get install -y iputils-ping
# Paso 7: Copiar y configurar scripts personalizados
COPY files/start-services.sh /usr/local/bin/start-services.sh
RUN echo "Copiando y configurando scripts personalizados..." && \
    chmod +x /usr/local/bin/start-services.sh && \
    echo "Scripts personalizados configurados correctamente."

# Paso 8: Crear directorios necesarios
RUN echo "Creando directorios requeridos por la aplicación..." && \
    mkdir -p /home/estelcon/{outbox,sent,error,plantillas,log,activity} && \
    echo "Directorios creados."

# Exponer los puertos necesarios
EXPOSE 8080 80 8443 2121 2122 22 40000 3306

# Comando de inicio con registro
CMD ["/usr/local/bin/start-services.sh"]