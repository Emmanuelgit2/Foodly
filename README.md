Foodly

Foodly es una aplicación móvil desarrollada para facilitar el descubrimiento de propuestas gastronómicas. La aplicación permite a los usuarios conocer diferentes restaurantes, consultar información sobre sus propuestas de comida y explorar lugares según sus preferencias.

Objetivo

El objetivo de Foodly es ofrecer una experiencia sencilla y visual para que las personas puedan descubrir restaurantes y conocer sus principales productos, categorías, ubicación y calificación.

Flujo de la aplicación

El flujo principal de la aplicación es:

Splash - Inicio - Registro - Home - Restaurante

1. Splash

Pantalla inicial de Foodly que muestra el logotipo de la aplicación.

2. Inicio

Presenta Foodly al usuario y permite comenzar a utilizar la aplicación.

3. Registro

El usuario completa sus datos básicos para continuar.

4. Home

Muestra los restaurantes disponibles y algunas de sus características principales.

5. Restaurante

Permite consultar información más detallada del restaurante seleccionado, como:

- Tipo de comida
- Calificación
- Ubicación
- Productos destacados

Diseño

La interfaz de Foodly utiliza un diseño moderno, sencillo y enfocado en la experiencia del usuario.

Paleta de colores

- 🟠 Naranja: color principal de la aplicación.
- ⚪ Blanco: utilizado como fondo y para generar limpieza visual.
- ⚫ Gris oscuro: utilizado principalmente para textos.
- ⚪ Gris claro: utilizado en algunos fondos y elementos secundarios.

Capturas de pantalla

Splash

[Splash](capturas/01_splash.png)

Inicio

[Inicio](capturas/02_inicio.png)

Registro

[Registro](capturas/03_registro.png)

Home

[Home](capturas/04_home.png)

Restaurante

[Restaurante](capturas/05_restaurante.png)

Tecnologías utilizadas

- Android Studio
- Kotlin
- Jetpack Compose
- Material 3
- NestJS
- MySQL

Base de datos

El proyecto general utiliza una base de datos MySQL llamada `saborapp`, que contiene información relacionada con usuarios, establecimientos, categorías, productos, pedidos y otros elementos del sistema.

Backend

El backend del proyecto fue desarrollado utilizando NestJS.

La estructura del proyecto permite trabajar con diferentes módulos relacionados con los usuarios, productos, pedidos y establecimientos.

Contexto académico

Proyecto desarrollado como parte del proceso académico de **Desarrollo de Software**, integrando conocimientos de desarrollo móvil, bases de datos y desarrollo backend.

Proyecto

Foodly — Descubre nuevos sabores cerca de ti.

## Aplicación Android

El proyecto de Android Studio está en la carpeta `mobile/`. Para abrirlo, selecciona esa carpeta desde **File → Open** en Android Studio, espera la sincronización de Gradle y pulsa **Run** con un emulador o teléfono conectado.

Para probar el inicio de sesión, ejecuta el backend NestJS desde la raíz. Copia `.env.example` a `.env` y configura MySQL, `JWT_SECRET`, el Client ID web de Google y las claves de Cloudflare Turnstile. No publiques el archivo `.env` ni compartas `TURNSTILE_SECRET`.

La app usa `10.0.2.2:3000` en el emulador Android para llegar al backend del computador. En un teléfono conectado por USB, usa `adb reverse tcp:3000 tcp:3000`; la app del teléfono usa `127.0.0.1:3000`.
