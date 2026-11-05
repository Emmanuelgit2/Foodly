
package com.example.saborapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import coil.compose.AsyncImage
import kotlin.coroutines.resume

private val Orange = Color(0xFFFF6B00)
private val LocalFoodlyDarkMode = compositionLocalOf { false }

@Composable private fun appInk() = if (LocalFoodlyDarkMode.current) Color(0xFFF3F3F3) else Color(0xFF191919)
@Composable private fun appBackground() = if (LocalFoodlyDarkMode.current) Color(0xFF121212) else Color(0xFFF6F6F6)
@Composable private fun appSurface() = if (LocalFoodlyDarkMode.current) Color(0xFF202020) else Color.White
@Composable private fun appMuted() = if (LocalFoodlyDarkMode.current) Color(0xFFB0B0B0) else Color(0xFF747474)

data class Product(
    val name: String,
    val image: String,
    // Precio ficticio de demostración en pesos colombianos; se asigna una vez al iniciar la app.
    val price: Int = (21..32).random() * 1_000
)

data class Restaurant(
    val id: Int,
    val name: String,
    val category: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val description: String,
    val phone: String,
    val profileImage: String,
    val products: List<Product>
)

private fun photo(id: String) =
    "https://images.unsplash.com/$id?w=900&auto=format&fit=crop&q=85"

