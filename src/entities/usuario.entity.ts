import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  OneToMany,
} from 'typeorm';

import { Calificacion } from './calificacion.entity';

@Entity('usuarios')
export class Usuario {
  @PrimaryGeneratedColumn({ name: 'id_usuario' })
  id_usuario: number;

  @Column({ type: 'varchar', length: 100 })
  nombre: string;

  @Column({
    type: 'varchar',
    length: 100,
    unique: true,
  })
  correo: string;

  @Column({
    name: 'conrasena',
    type: 'varchar',
    length: 255,
    nullable: true,
    select: false,
  })
  contrasena: string | null;

  @Column({
    type: 'varchar',
    length: 30,
    default: 'cliente',
  })
  rol: string;

  @Column({
    name: 'googleId',
    type: 'varchar',
    length: 255,
    nullable: true,
    unique: true,
  })
  googleId: string | null;

  @OneToMany(
    () => Calificacion,
    (calificacion) => calificacion.usuario,
  )
  calificaciones: Calificacion[];
}