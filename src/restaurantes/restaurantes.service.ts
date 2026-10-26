import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Restaurante, RestauranteDocument } from './restaurante.schema';

@Injectable()
export class RestaurantesService {
  constructor(
    @InjectModel(Restaurante.name)
    private restauranteModel: Model<RestauranteDocument>,
  ) {}

  async findAll(): Promise<Restaurante[]> {
    return this.restauranteModel.find().exec();
  }

  async findOne(id: string): Promise<Restaurante> {
    return this.restauranteModel.findById(id).exec();
  }
}