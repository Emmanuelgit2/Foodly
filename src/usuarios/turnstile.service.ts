
import { Injectable, ForbiddenException } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class TurnstileService {
  constructor(private readonly configService: ConfigService) {}

  async validarToken(token: string): Promise<void> {
    const secret = this.configService.get<string>('TURNSTILE_SECRET');

    if (!secret || !token || token.length > 2048) {
      throw new ForbiddenException(
        'Completa la verificación CAPTCHA.',
      );
    }

    const body = new URLSearchParams({
      secret,
      response: token,
    });

    try {
      const respuesta = await fetch(
        'https://challenges.cloudflare.com/turnstile/v0/siteverify',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body,
          signal: AbortSignal.timeout(10000),
        },
      );

      if (!respuesta.ok) {
        throw new Error('Error al comunicarse con Cloudflare.');
      }

      const resultado = (await respuesta.json()) as {
        success: boolean;
      };

      if (!resultado.success) {
        throw new ForbiddenException(
          'La verificación CAPTCHA falló. Inténtalo nuevamente.',
        );
      }
    } catch (error) {
      if (error instanceof ForbiddenException) {
        throw error;
      }

      throw new ForbiddenException(
        'No fue posible verificar el CAPTCHA. Inténtalo nuevamente.',
      );
    }
  }
}
