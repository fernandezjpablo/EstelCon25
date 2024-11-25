#!/bin/bash

set -e

echo "Creando usuarios y configurando contraseñas..."

create_user() {
  local user=$1
  local pass=$2
  if ! id -u $user &>/dev/null; then
    useradd $user
    echo "$user:$pass" | chpasswd
    echo "Usuario $user creado."
  else
    echo "Usuario $user ya existe, omitiendo."
  fi
}

create_user ftpestelcon "P4ssw0rd"
create_user estelcon "P4ssw0rd"
create_user tomcat "T0mc4tP4ssw0rd"
create_user adminusr "R00tP4ssw0rd"
create_user root "R00tP4ssw0rd"

echo "ftpestelcon" >> /etc/vsftpd.userlist
chmod 644 /etc/passwd && chmod 600 /etc/shadow

echo "Usuarios creados correctamente."
