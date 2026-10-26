// BASE DE DATOS LOCAL DE RESPALDO (FALLBACK EN CASO DE QUE LA API NO RESPONDA)
const restaurantesBD = {
    'Sabor Paisa': {
        id: 'Sabor Paisa',
        nombre: 'Sabor Paisa',
        categoria: 'Colombiana',
        categoriaKey: 'colombiana',
        descripcion: 'Sabores tradicionales paisas y comida típica preparada con sazón casera e ingredientes frescos.',
        calificacion: '4.8',
        ubicacion: 'Bello, Antioquia',
        direccion: 'Calle 50 #48-20, Parque de Bello',
        telefono: '+57 (604) 456-7890',
        whatsapp: '573001234567',
        avatarText: 'SP',
        fotoPerfil: 'imagenes/sabor-paisa-logo.jpg',
        imagenFondo: 'imagenes/sabor-paisa.jpg',
        platos: [
            { nombre: 'Bandeja Paisa Tradicional', precio: '$32.000', desc: 'Chicharrón crocante, carne molida, huevo frito, tajada de plátano, arepa y frijoles.' },
            { nombre: 'Sancocho Trifásico', precio: '$28.000', desc: 'Con carne de res, cerdo y pollo, acompañado de arroz, aguacate y arepa.' },
            { nombre: 'Cazuela de Frijoles', precio: '$25.000', desc: 'Servida con chicharrón picado, plátano maduro en cubos, maicitos y aguacate.' }
        ]
    },
    'La Parrilla del Valle': {
        id: 'La Parrilla del Valle',
        nombre: 'La Parrilla del Valle',
        categoria: 'Parrilla',
        categoriaKey: 'parrilla',
        descripcion: 'Cortes de carne premium madurados a la perfección y asados al carbón con sazón única.',
        calificacion: '4.6',
        ubicacion: 'Medellín, Antioquia',
        direccion: 'Cra 43A #1-50, El Poblado',
        telefono: '+57 (604) 312-4567',
        whatsapp: '573009876543',
        avatarText: 'PV',
        fotoPerfil: 'imagenes/parrilla-logo.jpg',
        imagenFondo: 'imagenes/parrilla.jpg',
        platos: [
            { nombre: 'Punta de Anca (350g)', precio: '$42.000', desc: 'Corte jugoso a la parrilla acompañado de papas criollas y guacamole.' },
            { nombre: 'Baby Beef Lomo Tierno', precio: '$45.000', desc: 'Acompañado de ensalada fresca de la casa y chimichurri especial.' },
            { nombre: 'Choripán Artesanal', precio: '$18.000', desc: 'Chorizo de la casa en pan baguette con queso fundido y salsa chimichurri.' }
        ]
    },
    'Burger House': {
        id: 'Burger House',
        nombre: 'Burger House',
        categoria: 'Hamburguesas',
        categoriaKey: 'hamburguesas',
        descripcion: 'Hamburguesas artesanales elaboradas con carne 100% de res, pan brioche horneado a diario y salsas de la casa.',
        calificacion: '4.7',
        ubicacion: 'Bello, Antioquia',
        direccion: 'Diag 55 #34-10, Niquía',
        telefono: '+57 (604) 234-5678',
        whatsapp: '573001112233',
        avatarText: 'BH',
        fotoPerfil: 'imagenes/burger-logo.jpg',
        imagenFondo: 'imagenes/burger.jpg',
        platos: [
            { nombre: 'Burger Doble Queso & Tocineta', precio: '$26.000', desc: '200g de carne, queso cheddar doble, tocineta ahumada y salsa BBQ.' },
            { nombre: 'Crispy Chicken Burger', precio: '$24.000', desc: 'Pechuga de pollo crujiente, pepinillos dulces y mayonesa de ajo.' },
            { nombre: 'Papas Especiales House', precio: '$16.000', desc: 'Papas a la francesa bañadas en queso fundido, tocineta y ciboulette.' }
        ]
    },
    'México en la Esquina': {
        id: 'México en la Esquina',
        nombre: 'México en la Esquina',
        categoria: 'Mexicana',
        categoriaKey: 'mexicana',
        descripcion: 'El auténtico sabor mexicano: tacos al pastor, quesabirrias, nachos artesanales y margaritas tradicionales.',
        calificacion: '4.5',
        ubicacion: 'Copacabana, Antioquia',
        direccion: 'Cl 50 #51-12, Centro',
        telefono: '+57 (604) 567-8901',
        whatsapp: '573004445566',
        avatarText: 'ME',
        fotoPerfil: 'imagenes/mexico-logo.jpg',
        imagenFondo: 'imagenes/mexico.jpg',
        platos: [
            { nombre: 'Tacos al Pastor (3 Unidades)', precio: '$22.000', desc: 'Carne de cerdo marinada con piña, cilantro fresco y cebolla picada.' },
            { nombre: 'Quesabirrias con Consomé', precio: '$27.000', desc: '3 tortillas con queso fundido y carne desmechada para sumergir.' },
            { nombre: 'Nachos Supremos', precio: '$25.000', desc: 'Tortillas de maíz con guacamole, frijol refrito, pico de gallo y jalapeños.' }
        ]
    }
};

