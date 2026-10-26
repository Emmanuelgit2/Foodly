import { Controller, Get, Param } from '@nestjs/common';
import { RestaurantesService } from './restaurantes.service';

@Controller('api/establecimientos')
export class RestaurantesController {
  constructor(private readonly restaurantesService: RestaurantesService) {}

  @Get()
  async getAll() {
    return this.restaurantesService.findAll();
  }

  @Get(':id')
  async getOne(@Param('id') id: string) {
    return this.restaurantesService.findOne(id);
  }
}