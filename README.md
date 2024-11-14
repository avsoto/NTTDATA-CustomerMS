# CustomerMS - Microservicio de Gestión de Clientes

Este repositorio contiene el microservicio **CustomerMS** que gestiona la creación, lectura, actualización y eliminación (CRUD) de los clientes en el sistema bancario del Banco XYZ. Este microservicio forma parte del proyecto **Proyecto II - NTTDATA Bootcamp Tech Girls Power**, y está construido con **Spring Boot** y **JPA/Hibernate**, y se conecta a una base de datos relacional (MySQL) para la persistencia de los datos.

## Requisitos del Sistema

- **Java 8+** o **Java 11+**
- **Spring Boot 2.x**
- **MySQL** para la base de datos
- **Postman** o cualquier otra herramienta para probar los endpoints de la API

## Funcionalidades del Microservicio

El microservicio **CustomerMS** proporciona los siguientes endpoints para gestionar la información de los clientes:

### 1. Crear Cliente
- **Método**: `POST`
- **Endpoint**: `/clientes`
- **Descripción**: Crea un nuevo cliente en el sistema.
- **Body de solicitud** (JSON):
  ```json
  {
    "firstName": "John Carlos",
    "lastName": "Doe Windsor",
    "email": "john@gmail.com",
    "dni": "55289600"
}

### 2. Listar Clientes
- **Método**: `GET`
- **Endpoint**: `/clientes`
- **Descripción**: Obtiene una lista de todos los clientes del sistema.
- **Respuesta** (200 OK):
  ```json
  [
    {
        "id": 1,
        "firstName": "Jane",
        "lastName": "Doe Sullivan",
        "dni": "12345678",
        "email": "janedoe@example.com"
    },
    {
        "id": 2,
        "firstName": "Pedro Andrew",
        "lastName": "Salas Sullivan",
        "dni": "23456789",
        "email": "pedro@example.com"
    }
  ]
  ```

### 3. Obtener Cliente por ID
- **Método**: `GET`
- **Endpoint**: `/clientes/{id}`
- **Descripción**: Obtiene los detalles de un cliente específico.
- **Parámetros de ruta**: `id` - Identificador único del cliente.
- **Respuesta exitosa** (200 OK):
  ```json
  {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "dni": "12345678",
    "email": "juan.perez@example.com"
  }
  ```

### 4. Actualizar Cliente
- **Método**: `PUT`
- **Endpoint**: `/clientes/{id}`
- **Descripción**: Actualiza los datos de un cliente específico.
- **Parámetros de ruta**: `id` - Identificador único del cliente.
- **Body de solicitud** (JSON):
  ```json
  {
    "firstName": "Piero Carlos",
    "lastName": "Peña Salamanca",
    "dni": "77568400",
    "email": "juan@example.com"
    }

### 5. Eliminar Cliente
- **Método**: `DELETE`
- **Endpoint**: `/clientes/{id}`
- **Descripción**: Elimina un cliente específico del sistema.
- **Parámetros de ruta**: `id` - Identificador único del cliente.
- **Respuesta exitosa** (200 OK):
  ```json
  {
    "message": "Cliente eliminado exitosamente"
  }
  ```

## Reglas de Negocio
- **Cliente único por DNI**: El sistema asegura que no haya clientes con el mismo DNI.
- **No se permite eliminar un cliente con cuentas activas**: Antes de eliminar a un cliente, se verifica que no tenga cuentas asociadas activas.

## Arquitectura

### Diagrama de Componentes
Se utiliza una arquitectura de **microservicios**, donde **CustomerMS** se comunica con otros microservicios, como **AccountMS**. La base de datos MySQL se encuentra conectada a ambos microservicios para persistir los datos.

### Diagrama de Secuencia
Los diagramas de secuencia detallan el flujo de comunicación entre los microservicios durante las operaciones CRUD de clientes, asegurando que los servicios se mantengan desacoplados y puedan escalar de manera independiente.

## Tecnologías Utilizadas
- **Spring Boot**: Para el desarrollo del microservicio.
- **JPA/Hibernate**: Para la persistencia de los datos.
- **MySQL**: Como base de datos relacional.
- **OpenAPI**: Para la documentación de la API (contract-first).
- **Java 8/11**: Se aplica programación funcional y orientada a objetos.

## Instalación y Ejecución

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/customer-ms.git
cd customer-ms
```

### 2. Configurar la Base de Datos

Asegúrate de tener MySQL instalado y crear una base de datos para el proyecto:

```sql
CREATE DATABASE customerms;
```

### 3. Configuración de `application.properties`

En el archivo `src/main/resources/application.properties`, configura las credenciales de tu base de datos:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/customerms
spring.datasource.username=usuario
spring.datasource.password=contraseña
spring.jpa.hibernate.ddl-auto=update
```

### 4. Ejecutar el Microservicio

Para ejecutar el microservicio, usa el siguiente comando:

```bash
mvn spring-boot:run
```

### 5. Probar los Endpoints

Usa **Postman** o una herramienta similar para probar los endpoints documentados.