let guardados = [];
let likes = {};
let telefonoDetalleActual = '';
let nombreDetalleActual = '';
let restaurantesCargadosAPI = [];

// CONFIGURACIÓN DE MICROSOFT ENTRA ID (MSAL.js)
const msalConfig = {
    auth: {
        clientId: "00000000-0000-0000-0000-000000000000",
        authority: "https://login.microsoftonline.com/common",
        redirectUri: window.location.origin
    }
};

let msalInstance = null;
if (typeof msal !== 'undefined') {
    msalInstance = new msal.PublicClientApplication(msalConfig);
}

// NAVEGACIÓN ENTRE PANTALLAS
function mostrarPantalla(idPantalla) {
    document.querySelectorAll('.pantalla').forEach(p => p.classList.add('oculto'));
    const destino = document.getElementById(idPantalla);
    if (destino) destino.classList.remove('oculto');
}

function mostrarSeccion(idSeccion) {
    document.querySelectorAll('.seccion-home').forEach(s => s.classList.add('oculto'));
    const destino = document.getElementById(`seccion-${idSeccion}`);
    if (destino) destino.classList.remove('oculto');
}

function irAlInicio() {
    mostrarSeccion('inicio');
}

function cerrarSesion() {
    mostrarPantalla('pantalla-login');
}

// RENDERIZAR FEED DESDE API NOSQL (NESTJS / MONGODB)
async function renderizarFeed() {
    const contenedor = document.getElementById('feed-restaurantes');
    const contenedorSeccion = document.getElementById('lista-restaurantes-seccion');
    if (!contenedor) return;

    try {
        const respuesta = await fetch('/api/establecimientos');
        const listaRestaurantes = await respuesta.json();

        // Si la base de datos NoSQL devuelve registros los usamos, de lo contrario usamos la base local de respaldo
        const datos = (Array.isArray(listaRestaurantes) && listaRestaurantes.length > 0)
            ? listaRestaurantes
            : Object.values(restaurantesBD);

        restaurantesCargadosAPI = datos;

        let html = '';
        datos.forEach(r => {
            const idRest = r._id || r.id;
            const isLiked = likes[idRest];
            const isSaved = guardados.includes(idRest);
            const catKey = r.categoriaKey || (r.categoria ? r.categoria.toLowerCase() : 'todos');

            html += `
                <article class="post-restaurante" data-categoria="${catKey}">
                    <div class="post-cabecera">
                        <div class="avatar-restaurante-wrapper">
                            <img src="${r.fotoPerfil || ''}" class="avatar-img" alt="${r.nombre}" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                            <div class="avatar-restaurante-fallback" style="display:none;">${r.avatarText || 'FD'}</div>
                        </div>
                        <div class="info-restaurante">
                            <strong>${r.nombre}</strong>
                            <span>📍 ${r.ubicacion}</span>
                        </div>
                    </div>

                    <div class="imagen-comida">
                        <img src="${r.imagenFondo || ''}" alt="${r.nombre}" onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=600'">
                    </div>

                    <div class="acciones-post">
                        <button class="btn-icon ${isLiked ? 'liked' : ''}" onclick="toggleLike('${idRest}', this)" title="Me gusta">
                            <svg viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg>
                        </button>
                        <button class="btn-icon" onclick="abrirComentario('${r.nombre}')" title="Comentar">
                            <svg viewBox="0 0 24 24"><path d="M21.99 4c0-1.1-.89-2-1.99-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4-.01-18z"/></svg>
                        </button>
                        <button class="btn-icon" onclick="compartirPost('${r.nombre}')" title="Compartir">
                            <svg viewBox="0 0 24 24"><path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/></svg>
                        </button>
                        <button class="btn-icon btn-guardar-ig ${isSaved ? 'saved' : ''}" onclick="toggleGuardar('${idRest}', this)" title="Guardar">
                            <svg viewBox="0 0 24 24"><path d="M17 3H7c-1.1 0-1.99.9-1.99 2L5 21l7-3 7 3V5c0-1.1-.9-2-2-2z"/></svg>
                        </button>
                    </div>

                    <div class="contenido-post">
                        <div class="calificacion-post">⭐ ${r.calificacion}</div>
                        <h2>${r.nombre}</h2>
                        <p>${r.descripcion}</p>
                        <div class="grupo-acciones-card">
                            <button class="boton-principal" onclick="verRestaurante('${idRest}')">Ver restaurante →</button>
                            <button class="boton-whatsapp" onclick="abrirWhatsApp('${r.whatsapp}', '${r.nombre}')">💬 WhatsApp</button>
                        </div>
                    </div>
                </article>
            `;
        });

        contenedor.innerHTML = html;
        if (contenedorSeccion) contenedorSeccion.innerHTML = html;

    } catch (error) {
        console.warn("API NestJS no disponible, usando fallback local:", error);
        restaurantesCargadosAPI = Object.values(restaurantesBD);
        let htmlFallback = '';
        Object.values(restaurantesBD).forEach(r => {
            htmlFallback += `
                <article class="post-restaurante" data-categoria="${r.categoriaKey}">
                    <div class="contenido-post" style="padding-top:16px;">
                        <h2>${r.nombre}</h2>
                        <p>${r.descripcion}</p>
                        <button class="boton-principal" onclick="verRestaurante('${r.id}')">Ver restaurante →</button>
                    </div>
                </article>
            `;
        });
        contenedor.innerHTML = htmlFallback;
    }
}

