// ==========================================
// LÓGICA GENERAL Y NAVEGACIÓN DE PANTALLAS
// ==========================================

function mostrarPantalla(idPantalla) {
    const pantallas = document.querySelectorAll('.pantalla');
    pantallas.forEach(p => p.classList.add('oculto'));

    const pantallaObjetivo = document.getElementById(idPantalla);
    if (pantallaObjetivo) {
        pantallaObjetivo.classList.remove('oculto');
    }
}

function mostrarSeccion(idSeccion) {
    const secciones = document.querySelectorAll('.seccion-home');
    secciones.forEach(s => s.classList.add('oculto'));

    const seccionObjetivo = document.getElementById(`seccion-${idSeccion}`);
    if (seccionObjetivo) {
        seccionObjetivo.classList.remove('oculto');
    }
}

function entrarDirectamente() {
    mostrarPantalla('pantalla-home');
    mostrarSeccion('inicio');
}

function cerrarSesion() {
    mostrarPantalla('pantalla-bienvenida');
}

// ==========================================
// REDIRECCIÓN DINÁMICA A WHATSAPP
// ==========================================

function abrirWhatsApp(numeroTelefono, nombreRestaurante) {
    if (!numeroTelefono || numeroTelefono === '-') {
        alert("Este establecimiento no cuenta con un número de WhatsApp disponible.");
        return;
    }
    
    // Limpiar caracteres no numéricos
    const telefonoLimpio = numeroTelefono.replace(/\D/g, '');
    const mensaje = encodeURIComponent(`¡Hola! Quisiera realizar una consulta/pedido para ${nombreRestaurante}.`);
    const url = `https://wa.me/${telefonoLimpio}?text=${mensaje}`;
    
    window.open(url, '_blank');
}

let telefonoDetalleActual = '';
let nombreDetalleActual = '';

function abrirWhatsAppDesdeDetalle() {
    abrirWhatsApp(telefonoDetalleActual, nombreDetalleActual);
}

// ==========================================
// VISTA Y DETALLE DE RESTAURANTES
// ==========================================

const restaurantesBD = {
    'Sabor Paisa': {
        nombre: 'Sabor Paisa',
        descripcion: 'Sabores tradicionales paisas y comida rápida para disfrutar en cualquier momento.',
        calificacion: '4.8',
        ubicacion: 'Bello, Antioquia',
        telefono: '573001234567',
        icono: '🇨🇴'
    },
    'La Parrilla del Valle': {
        nombre: 'La Parrilla del Valle',
        descripcion: 'Carnes a la parrilla con preparaciones especiales y sabores para compartir.',
        calificacion: '4.6',
        ubicacion: 'Medellín, Antioquia',
        telefono: '573009876543',
        icono: '🥩'
    },
    'Burger House': {
        nombre: 'Burger House',
        descripcion: 'Hamburguesas artesanales y papas hechas en casa para los amantes de la comida rápida.',
        calificacion: '4.7',
        ubicacion: 'Bello, Antioquia',
        telefono: '573001112233',
        icono: '🍔'
    },
    'México en la Esquina': {
        nombre: 'México en la Esquina',
        descripcion: 'Tacos y sabores mexicanos para disfrutar en cualquier momento.',
        calificacion: '4.5',
        ubicacion: 'Copacabana, Antioquia',
        telefono: '573004445566',
        icono: '🌮'
    }
};

function verRestaurante(nombre) {
    const datos = restaurantesBD[nombre];
    if (!datos) return;

    document.getElementById('detalle-nombre').innerText = datos.nombre;
    document.getElementById('detalle-descripcion').innerText = datos.descripcion;
    document.getElementById('detalle-calificacion').innerText = datos.calificacion;
    document.getElementById('detalle-ubicacion').innerText = datos.ubicacion;
    document.getElementById('detalle-telefono').innerText = datos.telefono;
    document.getElementById('detalle-icono').innerText = datos.icono;

    telefonoDetalleActual = datos.telefono;
    nombreDetalleActual = datos.nombre;

    mostrarSeccion('detalle-restaurante');
}

function volverAListaRestaurantes() {
    mostrarSeccion('inicio');
}

// ==========================================
// FORMULARIOS CON RECAPTCHA Y GOOGLE
// ==========================================

function iniciarSesion(event) {
    event.preventDefault();

    const captchaToken = grecaptcha.getResponse(0);
    if (!captchaToken) {
        alert("Por favor verifica el recuadro 'No soy un robot' antes de continuar.");
        return;
    }

    const correo = document.getElementById('login-correo').value;
    const contrasena = document.getElementById('login-contrasena').value;

    fetch('/usuarios/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ correo, contrasena, captchaToken })
    })
    .then(res => res.json())
    .then(data => {
        alert("¡Bienvenido a Foodly!");
        entrarDirectamente();
    })
    .catch(err => {
        // Simulación por si no hay conexión backend actual
        entrarDirectamente();
    });
}

function registrarUsuario(event) {
    event.preventDefault();

    const captchaToken = grecaptcha.getResponse(1);
    if (!captchaToken) {
        alert("Por favor verifica el recuadro 'No soy un robot' antes de continuar.");
        return;
    }

    alert("¡Cuenta creada exitosamente!");
    entrarDirectamente();
}

// CALLBACK OFICIAL DE GOOGLE SIGN-IN
function handleGoogleLogin(response) {
    const googleToken = response.credential;

    fetch('/usuarios/google-login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ token: googleToken })
    })
    .then(res => res.json())
    .then(data => {
        alert("Sesión iniciada con Google correctamente.");
        entrarDirectamente();
    })
    .catch(err => {
        // Fallback visual si el backend aún no procesa el token
        entrarDirectamente();
    });
}

// BUSCADOR Y CATEGORÍAS
function filtrarCategoria(cat, elemento) {
    document.querySelectorAll('.categoria').forEach(b => b.classList.remove('categoria-activa'));
    elemento.classList.add('categoria-activa');

    const posts = document.querySelectorAll('#seccion-inicio .post-restaurante');
    posts.forEach(post => {
        if (cat === 'todos' || post.dataset.categoria === cat) {
            post.style.display = 'block';
        } else {
            post.style.display = 'none';
        }
    });
}

function guardarRestaurante(nombre, boton) {
    boton.innerText = "Guardado ✓";
    boton.disabled = true;
}

function guardarDesdeDetalle() {
    alert("Restaurante guardado en tus favoritos.");
}

function cambiarUbicacion() {
    alert("Cambiando ubicación...");
}