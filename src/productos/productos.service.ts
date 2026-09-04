import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';

import { Producto } from '../entities/producto.entity';

@Injectable()
export class ProductosService {
  constructor(
    @InjectRepository(Producto)
    private readonly productoRepository: Repository<Producto>,
  ) {}

  async obtenerTodos(): Promise<Producto[]> {
    return this.productoRepository.find({
      relations: {
        establecimiento: true,
        categoria: true,
      },
    });
  }
}