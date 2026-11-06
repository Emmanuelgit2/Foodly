import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { JwtModule } from '@nestjs/jwt';
import { TypeOrmModule } from '@nestjs/typeorm';

import { AuthService } from './auth.service';
import { AuthController } from './auth.controller';
import { Calificacion } from '../entities/calificacion.entity';
import { Usuario } from '../entities/usuario.entity';
import { TurnstileService } from '../usuarios/turnstile.service';

@Module({
  imports: [
    ConfigModule,
    TypeOrmModule.forFeature([Usuario, Calificacion]),
    JwtModule.register({
      secret: process.env.JWT_SECRET,
      signOptions: {
        expiresIn: '1h',
      },
    }),
  ],
  controllers: [AuthController],
  providers: [AuthService, TurnstileService],
  exports: [AuthService],
})
export class AuthModule {}