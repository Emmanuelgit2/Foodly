import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  OneToMany,
  JoinColumn,
} from 'typeorm';

import { Usuario } from './usuario.entity';
import { Pedido } from './pedido.entity';

@Entity('repartidores')
export class Repartidor {
  @PrimaryGeneratedColumn({ name: 'id_repartidor' })
  id_repartidor: number;

  @Column({ name: 'id_usuario' })
  id_usuario: number;

  @ManyToOne(() => Usuario, usuario => usuario.repartidores)
  @JoinColumn({ name: 'id_usuario' })
  usuario: Usuario;

  @Column({ length: 20 })
  telefono: string;

  @Column({ length: 50 })
  vehiculo: string;

  @Column({ length: 20 })
  placa: string;

  @Column({ type: 'tinyint', default: 1 })
  disponible: number;

  @OneToMany(() => Pedido, pedido => pedido.repartidor)
  pedidos: Pedido[];
}