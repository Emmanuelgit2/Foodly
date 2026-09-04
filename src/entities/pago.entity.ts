import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  JoinColumn,
} from 'typeorm';

import { Pedido } from './pedido.entity';

@Entity('pagos')
export class Pago {
  @PrimaryGeneratedColumn({ name: 'id_pago' })
  id_pago: number;

  @Column({ name: 'id_pedido' })
  id_pedido: number;

  @ManyToOne(() => Pedido, pedido => pedido.pagos)
  @JoinColumn({ name: 'id_pedido' })
  pedido: Pedido;

  @Column({ name: 'metodo_pago', length: 50 })
  metodo_pago: string;

  @Column({ length: 30 })
  estado: string;

  @Column({ name: 'fecha_pago', type: 'datetime', nullable: true })
  fecha_pago: Date;
}