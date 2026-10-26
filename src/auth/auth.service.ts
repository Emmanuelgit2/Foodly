import { Injectable, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class AuthService {
  constructor(private jwtService: JwtService) {}

  // 1. Primer paso de autenticación: Genera sesión temporal MFA
  async login(user: any) {
    const payload = { email: user.email, sub: user.id };
    
    return {
      mfaRequired: true,
      mfaSessionToken: this.jwtService.sign(payload, { expiresIn: '5m' }),
      message: 'Credenciales válidas. Ingrese el código MFA de 6 dígitos enviado a su dispositivo.',
    };
  }

  // 2. Segundo paso: Validación del código MFA para emitir el JWT definitivo
  async verifyMFA(mfaSessionToken: string, code: string) {
    try {
      const payload = this.jwtService.verify(mfaSessionToken);
      
      // Simulación de validación de código MFA (e.g. '123456' o '654321')
      if (code !== '123456' && code !== '000000') {
        throw new UnauthorizedException('Código MFA inválido o expirado.');
      }

      // Generación del Access Token JWT final para consumir endpoints protegidos
      const accessToken = this.jwtService.sign(
        { email: payload.email, sub: payload.sub, mfaAuthenticated: true },
        { expiresIn: '1h' },
      );

      return {
        access_token: accessToken,
        token_type: 'Bearer',
        expires_in: 3600,
        user: { email: payload.email },
      };
    } catch (error) {
      throw new UnauthorizedException('Sesión MFA inválida o token expirado.');
    }
  }
}