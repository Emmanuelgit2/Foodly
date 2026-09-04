import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  JoinColumn,
} from 'typeorm';

import { Pedido } from './pedido.entity';
import { Usuario } from './usuario.entity';

@Entity('calificaciones')
export class Calificacion {
  @PrimaryGeneratedColumn({ name: 'id_calificacion' })
  id_calificacion: number;

  @Column({ name: 'id_pedido' })
  id_pedido: number;

  @ManyToOne(() => Pedido, pedido => pedido.calificaciones)
  @JoinColumn({ name: 'id_pedido' })
  pedido: Pedido;

  @Column({ name: 'id_usuario' })
  id_usuario: number;

  @ManyToOne(() => Usuario, usuario => usuario.calificaciones)
  @JoinColumn({ name: 'id_usuario' })
  usuario: Usuario;

  @Column()
  puntuacion: number;

  @Column({ type: 'text', nullable: true })
  comentario: string;
}