
import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { TypeOrmModule } from '@nestjs/typeorm';

import { AuthModule } from './auth/auth.module';
import { RestaurantesModule } from './restaurantes/restaurantes.module';

@Module({
  imports: [
    // Variables de entorno del archivo .env
    ConfigModule.forRoot({
      isGlobal: true,
    }),

    // Conexión a MySQL para usuarios y autenticación
    TypeOrmModule.forRoot({
      type: 'mysql',
      host: process.env.DB_HOST || 'localhost',
      port: Number(process.env.DB_PORT) || 3306,
      username: process.env.DB_USERNAME || 'root',
      password: process.env.DB_PASSWORD,
      database: process.env.DB_DATABASE || 'saborapp',
      autoLoadEntities: true,
      synchronize: false,
    }),

    // Conexión a MongoDB para los datos que ya utiliza este módulo
    MongooseModule.forRoot(
      process.env.MONGO_URI ||
        'mongodb://localhost:27017/foodlydb',
    ),

    // Módulos de Foodly
    AuthModule,
    RestaurantesModule,
  ],
  controllers: [],
  providers: [],
})
export class AppModule {}
