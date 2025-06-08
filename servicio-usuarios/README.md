# Servicio de Usuarios API

API REST para la gestión de usuarios y teléfonos con autenticación JWT.

## Requisitos previos

- Java 17 o superior
- Maven 3.8 o superior
- Base de datos H2 en modo servidor

## Configuración inicial

1. Inicia el servidor H2 (si no está en ejecución):
   ```
   java -jar h2*.jar
   ```

2. Clona el repositorio:
   ```
   git clone <url-del-repositorio>
   cd servicio-usuarios
   ```

3. Compila el proyecto:
   ```
   mvn clean install
   ```

4. Ejecuta la aplicación:
   ```
   mvn spring-boot:run
   ```

La aplicación se ejecutará en: http://localhost:8084

## Documentación de la API

La documentación de la API está disponible a través de Swagger UI:
```
http://localhost:8084/swagger-ui.html
```

## Probar los endpoints

### 1. Crear un usuario (No requiere autenticación)

**Método**: POST  
**URL**: http://localhost:8084/api/usuarios  
**Headers**:  
- Content-Type: application/json  

**Body**:
```json
{
  "nombre": "Juan Pérez",
  "correo": "juan@ejemplo.com",
  "contrasena": "Password123",
  "telefonos": [
    {
      "numero": "123456789",
      "codigoCiudad": "1",
      "codigoPais": "+57"
    }
  ]
}
```

**Respuesta esperada**: Status 201 (Created) con los datos del usuario incluyendo el token JWT.

### 2. Obtener un usuario por ID (Requiere autenticación)

**Método**: GET  
**URL**: http://localhost:8084/api/usuarios/{id}  
**Headers**:  
- Authorization: Bearer {token}

### 3. Actualizar un usuario (Requiere autenticación)

**Método**: PUT  
**URL**: http://localhost:8084/api/usuarios/{id}  
**Headers**:  
- Content-Type: application/json  
- Authorization: Bearer {token}

**Body**:
```json
{
  "nombre": "Juan Pérez Actualizado",
  "correo": "juan_nuevo@ejemplo.com",
  "contrasena": "NuevaPassword123",
  "telefonos": [
    {
      "numero": "987654321",
      "codigoCiudad": "1",
      "codigoPais": "+57"
    }
  ]
}
```

### 4. Actualizar parcialmente un usuario (Requiere autenticación)

**Método**: PATCH  
**URL**: http://localhost:8084/api/usuarios/{id}  
**Headers**:  
- Content-Type: application/json  
- Authorization: Bearer {token}

**Body** (solo incluye los campos a actualizar):
```json
{
  "nombre": "Nuevo Nombre"
}
```

### 5. Eliminar un usuario (Requiere autenticación)

**Método**: DELETE  
**URL**: http://localhost:8084/api/usuarios/{id}  
**Headers**:  
- Authorization: Bearer {token}

## Consejos para probar con Postman

1. **Crear una colección**: Crea una colección "Servicio Usuarios" para organizar todas las pruebas.

2. **Configurar variables de entorno**:
   - Crea una variable `base_url` con valor `http://localhost:8084`
   - Crea una variable `token` que se actualizará automáticamente

3. **Configurar test para capturar el token**:
   Para el endpoint de creación de usuario, agrega este script en la pestaña "Tests":
   ```javascript
   var jsonData = pm.response.json();
   pm.environment.set("token", jsonData.token);
   pm.environment.set("user_id", jsonData.id);
   ```

4. **Usar la variable token**:
   En el resto de endpoints, usa `{{token}}` en el header Authorization:
   ```
   Authorization: Bearer {{token}}
   ```

## Validaciones

- **Correo**: Debe tener un formato válido (ejemplo@dominio.com)
- **Contraseña**: Mínimo 8 caracteres, debe contener letras y números
- **Teléfono**: Los campos número, códigoCiudad y códigoPais son requeridos

## Solución de problemas

- **403 Forbidden**: Verifica que estés enviando el token JWT correctamente en el header Authorization
- **400 Bad Request**: Revisa la validación de campos (formato de correo, contraseña, etc.)
- **404 Not Found**: El recurso solicitado no existe

## Notas importantes

- Los tokens JWT son válidos por 24 horas
- El token se genera y actualiza al crear o actualizar un usuario
- El endpoint `/test` está disponible para verificar si el servicio está funcionando