function toggleLike(id, btn) {
    likes[id] = !likes[id];
    btn.classList.toggle('liked', likes[id]);
}

function abrirComentario(nombre) {
    alert(`Comentarios para ${nombre}: ¡Escribe tu opinión o reseña!`);
}

function compartirPost(nombre) {
    alert(`¡Enlace para compartir de ${nombre} copiado al portapapeles!`);
}

function toggleGuardar(id, btn) {
    const idx = guardados.indexOf(id);
    if (idx > -1) {
        guardados.splice(idx, 1);
        if (btn) btn.classList.remove('saved');
    } else {
        guardados.push(id);
        if (btn) btn.classList.add('saved');
    }
    renderizarGuardados();
}

function renderizarGuardados() {
    const contenedor = document.getElementById('lista-guardados');
    if (!contenedor) return;

    if (guardados.length === 0) {
        contenedor.innerHTML = '<p class="texto-vacio">No has guardado ningún restaurante aún.</p>';
        return;
    }

    let html = '';
    guardados.forEach(id => {
        const r = restaurantesCargadosAPI.find(item => (item._id === id || item.id === id)) || restaurantesBD[id];
        if (r) {
            html += `
                <article class="post-restaurante">
                    <div class="contenido-post" style="padding-top:16px;">
                        <h2>${r.nombre}</h2>
                        <p>${r.descripcion}</p>
                        <button class="boton-principal" onclick="verRestaurante('${r._id || r.id}')">Ver tarjeta →</button>
                    </div>
                </article>
            `;
        }
    });
    contenedor.innerHTML = html;
}

// DETALLE DEL RESTAURANTE
function verRestaurante(id) {
    const r = restaurantesCargadosAPI.find(item => (item._id === id || item.id === id)) || restaurantesBD[id];
    if (!r) {
        alert("Restaurante no encontrado.");
        return;
    }

    document.getElementById('detalle-nombre').innerText = r.nombre;
    document.getElementById('detalle-categoria').innerText = r.categoria;
    document.getElementById('detalle-descripcion').innerText = r.descripcion;
    document.getElementById('detalle-calificacion').innerText = `⭐ ${r.calificacion}`;
    document.getElementById('detalle-ubicacion').innerText = r.ubicacion;
    document.getElementById('detalle-direccion').innerText = r.direccion;
    document.getElementById('detalle-telefono').innerText = r.telefono;
    document.getElementById('detalle-whatsapp').innerText = `+${r.whatsapp}`;

    const logoImg = document.getElementById('detalle-logo-img');
    const logoText = document.getElementById('detalle-avatar-text');
    logoImg.style.display = 'block';
    logoText.style.display = 'none';
    logoImg.src = r.fotoPerfil || '';
    logoText.innerText = r.avatarText || 'FD';

    const banner = document.getElementById('detalle-banner');
    banner.src = r.imagenFondo || '';
    banner.onerror = function() {
        this.src = 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800';
    };

    const contenedorPlatos = document.getElementById('detalle-platos');
    let htmlPlatos = '';
    if (r.platos && r.platos.length > 0) {
        r.platos.forEach(p => {
            htmlPlatos += `
                <div class="plato-card">
                    <div class="plato-header">
                        <strong>${p.nombre}</strong>
                        <span class="plato-precio">${p.precio}</span>
                    </div>
                    <p>${p.desc}</p>
                </div>
            `;
        });
    }
    contenedorPlatos.innerHTML = htmlPlatos;

    telefonoDetalleActual = r.whatsapp;
    nombreDetalleActual = r.nombre;

    mostrarSeccion('detalle-restaurante');
}

