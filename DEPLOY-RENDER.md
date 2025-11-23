# Guía de Despliegue en Render

## Pasos para desplegar en Render

### 1. Preparar Base de Datos MySQL en Render

1. Ve a [Render Dashboard](https://dashboard.render.com)
2. Haz clic en "New +" y selecciona "PostgreSQL" (Render no ofrece MySQL gratis, usa PostgreSQL)
3. Configura:
   - **Name**: `tienda-ropa-db`
   - **Database**: `tienda_ropa`
   - **User**: `tienda_user`
   - **Region**: Oregon (US West) - más barato
   - **Plan**: Free
4. Haz clic en "Create Database"
5. **Guarda las credenciales** que aparecen:
   - Internal Database URL
   - External Database URL
   - Username
   - Password

### 2. Actualizar pom.xml para PostgreSQL

Agrega esta dependencia a tu `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 3. Crear Web Service en Render

1. En Render Dashboard, haz clic en "New +" → "Web Service"
2. Conecta tu repositorio GitHub: `TiendaColecciones`
3. Configura el servicio:
   - **Name**: `tienda-colecciones`
   - **Region**: Oregon (US West)
   - **Branch**: `main`
   - **Root Directory**: (déjalo vacío)
   - **Runtime**: Java
   - **Build Command**: `./render-build.sh`
   - **Start Command**: `./start.sh`

### 4. Configurar Variables de Entorno

En la sección "Environment Variables" del Web Service:
```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=postgresql://[host]:[port]/[database]?user=[username]&password=[password]
DB_USERNAME=[username de la base de datos]
DB_PASSWORD=[password de la base de datos]
PORT=8080
```

### 5. Comandos para subir cambios

```bash
git add .
git commit -m "Add Render deployment configuration"
git push origin main
```

### 6. Verificar Despliegue

- Render construirá y desplegará automáticamente
- La URL será: `https://tienda-colecciones.onrender.com`
- Revisa los logs en caso de errores

## Notas Importantes

- **Render Free Plan**: La app se "duerme" después de 15 minutos de inactividad
- **Primera carga**: Puede tardar 30-60 segundos en despertar
- **Base de datos**: PostgreSQL es gratuito en Render, MySQL requiere plan pago
- **Tiempo de build**: Puede tomar 5-10 minutos la primera vez

## Troubleshooting

Si hay errores:
1. Revisa los logs en Render Dashboard
2. Verifica que las variables de entorno estén correctas
3. Asegúrate de que el puerto sea 8080 (no 8090)
4. Confirma que la base de datos esté ejecutándose