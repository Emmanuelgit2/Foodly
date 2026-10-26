import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { RestaurantesController } from './restaurantes.controller';
import { RestaurantesService } from './restaurantes.service';
import { Restaurante, RestauranteSchema } from './restaurante.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Restaurante.name, schema: RestauranteSchema },
    ]),
  ],
  controllers: [RestaurantesController],
  providers: [RestaurantesService],
  exports: [RestaurantesService],
})
export class RestaurantesModule {}