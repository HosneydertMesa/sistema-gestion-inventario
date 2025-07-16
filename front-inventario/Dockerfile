# ---- Etapa 1: Build ----
# Usamos una imagen oficial de Node.js como base.
FROM node:20-alpine AS builder

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos de definición de dependencias
# ¡CORRECCIÓN! Ahora copiamos package-lock.json
COPY package.json package-lock.json ./

# ¡CORRECCIÓN! Usamos 'npm ci' que es la mejor práctica para instalaciones
# limpias y reproducibles en Docker, usando el lockfile.
RUN npm ci

# Copiamos el resto del código fuente de la aplicación
COPY . .

# ¡CORRECCIÓN! Construimos la aplicación para producción con npm.
RUN npm run build

# ---- Etapa 2: Serve ----
# Usamos una imagen de Nginx súper ligera para servir el contenido estático
FROM nginx:1.27-alpine

# Copiamos los archivos construidos de la etapa 'builder' al directorio web de Nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# Copiamos la configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponemos el puerto 80, que es el puerto por defecto de Nginx
EXPOSE 80
