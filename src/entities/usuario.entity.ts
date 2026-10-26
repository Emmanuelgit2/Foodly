
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

  @Column({ length: 100 })
  nombre: string;

  @Column({ length: 100, unique: true })
  correo: string;

  @Column({ name: 'conrasena', length: 255 })
  contrasena: string;

  @Column({ length: 30, default: 'cliente' })
  rol: string;

  @Column({ type: 'varchar', length: 255, nullable: true })
googleId: string;

// Asegúrate de que la contraseña sea opcional si el usuario inicia sesión con Google
@Column({ type: 'varchar', length: 255, nullable: true })
password?: string;

  @OneToMany(() => Calificacion, (calificacion) => calificacion.usuario)
  calificaciones: Calificacion[];
}
