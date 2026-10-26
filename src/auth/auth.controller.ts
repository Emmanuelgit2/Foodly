import { Controller, Post, Body } from '@nestjs/common';
import { AuthService } from './auth.service';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('login')
  async login(@Body() body: { correo: string; contrasena: string }) {
    // Retorna solicitud de MFA
    return this.authService.login({ email: body.correo, id: 'usr_12345' });
  }

  @Post('mfa/verify')
  async verifyMFA(@Body() body: { mfaSessionToken: string; code: string }) {
    return this.authService.verifyMFA(body.mfaSessionToken, body.code);
  }
}