import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  JoinColumn,
} from 'typeorm';

import { Pedido } from './pedido.entity';
import { Producto } from './producto.entity';

@Entity('detalle_pedido')
export class DetallePedido {
  @PrimaryGeneratedColumn({ name: 'id_detalle' })
  id_detalle: number;

  @Column({ name: 'id_pedido' })
  id_pedido: number;

  @ManyToOne(() => Pedido, pedido => pedido.detalles)
  @JoinColumn({ name: 'id_pedido' })
  pedido: Pedido;

  @Column({ name: 'id_producto' })
  id_producto: number;

  @ManyToOne(() => Producto, producto => producto.detalles)
  @JoinColumn({ name: 'id_producto' })
  producto: Producto;

  @Column()
  cantidad: number;

  @Column({ type: 'decimal', precision: 10, scale: 2 })
  precio_unitario: number;

  @Column({ type: 'decimal', precision: 10, scale: 2 })
  subtotal: number;
}