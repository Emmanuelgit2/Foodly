
import { Injectable, UnauthorizedException } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class EstrategiaJwt extends PassportStrategy(Strategy) {
  constructor(configService: ConfigService) {
    const secret = configService.get<string>('JWT_SECRET');

    if (!secret) {
      throw new Error('Falta configurar JWT_SECRET en el archivo .env');
    }

    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKey: secret,
    });
  }

  async validate(payload: {
    sub: number;
    correo: string;
    rol: string;
  }) {
    if (!payload.sub || !payload.correo) {
      throw new UnauthorizedException('Token inválido');
    }

    return {
      id_usuario: payload.sub,
      correo: payload.correo,
      rol: payload.rol,
    };
  }
}