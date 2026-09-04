import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';

import { Pedido } from '../entities/pedido.entity';

@Injectable()
export class PedidosService {
  constructor(
    @InjectRepository(Pedido)
    private readonly pedidoRepository: Repository<Pedido>,
  ) {}

  async obtenerTodos(): Promise<Pedido[]> {
    return this.pedidoRepository.find({
      relations: {
        usuario: true,
        repartidor: true,
      },
    });
  }
}