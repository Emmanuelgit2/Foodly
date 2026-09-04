import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  OneToMany,
  JoinColumn,
} from 'typeorm';

import { Establecimiento } from './establecimiento.entity';
import { Categoria } from './categoria.entity';
import { DetallePedido } from './detalle-pedido.entity';

@Entity('productos')
export class Producto {
  @PrimaryGeneratedColumn({ name: 'id_producto' })
  id_producto: number;

  @Column({ name: 'id_establecimiento' })
  id_establecimiento: number;

  @ManyToOne(
    () => Establecimiento,
    establecimiento => establecimiento.productos,
  )
  @JoinColumn({ name: 'id_establecimiento' })
  establecimiento: Establecimiento;

  @Column({ length: 100 })
  nombre: string;

  @Column({ length: 255, nullable: true })
  descripcion: string;

  @Column({ type: 'decimal', precision: 10, scale: 2 })
  precio: number;

  @Column({ type: 'tinyint', default: 1 })
  disponible: number;

  @Column({ name: 'id_categoria', nullable: true })
  id_categoria: number;

  @ManyToOne(
    () => Categoria,
    categoria => categoria.productos,
    { nullable: true },
  )
  @JoinColumn({ name: 'id_categoria' })
  categoria: Categoria;

  @OneToMany(() => DetallePedido, detalle => detalle.producto)
  detalles: DetallePedido[];
}