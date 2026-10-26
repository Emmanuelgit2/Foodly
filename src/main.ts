
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { join } from 'path';
import { NestExpressApplication } from '@nestjs/platform-express';

async function bootstrap() {
  const app = await NestFactory.create<NestExpressApplication>(AppModule);

  // Permitir peticiones desde el frontend de Foodly.
  app.enableCors({
    origin: 'http://127.0.0.1:5500',
    methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'],
    allowedHeaders: ['Content-Type', 'Authorization'],
  });

  // Servir archivos estáticos desde la carpeta public.
  app.useStaticAssets(join(__dirname, '..', 'public'));

  // Mantener el puerto actual de Foodly.
  await app.listen(process.env.PORT ?? 3000);
}

bootstrap();
