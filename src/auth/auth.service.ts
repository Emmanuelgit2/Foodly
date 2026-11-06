import {
  Injectable,
  UnauthorizedException,
  InternalServerErrorException,
} from '@nestjs/common';

import { JwtService } from '@nestjs/jwt';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { OAuth2Client } from 'google-auth-library';

import { Usuario } from '../entities/usuario.entity';

@Injectable()
export class AuthService {
  private readonly googleClient = new OAuth2Client();

  constructor(
    private readonly jwtService: JwtService,

    @InjectRepository(Usuario)
    private readonly usuarioRepository: Repository<Usuario>,
  ) {}

  async loginGoogle(credential: string) {
    const googleClientId = process.env.GOOGLE_CLIENT_ID;

    if (!googleClientId) {
      throw new InternalServerErrorException(
        'Falta configurar GOOGLE_CLIENT_ID en el backend.',
      );
    }

    let payload;

    try {
      const ticket = await this.googleClient.verifyIdToken({
        idToken: credential,
        audience: googleClientId,
      });

      payload = ticket.getPayload();
    } catch {
      throw new UnauthorizedException(
        'La credencial de Google no es válida o ha expirado.',
      );
    }

    if (
      !payload ||
      !payload.sub ||
      !payload.email ||
      payload.email_verified !== true
    ) {
      throw new UnauthorizedException(
        'No fue posible verificar la cuenta de Google.',
      );
    }

    const googleId = payload.sub;
    const correo = payload.email.toLowerCase();
    const nombre = payload.name || correo.split('@')[0];

    let usuario = await this.usuarioRepository.findOne({
      where: { googleId },
    });

    if (!usuario) {
      const usuarioConCorreo =
        await this.usuarioRepository.findOne({
          where: { correo },
        });

      if (usuarioConCorreo) {
        throw new UnauthorizedException(
          'Este correo ya está registrado. Inicia sesión con el método que usaste al registrarte.',
        );
      }

      usuario = this.usuarioRepository.create({
        nombre,
        correo,
        googleId,
        contrasena: null,
        rol: 'cliente',
      });

      try {
        usuario = await this.usuarioRepository.save(usuario);
      } catch {
        // Puede ocurrir si dos solicitudes crean la misma cuenta a la vez.
        const usuarioExistente =
          await this.usuarioRepository.findOne({
            where: { googleId },
          });

        if (!usuarioExistente) {
          throw new InternalServerErrorException(
            'No fue posible guardar la cuenta. Comprueba la estructura de la tabla usuarios.',
          );
        }

        usuario = usuarioExistente;
      }
    }

    const accessToken = await this.jwtService.signAsync({
      sub: usuario.id_usuario,
      correo: usuario.correo,
      rol: usuario.rol,
    });

    return {
      access_token: accessToken,
      token_type: 'Bearer',
      user: {
        id: usuario.id_usuario,
        nombre: usuario.nombre,
        correo: usuario.correo,
        rol: usuario.rol,
      },
    };
  }
}