private val sampleRestaurants = listOf(
    Restaurant(
        id = 1,
        name = "Sabor Paisa",
        category = "Comida paisa",
        location = "Bello",
        latitude = 6.337,
        longitude = -75.558,
        address = "Bello, Antioquia, Colombia",
        description = "Tradición paisa, sabores auténticos y platos para compartir.",
        phone = "573001234567",
        profileImage = photo("photo-1547592180-85f173990554"),
        products = listOf(
            Product("Plato de la casa", photo("photo-1547592180-85f173990554")),
            Product("Hamburguesa artesanal", photo("photo-1568901346375-23c9450c58cd")),
            Product("Parrilla especial", photo("photo-1544025162-d76694265947")),
            Product("Arepa con queso", photo("photo-1601050690597-df0568f70950")),
            Product("Perro caliente", photo("photo-1612392062798-2dba9f5f6a14"))
        )
    ),
    Restaurant(
        id = 2,
        name = "La Parrilla del Valle",
        category = "Parrilla",
        location = "Laureles",
        latitude = 6.245,
        longitude = -75.590,
        address = "Laureles, Medellín, Antioquia, Colombia",
        description = "Carnes a la parrilla y preparaciones para disfrutar sin afán.",
        phone = "573001234568",
        profileImage = photo("photo-1544025162-d76694265947"),
        products = listOf(
            Product("Carne a la parrilla", photo("photo-1544025162-d76694265947")),
            Product("Costillas", photo("photo-1529193591184-b1d58069ecdd")),
            Product("Hamburguesa", photo("photo-1568901346375-23c9450c58cd")),
            Product("Pollo asado", photo("photo-1532550907401-a500c9a57435"))
        )
    ),
    Restaurant(
        id = 3,
        name = "Burger House",
        category = "Hamburguesas",
        location = "El Poblado",
        latitude = 6.208,
        longitude = -75.568,
        address = "El Poblado, Medellín, Antioquia, Colombia",
        description = "Hamburguesas artesanales para quienes buscan algo diferente.",
        phone = "573001234569",
        profileImage = photo("photo-1568901346375-23c9450c58cd"),
        products = listOf(
            Product("Burger clásica", photo("photo-1568901346375-23c9450c58cd")),
            Product("Burger doble", photo("photo-1553979459-d2229ba7433a")),
            Product("Papas crocantes", photo("photo-1573080496219-bb080dd4f877")),
            Product("Burger especial", photo("photo-1550547660-d9450f859349"))
        )
    ),
    Restaurant(
        id = 4,
        name = "México en la Esquina",
        category = "Comida mexicana",
        location = "Sabaneta",
        latitude = 6.151,
        longitude = -75.616,
        address = "Sabaneta, Antioquia, Colombia",
        description = "Tacos, colores y sabores inspirados en la cocina mexicana.",
        phone = "573001234570",
        profileImage = photo("photo-1551504734-5ee1c4a1479b"),
        products = listOf(
            Product("Tacos", photo("photo-1551504734-5ee1c4a1479b")),
            Product("Burritos", photo("photo-1565299585323-38d6b0865b47")),
            Product("Nachos", photo("photo-1513456852971-30c0b8199d4d")),
            Product("Quesadillas", photo("photo-1618040996337-56904b7850b9"))
        )
    ),
    Restaurant(
        id = 5,
        name = "Dragón Wok",
        category = "Comida china",
        location = "Laureles",
        latitude = 6.247,
        longitude = -75.594,
        address = "Laureles, Medellín, Antioquia, Colombia",
        description = "Arroces, noodles y sabores orientales preparados al wok.",
        phone = "573001234571",
        profileImage = photo("photo-1525755662778-989d0524087e"),
        products = listOf(
            Product("Arroz chino especial", photo("photo-1512058564366-18510be2db19")),
            Product("Pollo agridulce", photo("photo-1546069901-ba9599a7e63c")),
            Product("Noodles salteados", photo("photo-1569718212165-3a8278d5f624")),
            Product("Rollitos primavera", photo("photo-1544025162-d76694265947"))
        )
    ),
    Restaurant(
        id = 6,
        name = "Rápido y Rico",
        category = "Comida rápida",
        location = "El Poblado",
        latitude = 6.211,
        longitude = -75.565,
        address = "El Poblado, Medellín, Antioquia, Colombia",
        description = "Antojos rápidos: perros, papas y hamburguesas para compartir.",
        phone = "573001234572",
        profileImage = photo("photo-1571091718767-18b5b1457add"),
        products = listOf(
            Product("Perro caliente", photo("photo-1612392062798-2dba9f5f6a14")),
            Product("Papas cargadas", photo("photo-1573080496219-bb080dd4f877")),
            Product("Combo de hamburguesa", photo("photo-1568901346375-23c9450c58cd")),
            Product("Alitas BBQ", photo("photo-1527477396000-e27163b481c2"))
        )
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkMode by rememberSaveable {
                mutableStateOf(getSharedPreferences("foodly_settings", MODE_PRIVATE).getBoolean("dark_mode", false))
            }
            val colors = if (darkMode) {
                darkColorScheme(primary = Orange, onPrimary = Color.White, background = Color(0xFF121212), surface = Color(0xFF202020))
            } else {
                lightColorScheme(primary = Orange, onPrimary = Color.White, background = Color(0xFFF6F6F6), surface = Color.White)
            }
            CompositionLocalProvider(LocalFoodlyDarkMode provides darkMode) {
                MaterialTheme(colorScheme = colors) {
                    FoodlyRoot(
                        darkMode = darkMode,
                        onToggleDarkMode = {
                            darkMode = !darkMode
                            getSharedPreferences("foodly_settings", MODE_PRIVATE).edit()
                                .putBoolean("dark_mode", darkMode).apply()
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodlyRoot(darkMode: Boolean, onToggleDarkMode: () -> Unit) {
    var page by remember { mutableStateOf("login") }
    var selected by remember { mutableStateOf<Restaurant?>(null) }
    var query by remember { mutableStateOf("") }
    var zone by remember { mutableStateOf("Todas") }
    var category by remember { mutableStateOf("Todas") }
    val saved = remember { mutableStateListOf<Int>() }
    var message by remember { mutableStateOf("") }
    var pendingGoogleCredential by remember { mutableStateOf<String?>(null) }
    var turnstileSiteKey by remember { mutableStateOf<String?>(null) }
    var showCaptcha by remember { mutableStateOf(false) }
    var isSigningIn by remember { mutableStateOf(false) }
    var nearbyEnabled by rememberSaveable { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var locationMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val updateLocation: () -> Unit = {
        coroutineScope.launch {
            locationMessage = "Buscando tu ubicación..."
            try {
                userLocation = getCurrentLocation(context)
                nearbyEnabled = true
                locationMessage = "Restaurantes ordenados por cercanía."
            } catch (error: Exception) {
                nearbyEnabled = false
                locationMessage = error.message ?: "No se pudo obtener la ubicación. Revisa que esté activada."
            }
        }
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.any { it }) updateLocation()
        else locationMessage = "Permite el acceso a la ubicación para ordenar por cercanía."
    }

    val zones = listOf("Todas", "Laureles", "El Poblado", "Sabaneta", "Bello")
    val categories = listOf("Todas") + sampleRestaurants.map { it.category }.distinct()

    val matchingRestaurants = sampleRestaurants.filter { r ->
        (zone == "Todas" || r.location == zone) &&
                (category == "Todas" || r.category == category) &&
                (
                        query.isBlank() ||
                                r.name.contains(query, true) ||
                                r.category.contains(query, true) ||
                                r.location.contains(query, true) ||
                                r.products.any { it.name.contains(query, true) }
                        )
    }
    val filtered = if (nearbyEnabled && userLocation != null) {
        matchingRestaurants.sortedBy { restaurant ->
            distanceKm(userLocation!!.latitude, userLocation!!.longitude, restaurant.latitude, restaurant.longitude)
        }
    } else matchingRestaurants
    val distances = if (nearbyEnabled && userLocation != null) {
        filtered.associate { restaurant ->
            restaurant.id to distanceKm(userLocation!!.latitude, userLocation!!.longitude, restaurant.latitude, restaurant.longitude)
        }
    } else emptyMap()

    Scaffold(
        containerColor = appBackground(),
        bottomBar = {
            if (page == "home" || page == "saved") {
                NavigationBar(containerColor = appSurface()) {
                    NavigationBarItem(
                        selected = page == "home",
                        onClick = { page = "home" },
                        icon = { Text("⌂", fontSize = 22.sp) },
                        label = { Text("Explorar") }
                    )
                    NavigationBarItem(
                        selected = page == "saved",
                        onClick = { page = "saved" },
                        icon = { Text("♥", fontSize = 20.sp) },
                        label = { Text("Guardados") }
                    )
                    NavigationBarItem(
                        selected = page == "login",
                        onClick = { page = "login" },
                        icon = { Text("●", fontSize = 20.sp) },
                        label = { Text("Cuenta") }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (page) {
                "login" -> LoginScreen(
                    onContinue = {
                        coroutineScope.launch {
                            isSigningIn = true
                            message = ""
                            try {
                                pendingGoogleCredential = obtenerGoogleIdToken(context)
                                turnstileSiteKey = obtenerTurnstileSiteKey()
                                showCaptcha = true
                            } catch (error: Exception) {
                                message = error.message
                                    ?: "No se pudo iniciar sesión con Google."
                                pendingGoogleCredential = null
                            } finally {
                                isSigningIn = false
                            }
                        }
                    },
                    message = message,
                    onExplore = { page = "home" },
                    isLoading = isSigningIn,
                )

                "home" -> HomeScreen(
                    query = query,
                    onQuery = { query = it },
                    zone = zone,
                    onZone = { zone = it },
                    category = category,
                    onCategory = { category = it },
                    zones = zones,
                    categories = categories,
                    restaurants = filtered,
                    distances = distances,
                    nearbyEnabled = nearbyEnabled,
                    locationMessage = locationMessage,
                    darkMode = darkMode,
                    onToggleDarkMode = onToggleDarkMode,
                    onToggleNearby = {
                        if (nearbyEnabled) {
                            nearbyEnabled = false
                            locationMessage = ""
                        } else {
                            val allowed = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                            if (allowed) updateLocation()
                            else locationPermissionLauncher.launch(
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                            )
                        }
                    },
                    saved = saved,
                    onSave = { id ->
                        if (saved.contains(id)) saved.remove(id)
                        else saved.add(id)
                    },
                    onOpen = {
                        selected = it
                        page = "detail"
                    }
                )

                "saved" -> {
                    val favorites = sampleRestaurants.filter { saved.contains(it.id) }
                    RestaurantListScreen(
                        title = "Mis restaurantes",
                        subtitle = "Tus lugares favoritos, siempre a mano.",
                        restaurants = favorites,
                        saved = saved,
                        onSave = { id ->
                            if (saved.contains(id)) saved.remove(id)
                            else saved.add(id)
                        },
                        onOpen = {
                            selected = it
                            page = "detail"
                        }
                    )
                }

                "detail" -> selected?.let { restaurant ->
                    DetailScreen(
                        restaurant = restaurant,
                        isSaved = saved.contains(restaurant.id),
                        onSave = {
                            if (saved.contains(restaurant.id)) saved.remove(restaurant.id)
                            else saved.add(restaurant.id)
                        },
                        onBack = { page = "home" }
                    )
                }
            }

            if (showCaptcha && turnstileSiteKey != null) {
                TurnstileDialog(
                    siteKey = turnstileSiteKey!!,
                    onClose = {
                        showCaptcha = false
                        pendingGoogleCredential = null
                    },
                    onToken = { captchaToken ->
                        val googleCredential = pendingGoogleCredential
                        showCaptcha = false
                        pendingGoogleCredential = null
                        if (googleCredential != null) {
                            coroutineScope.launch {
                                isSigningIn = true
                                try {
                                    val response = iniciarSesionGoogle(
                                        googleCredential,
                                        captchaToken,
                                    )
                                    val accessToken = response.optString("access_token")
                                    if (accessToken.isBlank()) {
                                        throw IllegalStateException(
                                            "El backend no devolvió el token de sesión."
                                        )
                                    }
                                    context.getSharedPreferences(
                                        "foodly_auth",
                                        android.content.Context.MODE_PRIVATE,
                                    ).edit().putString("access_token", accessToken).apply()
                                    message = ""
                                    page = "home"
                                } catch (error: Exception) {
                                    message = error.message
                                        ?: "No se pudo completar el inicio de sesión."
                                } finally {
                                    isSigningIn = false
                                }
                            }
                        }
                    },
                )
            }
            }
        }
    }

@Composable
fun LoginScreen(
    onContinue: () -> Unit,
    message: String,
    onExplore: () -> Unit,
    isLoading: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appSurface())
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.foodly_logo),
            contentDescription = "Logo de Foodly",
            modifier = Modifier.size(132.dp).clip(RoundedCornerShape(28.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.height(24.dp))
        Text("foodly", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = appInk())
        Text(
            "Tu próxima experiencia gastronómica empieza aquí.",
            fontSize = 16.sp,
            color = appMuted()
        )

        Spacer(Modifier.height(36.dp))

        Button(
            onClick = onContinue,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = appInk())
        ) {
            Text(
                if (isLoading) "Conectando con Google..." else "G  Continuar con Google",
                fontWeight = FontWeight.SemiBold,
            )
        }

        if (message.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(message, color = Orange, fontSize = 12.sp)
        }

        Spacer(Modifier.height(20.dp))
        TextButton(onClick = onExplore) {
            Text("Explorar sin iniciar sesión", color = Orange)
        }

        Spacer(Modifier.height(28.dp))
        Text(
            "Descubre restaurantes locales, conoce sus platos y elige dónde ir.",
            color = appMuted(),
            fontSize = 13.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    query: String,
    onQuery: (String) -> Unit,
    zone: String,
    onZone: (String) -> Unit,
    category: String,
    onCategory: (String) -> Unit,
    zones: List<String>,
    categories: List<String>,
    restaurants: List<Restaurant>,
    distances: Map<Int, Double>,
    nearbyEnabled: Boolean,
    locationMessage: String,
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onToggleNearby: () -> Unit,
    saved: List<Int>,
    onSave: (Int) -> Unit,
    onOpen: (Restaurant) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 22.dp,
            bottom = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.foodly_logo),
                            contentDescription = "Logo de Foodly",
                            modifier = Modifier.size(46.dp).clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Foodly", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Orange)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("¿Qué se te antoja hoy?", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = appInk())
                    Text("Descubre sabores cerca de ti.", color = appMuted())
                }
                IconButton(onClick = onToggleDarkMode) {
                    Text(if (darkMode) "☀️" else "🌙", fontSize = 24.sp)
                }
            }
        }

        item {
            OutlinedTextField(
                value = query,
                onValueChange = onQuery,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Restaurante, plato o comida...") },
                leadingIcon = { Text("⌕", fontSize = 26.sp) }
            )
        }

        item {
            OutlinedButton(
                onClick = onToggleNearby,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Orange),
            ) {
                Text(if (nearbyEnabled) "📍 Desactivar ordenar por cercanía" else "📍 Mostrar restaurantes más cercanos")
            }
            if (locationMessage.isNotBlank()) {
                Text(locationMessage, color = appMuted(), fontSize = 12.sp)
            }
        }

        item {
            Text("Explorar por zona", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(zones) { item ->
                    FilterChip(
                        selected = zone == item,
                        onClick = { onZone(item) },
                        label = { Text(item) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Orange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        item {
            Text("Categorías", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { item ->
                    FilterChip(
                        selected = category == item,
                        onClick = { onCategory(item) },
                        label = { Text(item) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Orange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        item {
            Text("Lugares para descubrir", fontSize = 21.sp, fontWeight = FontWeight.Bold)
            Text("${restaurants.size} resultados", fontSize = 13.sp, color = appMuted())
        }

        if (restaurants.isEmpty()) {
            item {
                Text("No encontramos resultados. Prueba con otra búsqueda o ubicación.")
            }
        }

        items(restaurants, key = { it.id }) { restaurant ->
            RestaurantCard(
                restaurant = restaurant,
                isSaved = saved.contains(restaurant.id),
                onSave = { onSave(restaurant.id) },
                onClick = { onOpen(restaurant) },
                distanceKm = distances[restaurant.id],
            )
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    isSaved: Boolean,
    onSave: () -> Unit,
    onClick: () -> Unit,
    distanceKm: Double? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = appSurface()),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = restaurant.profileImage,
                    contentDescription = "Foto de ${restaurant.name}",
                    modifier = Modifier.fillMaxWidth().height(175.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                    color = appSurface().copy(alpha = 0.95f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        restaurant.location,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        color = appInk(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                TextButton(
                    onClick = onSave,
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(appSurface(), CircleShape)
                ) {
                    Text(if (isSaved) "♥" else "♡", color = Orange, fontSize = 23.sp)
                }
            }

            Column(Modifier.padding(15.dp)) {
                Text(restaurant.name, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Text(restaurant.category, color = Orange, fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                Text(restaurant.description, color = appMuted(), fontSize = 13.sp, maxLines = 2)
                Spacer(Modifier.height(8.dp))
                Text("📍 ${restaurant.location}", color = appInk(), fontSize = 12.sp)
                if (distanceKm != null) {
                    Text("A ${String.format(java.util.Locale.US, "%.1f", distanceKm)} km de ti", color = Orange, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(10.dp))
                Text("Productos destacados", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(restaurant.products.take(4)) { product ->
                        Column(Modifier.width(112.dp)) {
                            AsyncImage(
                                model = product.image,
                                contentDescription = product.name,
                                modifier = Modifier.size(112.dp).clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.height(5.dp))
                            Text(
                                product.name,
                                fontSize = 12.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                "${formatPrice(product.price)} aprox.",
                                color = Orange,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = onClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Ver restaurante", color = Orange, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RestaurantListScreen(
    title: String,
    subtitle: String,
    restaurants: List<Restaurant>,
    saved: List<Int>,
    onSave: (Int) -> Unit,
    onOpen: (Restaurant) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text(subtitle, color = appMuted())
        }
        if (restaurants.isEmpty()) {
            item { Text("Aún no tienes restaurantes guardados. Explora y guarda tus favoritos.") }
        }
        items(restaurants, key = { it.id }) { restaurant ->
            RestaurantCard(
                restaurant = restaurant,
                isSaved = saved.contains(restaurant.id),
                onSave = { onSave(restaurant.id) },
                onClick = { onOpen(restaurant) }
            )
        }
    }
}

@Composable
fun DetailScreen(
    restaurant: Restaurant,
    isSaved: Boolean,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box {
                AsyncImage(
                    model = restaurant.profileImage,
                    contentDescription = "Foto de ${restaurant.name}",
                    modifier = Modifier.fillMaxWidth().height(260.dp),
                    contentScale = ContentScale.Crop
                )
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.padding(12.dp).background(appSurface(), CircleShape)
                ) {
                    Text("← Volver", color = appInk())
                }
            }
        }

        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(restaurant.name, fontSize = 29.sp, fontWeight = FontWeight.ExtraBold)
                Text(restaurant.category, color = Orange, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Text(restaurant.description, color = appMuted())
                Spacer(Modifier.height(8.dp))
                Text("📍 ${restaurant.address}", color = appInk())
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaved) appInk() else Orange
                    )
                ) {
                    Text(if (isSaved) "♥ Restaurante guardado" else "♡ Guardar restaurante")
                }

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        val url = Uri.parse(
                            "https://www.google.com/maps/search/?api=1&query=" +
                                    Uri.encode(restaurant.address)
                        )
                        context.startActivity(Intent(Intent.ACTION_VIEW, url))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("↗  Cómo llegar con Google Maps", color = appInk())
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        val url = Uri.parse(
                            "https://wa.me/${restaurant.phone}?text=" +
                                    Uri.encode("Hola, encontré ${restaurant.name} en Foodly y quisiera más información.")
                        )
                        context.startActivity(Intent(Intent.ACTION_VIEW, url))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF168B55))
                ) {
                    Text("Contactar por WhatsApp")
                }

                Spacer(Modifier.height(12.dp))
                Text("Nuestros productos", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Conoce algunas de sus propuestas gastronómicas.", color = appMuted())
            }
        }

        items(restaurant.products, key = { it.name }) { product ->
            Column(Modifier.padding(horizontal = 20.dp)) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxWidth().height(210.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(7.dp))
                Text(product.name, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Text("${formatPrice(product.price)} COP · precio de muestra", color = Orange, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun formatPrice(value: Int): String =
    java.text.NumberFormat.getCurrencyInstance(java.util.Locale("es", "CO"))
        .format(value)

private suspend fun getCurrentLocation(context: Context): Location {
    val fineGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarseGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    if (!fineGranted && !coarseGranted) throw SecurityException("Se necesita permiso de ubicación.")

    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val providers = manager.getProviders(true).filter {
        it == LocationManager.GPS_PROVIDER || it == LocationManager.NETWORK_PROVIDER
    }
    if (providers.isEmpty()) throw IllegalStateException("Activa la ubicación del teléfono e inténtalo de nuevo.")

    val lastKnown = providers.mapNotNull { provider ->
        try { manager.getLastKnownLocation(provider) } catch (_: SecurityException) { null }
    }.maxByOrNull { it.time }
    if (lastKnown != null && System.currentTimeMillis() - lastKnown.time < 120_000) return lastKnown

    return withTimeout(20_000) {
        suspendCancellableCoroutine { continuation ->
            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    manager.removeUpdates(this)
                    if (continuation.isActive) continuation.resume(location)
                }
            }
            try {
                providers.forEach { provider ->
                    manager.requestLocationUpdates(provider, 0L, 0f, listener, Looper.getMainLooper())
                }
            } catch (error: Exception) {
                if (continuation.isActive) continuation.resumeWith(Result.failure(error))
            }
            continuation.invokeOnCancellation { manager.removeUpdates(listener) }
        }
    }
}

private fun distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadiusKm = 6_371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
        kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
        kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
    return earthRadiusKm * 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
}
