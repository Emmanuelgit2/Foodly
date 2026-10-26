
import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { TypeOrmModule } from '@nestjs/typeorm';

import { AppController } from './app.controller';
import { AppService } from './app.service';

import { Usuario } from './entities/usuario.entity';
import { Establecimiento } from './entities/establecimiento.entity';
import { Categoria } from './entities/categoria.entity';
import { Producto } from './entities/producto.entity';
import { Calificacion } from './entities/calificacion.entity';
import { UsuariosModule } from './usuarios/usuarios.module';
import { ProductosModule } from './productos/productos.module';
import { EstablecimientosModule } from './establecimientos/establecimientos.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env',
    }),

    TypeOrmModule.forRootAsync({
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => ({
        type: 'mysql' as const,
        host: configService.get<string>('DB_HOST', 'localhost'),
        port: Number(configService.get<string>('DB_PORT', '3306')),
        username: configService.get<string>('DB_USERNAME'),
        password: configService.get<string>('DB_PASSWORD'),
        database: configService.get<string>('DB_DATABASE', 'saborapp'),
        entities: [
          Usuario,
          Establecimiento,
          Categoria,
          Producto,
          Calificacion,
        ],
        synchronize: false,
      }),
    }),

    UsuariosModule,
    ProductosModule,
    EstablecimientosModule,
  ],

  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
