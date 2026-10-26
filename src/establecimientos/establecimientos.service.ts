import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';

import { Establecimiento } from '../entities/establecimiento.entity';

@Injectable()
export class EstablecimientosService {
  constructor(
    @InjectRepository(Establecimiento)
    private readonly establecimientoRepository: Repository<Establecimiento>,
  ) {}

  async obtenerActivos() {
    return this.establecimientoRepository.find({
      where: { estado: 1 },
      select: {
        id_establecimiento: true,
        nombre: true,
        descripcion: true,
        direccion: true,
        telefono: true,
      },
      order: {
        nombre: 'ASC',
      },
    });
  }

  async crear(datos: {
    nombre: string;
    descripcion?: string;
    direccion: string;
    telefono?: string;
  }) {
    const establecimiento = this.establecimientoRepository.create({
      nombre: datos.nombre,
      descripcion: datos.descripcion,
      direccion: datos.direccion,
      telefono: datos.telefono,
      estado: 1,
    });

    return this.establecimientoRepository.save(establecimiento);
  }
}
