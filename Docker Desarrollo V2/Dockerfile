FROM debian:bullseye
LABEL Author="Lendar"

USER root

# Paso 1: Actualizar el sistema y registrar el progreso
RUN echo "Iniciando actualización del sistema..." && \
    apt-get update && apt-get upgrade -y && \
    echo "Actualización del sistema completada."

# Paso 2: Instalar dependencias principales
RUN echo "Instalando dependencias principales..." && \
    apt-get install -y apt-transport-https wget procps openjdk-11-jdk mariadb-server=1:10.5.* \
    openssh-server vsftpd net-tools systemctl nginx vim && \
    echo "Dependencias principales instaladas correctamente."

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
    rm -rf /tmp/* && \
    echo "Apache Tomcat instalado correctamente."

# Paso 5: Crear usuarios del sistema con contraseñas
COPY init_users.sh /init_users.sh
RUN chmod +x /init_users.sh && /init_users.sh

# Paso 6: Permitir acceso SSH para root
RUN echo "PermitRootLogin yes" >> /etc/ssh/sshd_config && \
    echo "PasswordAuthentication yes" >> /etc/ssh/sshd_config && \
    systemctl restart sshd && \
    echo "Configuración SSH actualizada."

# Paso 7: Verificar la creación de usuarios
RUN cut -d: -f1 /etc/passwd

# Paso 8: Copiar y configurar archivos personalizados
COPY files/start-services.sh /usr/local/bin/start-services.sh
RUN echo "Copiando y configurando scripts personalizados..." && \
    chmod +x /usr/local/bin/start-services.sh && \
    echo "Scripts personalizados configurados correctamente."

# Paso 9: Crear directorios necesarios
RUN echo "Creando directorios requeridos por la aplicación..." && \
    mkdir -p /home/estelcon/{outbox,sent,error,plantillas,log,activity} && \
    echo "Directorios creados."

# Exponer los puertos necesarios
EXPOSE 8080 80 8443 2121 2122 22 40000 3306

# Comando de inicio con registro
CMD echo "Iniciando servicios..." && /usr/local/bin/start-services.sh