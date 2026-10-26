
import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { JwtModule } from '@nestjs/jwt';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { PassportModule } from '@nestjs/passport';

import { Usuario } from '../entities/usuario.entity';
import { UsuariosController } from './usuarios.controller';
import { UsuariosService } from './usuarios.service';
import { TurnstileService } from './turnstile.service';
import { EstrategiaJwt } from './estrategia-jwt';
import { GuardiaJwt } from './guardia-jwt';

@Module({
  imports: [
    TypeOrmModule.forFeature([Usuario]),
    ConfigModule,
    PassportModule.register({ defaultStrategy: 'jwt' }),
    JwtModule.registerAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => {
        const secret = configService.get<string>('JWT_SECRET');

        if (!secret) {
          throw new Error(
            'Falta configurar JWT_SECRET en el archivo .env',
          );
        }

        return {
          secret,
          signOptions: {
            expiresIn: '1h',
          },
        };
      },
    }),
  ],
  controllers: [UsuariosController],
  providers: [
    UsuariosService,
    TurnstileService,
    EstrategiaJwt,
    GuardiaJwt,
  ],
  exports: [GuardiaJwt],
})
export class UsuariosModule {}
