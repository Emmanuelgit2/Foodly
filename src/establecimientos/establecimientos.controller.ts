import {
  Body,
  Controller,
  Get,
  Post,
  UseGuards,
} from '@nestjs/common';

import { EstablecimientosService } from './establecimientos.service';
import { GuardiaJwt } from '../usuarios/guardia-jwt';

@Controller('establecimientos')
export class EstablecimientosController {
  constructor(
    private readonly establecimientosService: EstablecimientosService,
  ) {}

  // Consulta pública de establecimientos activos
  @Get()
  obtenerActivos() {
    return this.establecimientosService.obtenerActivos();
  }

  // Crear un establecimiento requiere iniciar sesión
  @UseGuards(GuardiaJwt)
  @Post()
  crear(
    @Body()
    datos: {
      nombre: string;
      descripcion?: string;
      direccion: string;
      telefono?: string;
    },
  ) {
    return this.establecimientosService.crear(datos);
  }
}