function volverAListaRestaurantes() {
    mostrarSeccion('inicio');
}

// WHATSAPP
function abrirWhatsApp(numero, nombre) {
    if (!numero) {
        alert("Este restaurante no tiene WhatsApp disponible.");
        return;
    }
    const numLimpio = numero.replace(/\D/g, '');
    const msg = encodeURIComponent(`¡Hola! Quisiera más información o hacer un pedido en ${nombre}.`);
    window.open(`https://wa.me/${numLimpio}?text=${msg}`, '_blank');
}

function abrirWhatsAppDesdeDetalle() {
    abrirWhatsApp(telefonoDetalleActual, nombreDetalleActual);
}

// AUTENTICACIÓN LOCAL / CAPTCHA / SSO
async function iniciarSesion(event) {
    event.preventDefault();

    const captchaRes = grecaptcha.getResponse(0);
    if (!captchaRes) {
        alert("⚠️ Completa la casilla 'No soy un robot' para continuar.");
        return;
    }

    const correo = document.getElementById('login-correo').value;
    const contrasena = document.getElementById('login-contrasena').value;

    try {
        // Petición al endpoint NestJS de autenticación
        const res = await fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ correo, contrasena })
        });
        const data = await res.json();

        if (data.mfaRequired) {
            const mfaCode = prompt(`${data.message}\n\nIngresa el código MFA (Usa 123456):`);
            if (!mfaCode) return;

            const resMfa = await fetch('/auth/mfa/verify', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ mfaSessionToken: data.mfaSessionToken, code: mfaCode })
            });
            const dataMfa = await resMfa.json();

            if (dataMfa.access_token) {
                localStorage.setItem('jwt_token', dataMfa.access_token);
                alert("¡Autenticación exitosa! Token JWT emitido.");
            }
        }
    } catch (err) {
        console.warn("Backend local offline, continuando en modo cliente:", err);
    }

    mostrarPantalla('pantalla-home');
    mostrarSeccion('inicio');
    renderizarFeed();
}

function registrarUsuario(event) {
    event.preventDefault();

    const captchaRes = grecaptcha.getResponse(1);
    if (!captchaRes) {
        alert("⚠️ Completa la casilla 'No soy un robot' para continuar.");
        return;
    }

    alert("¡Cuenta registrada con éxito!");
    mostrarPantalla('pantalla-home');
    mostrarSeccion('inicio');
    renderizarFeed();
}

function handleGoogleLogin(response) {
    mostrarPantalla('pantalla-home');
    mostrarSeccion('inicio');
    renderizarFeed();
}

async function iniciarSesionMicrosoft() {
    try {
        if (!msalInstance) {
            alert("¡Autenticación con Microsoft SSO exitosa (Modo Demo)!");
            mostrarPantalla('pantalla-home');
            mostrarSeccion('inicio');
            renderizarFeed();
            return;
        }

        const loginResponse = await msalInstance.loginPopup({
            scopes: ["User.Read", "email"]
        });

        alert(`¡Bienvenido ${loginResponse.account.name || 'Usuario'} vía Microsoft!`);
        mostrarPantalla('pantalla-home');
        mostrarSeccion('inicio');
        renderizarFeed();
    } catch (error) {
        console.error("Error al iniciar sesión con Microsoft:", error);
        mostrarPantalla('pantalla-home');
        mostrarSeccion('inicio');
        renderizarFeed();
    }
}

// FILTROS Y BUSCADOR
function filtrarCategoria(cat, btn) {
    document.querySelectorAll('.categoria').forEach(b => b.classList.remove('categoria-activa'));
    btn.classList.add('categoria-activa');

    document.querySelectorAll('#feed-restaurantes .post-restaurante').forEach(post => {
        if (cat === 'todos' || post.dataset.categoria === cat) {
            post.style.display = 'block';
        } else {
            post.style.display = 'none';
        }
    });
}

function buscarRestaurantes() {
    const query = document.getElementById('buscador-restaurantes').value.toLowerCase();
    document.querySelectorAll('#feed-restaurantes .post-restaurante').forEach(post => {
        const texto = post.innerText.toLowerCase();
        post.style.display = texto.includes(query) ? 'block' : 'none';
    });
}

function guardarDesdeDetalle() {
    alert("Restaurante guardado en tus favoritos.");
}

function cambiarUbicacion() {
    alert("Ubicación actual: Bello, Antioquia.");
}

// INICIALIZACIÓN
window.addEventListener('DOMContentLoaded', () => {
    mostrarPantalla('pantalla-login');
});