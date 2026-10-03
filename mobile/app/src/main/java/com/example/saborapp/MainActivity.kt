package com.example.saborapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Naranja = Color(0xFFFF6B00)
private val NaranjaClaro = Color(0xFFFFF1E8)
private val Negro = Color(0xFF171717)
private val NegroSuave = Color(0xFF252525)
private val GrisFondo = Color(0xFFF6F6F6)
private val GrisTexto = Color(0xFF707070)
private val Blanco = Color.White
private val Verde = Color(0xFF168A4A)

data class Restaurante(
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val calificacion: String,
    val distancia: String,
    val direccion: String,
    val horario: String,
    val telefono: String,
    val imagen: Int,
    val productos: List<String>
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FoodlyApp()
        }
    }
}

@Composable
fun FoodlyApp() {

    var pantalla by remember {
        mutableStateOf("splash")
    }

    var pagina by remember {
        mutableStateOf("inicio")
    }

    var restauranteSeleccionado by remember {
        mutableStateOf<Restaurante?>(null)
    }

    val favoritos = remember {
        mutableStateListOf<String>()
    }

    val restaurantes = remember {
        listOf(
            Restaurante(
                nombre = "Sabor Paisa",
                categoria = "Comida típica",
                descripcion = "Sabores tradicionales, comida rápida y platos con esencia paisa.",
                calificacion = "4.8",
                distancia = "0.8 km",
                direccion = "Bello, Antioquia",
                horario = "11:00 AM - 10:30 PM",
                telefono = "3116469679",
                imagen = R.drawable.sabor_paisa,
                productos = listOf(
                    "Hamburguesa clásica",
                    "Perro especial",
                    "Papas especiales",
                    "Chuzo artesanal"
                )
            ),
            Restaurante(
                nombre = "La Parrilla del Valle",
                categoria = "Parrilla",
                descripcion = "Carnes a la parrilla y preparaciones para compartir.",
                calificacion = "4.6",
                distancia = "1.4 km",
                direccion = "Bello, Antioquia",
                horario = "12:00 PM - 10:00 PM",
                telefono = "3000000000",
                imagen = R.drawable.parrilla_valle,
                productos = listOf(
                    "Parrillada de la casa",
                    "Churrasco",
                    "Costillas BBQ",
                    "Papas criollas"
                )
            ),
            Restaurante(
                nombre = "Burger House",
                categoria = "Hamburguesas",
                descripcion = "Hamburguesas artesanales, papas y combinaciones especiales.",
                calificacion = "4.7",
                distancia = "2.1 km",
                direccion = "Bello, Antioquia",
                horario = "5:00 PM - 11:00 PM",
                telefono = "3010000000",
                imagen = R.drawable.burger_house,
                productos = listOf(
                    "Burger House",
                    "Cheese Burger",
                    "Bacon Burger",
                    "Papas cheddar"
                )
            ),
            Restaurante(
                nombre = "México en la Esquina",
                categoria = "Mexicana",
                descripcion = "Una propuesta mexicana con tacos, burritos y sabores intensos.",
                calificacion = "4.5",
                distancia = "2.7 km",
                direccion = "Bello, Antioquia",
                horario = "5:00 PM - 10:30 PM",
                telefono = "3020000000",
                imagen = R.drawable.mexico_esquina,
                productos = listOf(
                    "Tacos al pastor",
                    "Burrito especial",
                    "Nachos",
                    "Quesadilla"
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(1800)
        pantalla = "inicio"
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = GrisFondo
    ) {

        when (pantalla) {

            "splash" -> {
                SplashPro()
            }

            "inicio" -> {
                PantallaInicioPro(
                    onComenzar = {
                        pantalla = "login"
                    }
                )
            }

            "login" -> {
                PantallaLoginPro(
                    onIngresar = {
                        pantalla = "app"
                    }
                )
            }

            "app" -> {

                if (restauranteSeleccionado != null) {

                    PantallaRestaurantePro(
                        restaurante = restauranteSeleccionado!!,
                        favorito = favoritos.contains(
                            restauranteSeleccionado!!.nombre
                        ),
                        onVolver = {
                            restauranteSeleccionado = null
                        },
                        onFavorito = {

                            val nombre = restauranteSeleccionado!!.nombre

                            if (favoritos.contains(nombre)) {
                                favoritos.remove(nombre)
                            } else {
                                favoritos.add(nombre)
                            }
                        }
                    )

                } else {

                    Scaffold(
                        containerColor = GrisFondo,
                        bottomBar = {

                            BarraNavegacionPro(
                                paginaActual = pagina,
                                onPagina = {
                                    pagina = it
                                }
                            )
                        }
                    ) { padding ->

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {

                            when (pagina) {

                                "inicio" -> {
                                    PantallaHomePro(
                                        restaurantes = restaurantes,
                                        favoritos = favoritos,
                                        onRestaurante = {
                                            restauranteSeleccionado = it
                                        },
                                        onFavorito = {
                                            if (favoritos.contains(it.nombre)) {
                                                favoritos.remove(it.nombre)
                                            } else {
                                                favoritos.add(it.nombre)
                                            }
                                        },
                                        onExplorar = {
                                            pagina = "explorar"
                                        }
                                    )
                                }

                                "explorar" -> {
                                    PantallaExplorarPro(
                                        restaurantes = restaurantes,
                                        favoritos = favoritos,
                                        onRestaurante = {
                                            restauranteSeleccionado = it
                                        },
                                        onFavorito = {
                                            if (favoritos.contains(it.nombre)) {
                                                favoritos.remove(it.nombre)
                                            } else {
                                                favoritos.add(it.nombre)
                                            }
                                        }
                                    )
                                }

                                "favoritos" -> {
                                    PantallaFavoritosPro(
                                        restaurantes = restaurantes,
                                        favoritos = favoritos,
                                        onRestaurante = {
                                            restauranteSeleccionado = it
                                        }
                                    )
                                }

                                "perfil" -> {
                                    PantallaPerfilPro()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashPro() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.foodly_logo
                ),
                contentDescription = "Foodly",
                modifier = Modifier
                    .width(240.dp)
                    .height(180.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "DESCUBRE. COME. DISFRUTA.",
                color = GrisTexto,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
        }
    }
}

@Composable
fun PantallaInicioPro(
    onComenzar: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(45.dp)
        )

        Image(
            painter = painterResource(
                id = R.drawable.foodly_logo
            ),
            contentDescription = "Foodly",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Tu próxima experiencia gastronómica comienza aquí.",
            color = Negro,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "Descubre restaurantes, sabores y lugares que valen la pena conocer.",
            color = GrisTexto,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(
            modifier = Modifier.height(35.dp)
        )

        Button(
            onClick = onComenzar,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Naranja,
                contentColor = Blanco
            )
        ) {

            Text(
                text = "Comenzar",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "Explora sabores cerca de ti",
            color = GrisTexto,
            fontSize = 13.sp
        )
    }
}

@Composable
fun PantallaLoginPro(
    onIngresar: () -> Unit
) {

    var nombre by remember {
        mutableStateOf("")
    }

    var pais by remember {
        mutableStateOf("")
    }

    var edad by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Text(
            text = "Bienvenido a Foodly",
            color = Negro,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Cuéntanos un poco sobre ti para personalizar tu experiencia.",
            color = GrisTexto,
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        CampoPro(
            valor = nombre,
            onValor = {
                nombre = it
                error = false
            },
            etiqueta = "Nombre completo"
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        CampoPro(
            valor = pais,
            onValor = {
                pais = it
                error = false
            },
            etiqueta = "País"
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        CampoPro(
            valor = edad,
            onValor = {
                edad = it
                error = false
            },
            etiqueta = "Edad"
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        if (error) {

            Text(
                text = "Completa todos los campos para continuar.",
                color = Color(0xFFD32F2F),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        Button(
            onClick = {

                if (
                    nombre.isNotBlank() &&
                    pais.isNotBlank() &&
                    edad.isNotBlank()
                ) {
                    onIngresar()
                } else {
                    error = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Naranja,
                contentColor = Blanco
            )
        ) {

            Text(
                text = "Entrar a Foodly",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CampoPro(
    valor: String,
    onValor: (String) -> Unit,
    etiqueta: String
) {

    OutlinedTextField(
        value = valor,
        onValueChange = onValor,
        label = {
            Text(etiqueta)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
fun PantallaHomePro(
    restaurantes: List<Restaurante>,
    favoritos: List<String>,
    onRestaurante: (Restaurante) -> Unit,
    onFavorito: (Restaurante) -> Unit,
    onExplorar: () -> Unit
) {

    var busqueda by remember {
        mutableStateOf("")
    }

    var categoria by remember {
        mutableStateOf("Todos")
    }

    val categorias = listOf(
        "Todos",
        "Comida típica",
        "Parrilla",
        "Hamburguesas",
        "Mexicana"
    )

    val filtrados = restaurantes.filter {

        val coincideCategoria =
            categoria == "Todos" ||
                    it.categoria == categoria

        val coincideBusqueda =
            it.nombre.contains(
                busqueda,
                ignoreCase = true
            ) ||
                    it.categoria.contains(
                        busqueda,
                        ignoreCase = true
                    )

        coincideCategoria && coincideBusqueda
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Negro)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 25.dp,
                    bottom = 24.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Hola 👋",
                        color = Color(0xFFBDBDBD),
                        fontSize = 14.sp
                    )

                    Text(
                        text = "¿Qué quieres comer?",
                        color = Blanco,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Naranja),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "F",
                        color = Blanco,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            BuscadorPro(
                valor = busqueda,
                onValor = {
                    busqueda = it
                }
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Explora por categoría",
            color = Negro,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            categorias.forEach { item ->

                CategoriaChip(
                    texto = item,
                    seleccionada = categoria == item,
                    onClick = {
                        categoria = item
                    }
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Destacados",
                color = Negro,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = onExplorar
            ) {

                Text(
                    text = "Ver todos",
                    color = Naranja,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            restaurantes.take(4).forEach { restaurante ->

                RestauranteDestacado(
                    restaurante = restaurante,
                    favorito = favoritos.contains(
                        restaurante.nombre
                    ),
                    onClick = {
                        onRestaurante(restaurante)
                    },
                    onFavorito = {
                        onFavorito(restaurante)
                    }
                )

                Spacer(
                    modifier = Modifier.width(14.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "Restaurantes cerca de ti",
            color = Negro,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        if (filtrados.isEmpty()) {

            EstadoVacio()

        } else {

            filtrados.forEach { restaurante ->

                RestauranteListaPro(
                    restaurante = restaurante,
                    favorito = favoritos.contains(
                        restaurante.nombre
                    ),
                    onClick = {
                        onRestaurante(restaurante)
                    },
                    onFavorito = {
                        onFavorito(restaurante)
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )
    }
}

@Composable
fun BuscadorPro(
    valor: String,
    onValor: (String) -> Unit
) {

    OutlinedTextField(
        value = valor,
        onValueChange = onValor,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = {
            Text(
                text = "Buscar restaurantes, comida..."
            )
        },
        leadingIcon = {
            Text(
                text = "⌕",
                fontSize = 25.sp,
                color = GrisTexto
            )
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun CategoriaChip(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .clickable {
                onClick()
            },
        color = if (seleccionada) {
            Naranja
        } else {
            Blanco
        },
        shape = RoundedCornerShape(30.dp)
    ) {

        Text(
            text = texto,
            color = if (seleccionada) {
                Blanco
            } else {
                Negro
            },
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                horizontal = 17.dp,
                vertical = 11.dp
            )
        )
    }
}

@Composable
fun RestauranteDestacado(
    restaurante: Restaurante,
    favorito: Boolean,
    onFavorito: () -> Unit,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .width(250.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .background(
                        color = when (restaurante.categoria) {
                            "Parrilla" -> Color(0xFF3A2922)
                            "Hamburguesas" -> Color(0xFF5A351E)
                            "Mexicana" -> Color(0xFF7A3525)
                            "Comida típica" -> Color(0xFF4A3526)
                            else -> Negro
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = when (restaurante.categoria) {
                            "Parrilla" -> "🔥"
                            "Hamburguesas" -> "🍔"
                            "Mexicana" -> "🌮"
                            "Comida típica" -> "🍽️"
                            else -> "🍴"
                        },
                        fontSize = 48.sp
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "FOODLY",
                        color = Blanco,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }

                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            onFavorito()
                        },
                    shape = CircleShape,
                    color = Blanco
                ) {

                    Text(
                        text = if (favorito) "♥" else "♡",
                        color = if (favorito) {
                            Naranja
                        } else {
                            Negro
                        },
                        fontSize = 22.sp,
                        modifier = Modifier.padding(7.dp)
                    )
                }

                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.BottomStart),
                    shape = RoundedCornerShape(10.dp),
                    color = Naranja
                ) {

                    Text(
                        text = restaurante.categoria,
                        color = Blanco,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 5.dp
                        )
                    )
                }
            }

            Column(
                modifier = Modifier.padding(14.dp)
            ) {

                Text(
                    text = restaurante.nombre,
                    color = Negro,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = restaurante.descripcion,
                    color = GrisTexto,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "★ ${restaurante.calificacion}",
                        color = Naranja,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Text(
                        text = restaurante.distancia,
                        color = GrisTexto,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RestauranteListaPro(
    restaurante: Restaurante,
    favorito: Boolean,
    onClick: () -> Unit,
    onFavorito: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 6.dp
            )
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        )
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(
                    id = restaurante.imagen
                ),
                contentDescription = restaurante.nombre,
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = restaurante.nombre,
                    color = Negro,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = restaurante.categoria,
                    color = GrisTexto,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Row {

                    Text(
                        text = "★ ${restaurante.calificacion}",
                        color = Naranja,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Text(
                        text = restaurante.distancia,
                        color = GrisTexto,
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                text = if (favorito) "♥" else "♡",
                color = if (favorito) Naranja else GrisTexto,
                fontSize = 24.sp,
                modifier = Modifier
                    .clickable {
                        onFavorito()
                    }
                    .padding(6.dp)
            )
        }
    }
}

@Composable
fun PantallaExplorarPro(
    restaurantes: List<Restaurante>,
    favoritos: List<String>,
    onRestaurante: (Restaurante) -> Unit,
    onFavorito: (Restaurante) -> Unit
) {

    var busqueda by remember {
        mutableStateOf("")
    }

    var categoria by remember {
        mutableStateOf("Todos")
    }

    val categorias = listOf(
        "Todos",
        "Comida típica",
        "Parrilla",
        "Hamburguesas",
        "Mexicana"
    )

    val resultados = restaurantes.filter {

        val categoriaOk =
            categoria == "Todos" ||
                    it.categoria == categoria

        val textoOk =
            it.nombre.contains(
                busqueda,
                ignoreCase = true
            ) ||
                    it.categoria.contains(
                        busqueda,
                        ignoreCase = true
                    )

        categoriaOk && textoOk
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
            .verticalScroll(rememberScrollState())
            .padding(top = 25.dp)
    ) {

        Text(
            text = "Explorar",
            color = Negro,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Encuentra un lugar que te sorprenda.",
            color = GrisTexto,
            fontSize = 15.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        BuscadorPro(
            valor = busqueda,
            onValor = {
                busqueda = it
            }
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            categorias.forEach {

                CategoriaChip(
                    texto = it,
                    seleccionada = categoria == it,
                    onClick = {
                        categoria = it
                    }
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "${resultados.size} lugares encontrados",
            color = GrisTexto,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        resultados.forEach { restaurante ->

            RestauranteListaPro(
                restaurante = restaurante,
                favorito = favoritos.contains(
                    restaurante.nombre
                ),
                onClick = {
                    onRestaurante(restaurante)
                },
                onFavorito = {
                    onFavorito(restaurante)
                }
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )
    }
}

@Composable
fun PantallaFavoritosPro(
    restaurantes: List<Restaurante>,
    favoritos: List<String>,
    onRestaurante: (Restaurante) -> Unit
) {

    val guardados = restaurantes.filter {
        favoritos.contains(it.nombre)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
            .verticalScroll(rememberScrollState())
            .padding(top = 25.dp)
    ) {

        Text(
            text = "Mis favoritos",
            color = Negro,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Tus lugares guardados para volver cuando quieras.",
            color = GrisTexto,
            fontSize = 15.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (guardados.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "♡",
                    color = Naranja,
                    fontSize = 55.sp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Todavía no tienes favoritos",
                    color = Negro,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Guarda los restaurantes que más te gusten y aparecerán aquí.",
                    color = GrisTexto,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

        } else {

            guardados.forEach { restaurante ->

                RestauranteListaPro(
                    restaurante = restaurante,
                    favorito = true,
                    onClick = {
                        onRestaurante(restaurante)
                    },
                    onFavorito = {}
                )
            }
        }
    }
}

@Composable
fun PantallaPerfilPro() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Mi perfil",
            color = Negro,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Negro
            )
        ) {

            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(Naranja),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "E",
                        color = Blanco,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.width(15.dp)
                )

                Column {

                    Text(
                        text = "Usuario Foodly",
                        color = Blanco,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Explorador gastronómico",
                        color = Color(0xFFBDBDBD),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        PerfilOpcionPro(
            titulo = "Mis favoritos",
            descripcion = "Restaurantes que has guardado"
        )

        PerfilOpcionPro(
            titulo = "Mis preferencias",
            descripcion = "Personaliza lo que quieres descubrir"
        )

        PerfilOpcionPro(
            titulo = "Sobre Foodly",
            descripcion = "Conoce más sobre nuestra plataforma"
        )

        PerfilOpcionPro(
            titulo = "Ayuda y soporte",
            descripcion = "Estamos aquí para ayudarte"
        )
    }
}

@Composable
fun PerfilOpcionPro(
    titulo: String,
    descripcion: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        )
    ) {

        Row(
            modifier = Modifier.padding(17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(NaranjaClaro),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "•",
                    color = Naranja,
                    fontSize = 25.sp
                )
            }

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column {

                Text(
                    text = titulo,
                    color = Negro,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = descripcion,
                    color = GrisTexto,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun BarraNavegacionPro(
    paginaActual: String,
    onPagina: (String) -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Blanco,
        shadowElevation = 8.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            NavegacionItemPro(
                icono = "⌂",
                titulo = "Inicio",
                activo = paginaActual == "inicio",
                onClick = {
                    onPagina("inicio")
                }
            )

            NavegacionItemPro(
                icono = "⌕",
                titulo = "Explorar",
                activo = paginaActual == "explorar",
                onClick = {
                    onPagina("explorar")
                }
            )

            NavegacionItemPro(
                icono = "♡",
                titulo = "Favoritos",
                activo = paginaActual == "favoritos",
                onClick = {
                    onPagina("favoritos")
                }
            )

            NavegacionItemPro(
                icono = "●",
                titulo = "Perfil",
                activo = paginaActual == "perfil",
                onClick = {
                    onPagina("perfil")
                }
            )
        }
    }
}

@Composable
fun NavegacionItemPro(
    icono: String,
    titulo: String,
    activo: Boolean,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 15.dp,
                vertical = 5.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = icono,
            color = if (activo) Naranja else GrisTexto,
            fontSize = 22.sp,
            fontWeight = if (activo) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )

        Text(
            text = titulo,
            color = if (activo) Naranja else GrisTexto,
            fontSize = 11.sp,
            fontWeight = if (activo) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}

@Composable
fun PantallaRestaurantePro(
    restaurante: Restaurante,
    favorito: Boolean,
    onVolver: () -> Unit,
    onFavorito: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {

            Image(
                painter = painterResource(
                    id = restaurante.imagen
                ),
                contentDescription = restaurante.nombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier
                    .padding(18.dp)
                    .clickable {
                        onVolver()
                    },
                shape = CircleShape,
                color = Blanco
            ) {

                Text(
                    text = "‹",
                    color = Negro,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 5.dp
                    )
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(18.dp)
                    .clickable {
                        onFavorito()
                    },
                shape = CircleShape,
                color = Blanco
            ) {

                Text(
                    text = if (favorito) "♥" else "♡",
                    color = if (favorito) Naranja else Negro,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(9.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = restaurante.nombre,
                color = Negro,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = restaurante.categoria,
                color = Naranja,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = restaurante.descripcion,
                color = GrisTexto,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                DatoRestaurante(
                    titulo = "Calificación",
                    valor = "★ ${restaurante.calificacion}"
                )

                DatoRestaurante(
                    titulo = "Distancia",
                    valor = restaurante.distancia
                )

                DatoRestaurante(
                    titulo = "Horario",
                    valor = restaurante.horario
                )
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            Text(
                text = "Productos destacados",
                color = Negro,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            restaurante.productos.forEach { producto ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Blanco
                    )
                ) {

                    Row(
                        modifier = Modifier.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NaranjaClaro),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "•",
                                color = Naranja,
                                fontSize = 24.sp
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )

                        Text(
                            text = producto,
                            color = Negro,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Blanco
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Text(
                        text = "Información del lugar",
                        color = Negro,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text = "📍 ${restaurante.direccion}",
                        color = GrisTexto,
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(9.dp)
                    )

                    Text(
                        text = "◷ ${restaurante.horario}",
                        color = GrisTexto,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Verde,
                    contentColor = Blanco
                )
            ) {

                Text(
                    text = "Contactar por WhatsApp",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {

                Text(
                    text = "Ver ubicación"
                )
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )
        }
    }
}

@Composable
fun DatoRestaurante(
    titulo: String,
    valor: String
) {

    Column(
        modifier = Modifier.width(105.dp)
    ) {

        Text(
            text = titulo,
            color = GrisTexto,
            fontSize = 11.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = valor,
            color = Negro,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun EstadoVacio() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "⌕",
            color = Naranja,
            fontSize = 48.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "No encontramos lugares",
            color = Negro,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Prueba con otra búsqueda o categoría.",
            color = GrisTexto,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}