import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';

import { AppController } from './app.controller';
import { AppService } from './app.service';

import { Usuario } from './entities/usuario.entity';
import { Repartidor } from './entities/repartidor.entity';
import { Establecimiento } from './entities/establecimiento.entity';
import { Categoria } from './entities/categoria.entity';
import { Producto } from './entities/producto.entity';
import { Pedido } from './entities/pedido.entity';
import { DetallePedido } from './entities/detalle-pedido.entity';
import { Pago } from './entities/pago.entity';
import { Direccion } from './entities/direccion.entity';
import { Calificacion } from './entities/calificacion.entity';
import { SeguimientoPedido } from './entities/seguimiento-pedido.entity';

import { UsuariosModule } from './usuarios/usuarios.module';
import { ProductosModule } from './productos/productos.module';
import { PedidosModule } from './pedidos/pedidos.module';
import { DetallePedidoModule } from './detalle-pedido/detalle-pedido.module';

@Module({
  imports: [
    TypeOrmModule.forRoot({
      type: 'mysql',
      host: 'localhost',
      port: 3306,
      username: 'root',
      password: '1036518276',
      database: 'saborapp',
      entities: [
        Usuario,
        Repartidor,
        Establecimiento,
        Categoria,
        Producto,
        Pedido,
        DetallePedido,
        Pago,
        Direccion,
        Calificacion,
        SeguimientoPedido,
      ],
      synchronize: false,
    }),

    UsuariosModule,
    ProductosModule,
    PedidosModule,
    DetallePedidoModule,
  ],

  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}





//http://localhost:3000/usuarios//
//http://localhost:3000/productos//
//http://localhost:3000/pedidos//