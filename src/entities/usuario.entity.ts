import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  OneToMany,
} from 'typeorm';

import { Pedido } from './pedido.entity';
import { Direccion } from './direccion.entity';
import { Repartidor } from './repartidor.entity';
import { Calificacion } from './calificacion.entity';

@Entity('usuarios')
export class Usuario {
  @PrimaryGeneratedColumn({ name: 'id_usuario' })
  id_usuario: number;

  @Column({ length: 100 })
  nombre: string;

  @Column({ length: 100, unique: true })
  correo: string;

  @Column({ name: 'contrasena', length: 255 })
  contrasena: string;

  @Column({ length: 30, default: 'cliente' })
  rol: string;

  @OneToMany(() => Pedido, pedido => pedido.usuario)
  pedidos: Pedido[];

  @OneToMany(() => Direccion, direccion => direccion.usuario)
  direcciones: Direccion[];

  @OneToMany(() => Repartidor, repartidor => repartidor.usuario)
  repartidores: Repartidor[];

  @OneToMany(() => Calificacion, calificacion => calificacion.usuario)
  calificaciones: Calificacion[];
}