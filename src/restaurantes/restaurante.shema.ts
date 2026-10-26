import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type RestauranteDocument = Restaurante & Document;

@Schema()
export class Restaurante {
  @Prop({ required: true })
  nombre: string;

  @Prop({ required: true })
  categoria: string;

  @Prop({ required: true })
  descripcion: string;

  @Prop({ required: true })
  calificacion: string;

  @Prop({ required: true })
  ubicacion: string;

  @Prop({ required: true })
  direccion: string;

  @Prop({ required: true })
  telefono: string;

  @Prop({ required: true })
  whatsapp: string;

  @Prop()
  fotoPerfil: string;

  @Prop()
  imagenFondo: string;

  @Prop({ type: Array })
  platos: Array<{ nombre: string; precio: string; desc: string }>;
}

export const RestauranteSchema = SchemaFactory.createForClass(Restaurante);