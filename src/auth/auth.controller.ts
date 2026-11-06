import {
  BadRequestException,
  Body,
  Controller,
  Get,
  HttpCode,
  Post,
} from '@nestjs/common';
import { ConfigService } from '@nestjs/config';

import { AuthService } from './auth.service';
import { TurnstileService } from '../usuarios/turnstile.service';

@Controller('auth')
export class AuthController {
  constructor(
    private readonly authService: AuthService,
    private readonly turnstileService: TurnstileService,
    private readonly configService: ConfigService,
  ) {}

  // La Site Key es pública; nunca devolver TURNSTILE_SECRET desde este endpoint.
  @Get('config')
  getPublicConfig() {
    return {
      turnstileSiteKey:
        this.configService.get<string>('TURNSTILE_SITE_KEY') || null,
    };
  }

  @HttpCode(200)
  @Post('google')
  async loginGoogle(
    @Body() body: { credential?: string; turnstileToken?: string },
  ) {
    if (
      !body ||
      typeof body.credential !== 'string' ||
      !body.credential.trim()
    ) {
      throw new BadRequestException(
        'No se recibió la credencial de Google.',
      );
    }

    await this.turnstileService.validarToken(body.turnstileToken || '');
    return this.authService.loginGoogle(body.credential);
  }
}
