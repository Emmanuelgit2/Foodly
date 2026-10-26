import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigModule } from '@nestjs/config';
import { AuthModule } from './auth/auth.module';
import { RestaurantesModule } from './restaurantes/restaurantes.module';

@Module({
  imports: [
    // Configuración global de variables de entorno (.env)
    ConfigModule.forRoot({ 
      isGlobal: true 
    }),
    
    // Conexión a la base de datos NoSQL MongoDB (docker-compose o local)
    MongooseModule.forRoot(
      process.env.MONGO_URI || 'mongodb://localhost:27017/foodlydb'
    ),
    
    // Módulos de la aplicación
    AuthModule,
    RestaurantesModule,
  ],
  controllers: [],
  providers: [],
})
export class AppModule {}