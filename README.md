# 📦 Sistema de Gestión de Productos e Inventario

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen)
![React](https://img.shields.io/badge/React-18-blueviolet)
![Docker](https://img.shields.io/badge/Docker-black?logo=docker&logoColor=blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-white?logo=postgresql&logoColor=blue)

Este proyecto implementa un sistema de gestión de productos e inventario basado en una **arquitectura de microservicios**. La interfaz de usuario es una aplicación moderna construida en **React**. Todo el sistema está contenedorizado con **Docker** y orquestado con **Docker Compose** para un despliegue y desarrollo simplificado.

## 📋 Índice

* [Arquitectura](#-arquitectura)
* [Tecnologías Utilizadas](#-tecnologías-utilizadas)
* [Funcionalidades Detalladas](#-funcionalidades-detalladas)
* [Requisitos Previos](#-requisitos-previos)
* [Estructura del Proyecto](#-estructura-del-proyecto)
* [Instalación y Ejecución](#-instalación-y-ejecución)
* [Uso y Documentación](#-uso-y-documentación)
* [Pruebas (Testing)](#-pruebas-testing)

---

## 🏛️ Arquitectura

El sistema se compone de los siguientes servicios:

* **`ms-productos`**: Microservicio en Java (Spring Boot) que gestiona el ciclo de vida de los productos (CRUD). Requiere una API Key para su acceso.
* **`ms-inventario`**: Microservicio en Java (Spring Boot) que gestiona el stock de los productos. Se comunica internamente con `ms-productos` para obtener detalles.
* **`frontend-ui`**: Interfaz de usuario en React que consume los endpoints de ambos microservicios.
* **`db`**: Base de datos PostgreSQL utilizada por ambos microservicios.

> _**Consideración de diseño:** La comunicación entre servicios se realiza mediante peticiones HTTP RESTful con formato JSON. Para mejorar la robustez, se recomienda implementar patrones de resiliencia como **reintentos y timeouts** en la comunicación entre `ms-inventario` y `ms-productos`._

---

## ✨ Tecnologías Utilizadas

| Área              | Tecnología                                                                                          |
| ----------------- | --------------------------------------------------------------------------------------------------- |
| **Backend** | ☕ Java 17, 🌱 Spring Boot 3, Spring Security, Spring Data JPA, Lombok, Maven                        |
| **Frontend** | ⚛️ React, JavaScript (ES6+), CSS                                                                    |
| **Base de Datos** | 🐘 PostgreSQL                                                                                       |
| **Pruebas** | 🧪 JUnit 5, Mockito, Testcontainers, WireMock                                                       |
| **DevOps** | 🐳 Docker, Docker Compose                                                                           |
| **Servidor Web** | 🌐 Nginx (para servir el frontend)                                                                  |

---

## 🚀 Funcionalidades Detalladas

### Microservicio 1: Productos (`ms-productos`)

Gestiona toda la información relativa a los productos.

* `POST /api/v1/products`: Crea un nuevo producto.
* `GET /api/v1/products/{id}`: Obtiene un producto por su ID.
* `PUT /api/v1/products/{id}`: Actualiza un producto existente.
* `DELETE /api/v1/products/{id}`: Elimina un producto.
* `GET /api/v1/products`: Lista todos los productos (con paginación, ej: `?page=0&size=10`).

### Microservicio 2: Inventario (`ms-inventario`)

Gestiona el stock y se comunica con el servicio de productos.

* `GET /api/v1/inventory/{productId}`: Consulta el stock, enriqueciendo la respuesta con datos de `ms-productos`.
* `PATCH /api/v1/inventory/{productId}/stock`: Actualiza la cantidad de stock (acepta valores positivos y negativos).

---

## ✅ Requisitos Previos

Asegúrate de tener instalado el siguiente software en tu máquina:

* 🐳 Docker & Docker Compose
* ☕ Java 17 o superior
* 📦 Apache Maven 3.3.1 o superior
* 📜 Node.js 18 o superior

---

## 📁 Estructura del Proyecto

```bash
/
├── msProductos/         # Microservicio de Productos (Spring Boot)
├── msInventario/        # Microservicio de Inventario (Spring Boot)
├── front-inventario/    # Interfaz de Usuario (React)
├── docker-compose.yml   # Archivo de orquestación de Docker
└── README.md
```

---

## ⚙️ Instalación y Ejecución

Sigue estos pasos para levantar el sistema completo.

### 1. Construir los Microservicios (Backend)

Antes de levantar los contenedores, compila los proyectos de Java para crear los archivos `.jar`.

```bash
# Navega al directorio del microservicio de productos y constrúyelo
cd msProductos
mvn clean install

# Vuelve a la raíz y haz lo mismo para el microservicio de inventario
cd ../msInventario
mvn clean install

# Regresa a la raíz del proyecto
cd ..
```

### 2. Levantar Todo el Sistema con Docker Compose

Una vez que los archivos `.jar` han sido creados, levanta toda la infraestructura con un solo comando desde la raíz del proyecto:

```bash
docker-compose up --build
```

> El flag `--build` reconstruye las imágenes de los servicios. La primera vez puede tardar unos minutos mientras se descargan las imágenes base.

Una vez que todos los contenedores se inicien, el sistema estará listo.

---

## 💻 Uso y Documentación

### 🖥️ Interfaz de Usuario (Frontend)

Accede a la aplicación web abriendo tu navegador en:
**[http://localhost:93](http://localhost:93)**

### 📖 Documentación de la API (Swagger)

Cada microservicio expone su propia documentación de API interactiva a través de Swagger UI.

* **`ms-productos`**: **[http://localhost:8091/swagger-ui.html](http://localhost:8091/swagger-ui.html)**
* **`ms-inventario`**: **[http://localhost:8092/swagger-ui.html](http://localhost:8092/swagger-ui.html)**

### 🔑 Acceso Directo a las APIs (Backend)

Puedes interactuar directamente con las APIs usando herramientas como Postman o cURL.

* **Microservicio de Productos**: `http://localhost:8091/api/v1/products`
    > **Importante:** Todas las peticiones a este servicio deben incluir la cabecera `X-API-KEY` con el valor `mi-api-key-secreta`.

* **Microservicio de Inventario**: `http://localhost:8092/api/v1/inventory`
    > Este servicio no requiere autenticación.

---

## 🧪 Pruebas (Testing)

El proyecto incluye una estrategia de pruebas para garantizar la calidad del código.

* **Pruebas Unitarias**: Enfocadas en componentes individuales en aislamiento.
* **Pruebas de Integración**: Se incluye al menos una por microservicio (`*IntegrationTest.java`). Para las dependencias, se utiliza:
    * **Testcontainers**: Para levantar una instancia real de PostgreSQL.
    * **WireMock**: Para simular la comunicación HTTP entre `ms-inventario` y `ms-productos`.

Para ejecutar todas las pruebas, utiliza el comando de Maven:

```bash
mvn clean install
