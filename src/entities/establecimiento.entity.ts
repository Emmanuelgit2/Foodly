import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  OneToMany,
} from 'typeorm';

import { Producto } from './producto.entity';

@Entity('establecimientos')
export class Establecimiento {
  @PrimaryGeneratedColumn({ name: 'id_establecimiento' })
  id_establecimiento: number;

  @Column({ length: 100 })
  nombre: string;

  @Column({ length: 255, nullable: true })
  descripcion: string;

  @Column({ length: 150 })
  direccion: string;

  @Column({ length: 20, nullable: true })
  telefono: string;

  @Column({ type: 'tinyint', default: 1 })
  estado: number;

  @OneToMany(() => Producto, producto => producto.establecimiento)
  productos: Producto[];
}