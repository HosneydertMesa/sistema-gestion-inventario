# Usa una imagen base de Java 17 ligera
FROM eclipse-temurin:17-jre-focal

# Argumento para pasar el nombre del archivo JAR desde Docker Compose
ARG JAR_FILE=target/*.jar

# Copia el archivo JAR construido por Maven al contenedor
COPY ${JAR_FILE} app.jar

# Expone el puerto en el que correrá la aplicación dentro del contenedor
# (configurar en docker-compose.yml)

# Comando para ejecutar la aplicación cuando el contenedor inicie
ENTRYPOINT ["java","-jar","/app.jar"]