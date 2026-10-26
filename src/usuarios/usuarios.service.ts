
import {
  Injectable,
  ConflictException,
  UnauthorizedException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';

import { Usuario } from '../entities/usuario.entity';

@Injectable()
export class UsuariosService {
  constructor(
    @InjectRepository(Usuario)
    private readonly usuarioRepository: Repository<Usuario>,

    private readonly jwtService: JwtService,
  ) {}

  async obtenerTodos() {
    const usuarios = await this.usuarioRepository.find();

    return usuarios.map(({ contrasena, ...usuario }) => usuario);
  }

  async crearUsuario(datos: {
    nombre: string;
    apellido: string;
    cedula: string;
    correo: string;
    contrasena: string;
  }) {
    const existente = await this.usuarioRepository.findOne({
      where: { correo: datos.correo.trim() },
    });

    if (existente) {
      throw new ConflictException('El correo ya está registrado');
    }

    const hash = await bcrypt.hash(datos.contrasena, 12);

    const usuario = this.usuarioRepository.create({
      nombre: `${datos.nombre} ${datos.apellido}`.trim(),
      correo: datos.correo.trim(),
      contrasena: hash,
      rol: 'cliente',
    });

    const guardado = await this.usuarioRepository.save(usuario);
    const { contrasena, ...usuarioPublico } = guardado;

    return usuarioPublico;
  }

  async iniciarSesion(datos: {
    correo: string;
    contrasena: string;
  }) {
    const correo = datos.correo?.trim();
    const contrasenaIngresada = datos.contrasena;

    const usuario = await this.usuarioRepository.findOne({
      where: { correo },
    });

    if (
      !usuario ||
      typeof contrasenaIngresada !== 'string' ||
      contrasenaIngresada.length === 0
    ) {
      throw new UnauthorizedException(
        'Correo o contraseña incorrectos',
      );
    }

    const esHashBcrypt =
      /^\$2[aby]\$\d{2}\$/.test(usuario.contrasena);

    let contrasenaValida: boolean;

    if (esHashBcrypt) {
      contrasenaValida = await bcrypt.compare(
        contrasenaIngresada,
        usuario.contrasena,
      );
    } else {
      // Compatibilidad temporal con usuarios de prueba antiguos.
      contrasenaValida =
        contrasenaIngresada === usuario.contrasena;

      if (contrasenaValida) {
        usuario.contrasena = await bcrypt.hash(
          contrasenaIngresada,
          12,
        );

        await this.usuarioRepository.save(usuario);
      }
    }

    if (!contrasenaValida) {
      throw new UnauthorizedException(
        'Correo o contraseña incorrectos',
      );
    }

    const accessToken = await this.jwtService.signAsync({
      sub: usuario.id_usuario,
      correo: usuario.correo,
      rol: usuario.rol,
    });

    const { contrasena, ...usuarioPublico } = usuario;

    return {
      access_token: accessToken,
      usuario: usuarioPublico,
    };
  }
}
