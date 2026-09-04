import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  JoinColumn,
} from 'typeorm';

import { Usuario } from './usuario.entity';

@Entity('direcciones')
export class Direccion {
  @PrimaryGeneratedColumn({ name: 'id_direccion' })
  id_direccion: number;

  @Column({ name: 'id_usuario' })
  id_usuario: number;

  @ManyToOne(() => Usuario, usuario => usuario.direcciones)
  @JoinColumn({ name: 'id_usuario' })
  usuario: Usuario;

  @Column({ length: 255 })
  direccion: string;

  @Column({ length: 100 })
  ciudad: string;

  @Column({ length: 255, nullable: true })
  referencia: string;

  @Column({ type: 'tinyint', default: 0 })
  principal: number;
}