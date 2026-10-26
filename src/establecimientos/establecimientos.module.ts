import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PassportModule } from '@nestjs/passport';

import { Establecimiento } from '../entities/establecimiento.entity';
import { UsuariosModule } from '../usuarios/usuarios.module';
import { GuardiaJwt } from '../usuarios/guardia-jwt';

import { EstablecimientosController } from './establecimientos.controller';
import { EstablecimientosService } from './establecimientos.service';

@Module({
  imports: [
    TypeOrmModule.forFeature([Establecimiento]),
    PassportModule.register({
      defaultStrategy: 'jwt',
    }),
    UsuariosModule,
  ],
  controllers: [EstablecimientosController],
  providers: [
    EstablecimientosService,
    GuardiaJwt,
  ],
  exports: [EstablecimientosService],
})
export class EstablecimientosModule {}