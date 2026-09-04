import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  JoinColumn,
} from 'typeorm';

import { Pedido } from './pedido.entity';

@Entity('seguimiento_pedido')
export class SeguimientoPedido {
  @PrimaryGeneratedColumn({ name: 'id_seguimiento' })
  id_seguimiento: number;

  @Column({ name: 'id_pedido' })
  id_pedido: number;

  @ManyToOne(() => Pedido, pedido => pedido.seguimientos)
  @JoinColumn({ name: 'id_pedido' })
  pedido: Pedido;

  @Column({ length: 30 })
  estado: string;

  @Column({ type: 'datetime' })
  fecha: Date;
}