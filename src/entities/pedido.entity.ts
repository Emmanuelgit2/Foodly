import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  OneToMany,
  JoinColumn,
} from 'typeorm';

import { Usuario } from './usuario.entity';
import { Repartidor } from './repartidor.entity';
import { DetallePedido } from './detalle-pedido.entity';
import { Pago } from './pago.entity';
import { SeguimientoPedido } from './seguimiento-pedido.entity';
import { Calificacion } from './calificacion.entity';

@Entity('pedidos')
export class Pedido {
  @PrimaryGeneratedColumn({ name: 'id_pedido' })
  id_pedido: number;

  @Column({ name: 'id_usuario' })
  id_usuario: number;

  @ManyToOne(() => Usuario, usuario => usuario.pedidos)
  @JoinColumn({ name: 'id_usuario' })
  usuario: Usuario;

  @Column({ type: 'datetime', default: () => 'CURRENT_TIMESTAMP' })
  fecha: Date;

  @Column({ length: 30, default: 'pendiente' })
  estado: string;

  @Column({
    type: 'decimal',
    precision: 10,
    scale: 2,
    default: 0,
  })
  total: number;

  @Column({ name: 'direccion_entrega', length: 255, nullable: true })
  direccion_entrega: string;

  @Column({ name: 'id_repartidor', nullable: true })
  id_repartidor: number;

  @ManyToOne(
    () => Repartidor,
    repartidor => repartidor.pedidos,
    { nullable: true },
  )
  @JoinColumn({ name: 'id_repartidor' })
  repartidor: Repartidor;

  @OneToMany(() => DetallePedido, detalle => detalle.pedido)
  detalles: DetallePedido[];

  @OneToMany(() => Pago, pago => pago.pedido)
  pagos: Pago[];

  @OneToMany(() => SeguimientoPedido, seguimiento => seguimiento.pedido)
  seguimientos: SeguimientoPedido[];

  @OneToMany(() => Calificacion, calificacion => calificacion.pedido)
  calificaciones: Calificacion[];
}