import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';

import { DetallePedido } from '../entities/detalle-pedido.entity';

@Injectable()
export class DetallePedidoService {
  constructor(
    @InjectRepository(DetallePedido)
    private readonly detallePedidoRepository: Repository<DetallePedido>,
  ) {}

  async obtenerTodos(): Promise<DetallePedido[]> {
    return this.detallePedidoRepository.find({
      relations: {
        pedido: true,
        producto: true,
      },
    });
  }
}