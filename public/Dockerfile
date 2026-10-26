# 1. Imagen base oficial de Node.js
FROM node:18-alpine

# 2. Directorio de trabajo dentro del contenedor
WORKDIR /app

# 3. Copiar archivos de dependencias
COPY package*.json ./

# 4. Instalar dependencias
RUN npm install

# 5. Copiar el resto del código del proyecto
COPY . .

# 6. Compilar la aplicación NestJS
RUN npm run build

# 7. Exponer el puerto en el que corre la app
EXPOSE 3000

# 8. Comando para iniciar la aplicación
CMD ["npm", "run", "start:prod"]