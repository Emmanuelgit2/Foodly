
import {
  Body,
  Controller,
  Get,
  Post,
  HttpCode,
  UseGuards,
} from '@nestjs/common';

import { UsuariosService } from './usuarios.service';
import { TurnstileService } from './turnstile.service';
import { GuardiaJwt } from './guardia-jwt';

interface DatosRegistro {
  nombre: string;
  apellido: string;
  cedula: string;
  correo: string;
  contrasena: string;
  turnstileToken: string;
}

interface DatosInicioSesion {
  correo: string;
  contrasena: string;
  turnstileToken: string;
}

@Controller('usuarios')
export class UsuariosController {
  constructor(
    private readonly usuariosService: UsuariosService,
    private readonly turnstileService: TurnstileService,
  ) {}

  @UseGuards(GuardiaJwt)
  @Get()
  obtenerTodos() {
    return this.usuariosService.obtenerTodos();
  }

  @Post()
  async crearUsuario(@Body() datos: DatosRegistro) {
    await this.turnstileService.validarToken(
      datos.turnstileToken,
    );

    const { turnstileToken, ...datosUsuario } = datos;

    return this.usuariosService.crearUsuario(datosUsuario);
  }

  @HttpCode(200)
  @Post('login')
  async iniciarSesion(@Body() datos: DatosInicioSesion) {
    await this.turnstileService.validarToken(
      datos.turnstileToken,
    );

    const { turnstileToken, ...datosLogin } = datos;

    return this.usuariosService.iniciarSesion(datosLogin);
  }
}
