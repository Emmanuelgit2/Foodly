import { Controller, Get } from '@nestjs/common';

import { DetallePedidoService } from './detalle-pedido.service';

@Controller('detalle-pedido')
export class DetallePedidoController {
  constructor(
    private readonly detallePedidoService: DetallePedidoService,
  ) {}

  @Get()
  obtenerTodos() {
    return this.detallePedidoService.obtenerTodos();
  }
}