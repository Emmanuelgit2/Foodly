// Base de datos de restaurantes con imágenes locales
const restaurantesBD = [
  {
    id: "sabor-paisa",
    nombre: "Sabor Paisa",
    categoria: "Colombiana",
    ubicacion: "Bello, Antioquia",
    direccion: "Calle 50 #48-20, Parque de Bello",
    lat: 6.3317,
    lng: -75.5583,
    telefono: "+57 (604) 456-7890",
    whatsapp: "+573001234567",
    logo: "imagenes/saborpaisa-logo.jpg",
    portada: "imagenes/paisa.jpg",
    calificacion: 4.8,
    abierto: true,
    likesCount: 439,
    commentsCount: 6,
    repostsCount: 23,
    descripcion: "Sabores tradicionales paisas y comida típica preparada con sazón casera e ingredientes frescos.",
    platos: [
      { id: "p1", nombre: "Bandeja Paisa Tradicional", precio: "$32.000", desc: "Chicharrón crocante, carne molida, huevo frito, tajada de plátano, arepa y frijoles." },
      { id: "p2", nombre: "Sancocho Trifásico", precio: "$28.000", desc: "Con carne de res, cerdo y pollo, acompañado de arroz, aguacate y arepa." },
      { id: "p3", nombre: "Cazuela de Frijoles", precio: "$25.000", desc: "Servida con chicharrón picado, plátano maduro en cubos, maicitos y aguacate." }
    ]
  },
  {
    id: "burger-house",
    nombre: "Burger House",
    categoria: "Hamburguesas",
    ubicacion: "Bello, Antioquia",
    direccion: "Carrera 49 #52-10",
    lat: 6.3340,
    lng: -75.5560,
    telefono: "+57 (604) 456-1122",
    whatsapp: "+573009876543",
    logo: "imagenes/burguer-logo.jpg",
    portada: "imagenes/burguer.jpg",
    calificacion: 4.9,
    abierto: true,
    likesCount: 280,
    commentsCount: 12,
    repostsCount: 15,
    descripcion: "Hamburguesas artesanales de res 100% madurada, pan brioche horneado a diario y salsas de la casa.",
    platos: [
      { id: "b1", nombre: "Burger Royal Bacon", precio: "$29.000", desc: "Doble carne, tocino ahumado, queso cheddar fundido y cebolla caramelizada." },
      { id: "b2", nombre: "Burger Trufada", precio: "$31.000", desc: "Carne de res, queso suizo, mayo de trufa y rúgula fresca." }
    ]
  },
  {
    id: "parrilla-valle",
    nombre: "La Parrilla del Valle",
    categoria: "Parrilla",
    ubicacion: "Bello, Antioquia",
    direccion: "Calle 55 #45-12",
    lat: 6.3360,
    lng: -75.5520,
    telefono: "+57 (604) 456-3344",
    whatsapp: "+573012223344",
    logo: "imagenes/parrilla-logo.jpg",
    portada: "imagenes/parrilla.jpg",
    calificacion: 4.7,
    abierto: false,
    likesCount: 195,
    commentsCount: 4,
    repostsCount: 8,
    descripcion: "Cortes de carne premium a la parrilla, costillas ahumadas y chorizos artesanales al carbón.",
    platos: [
      { id: "v1", nombre: "Tomahawk Angus (800g)", precio: "$85.000", desc: "Corte grueso a la parrilla acompañado de papas criollas y chimichurri." },
      { id: "v2", nombre: "Costillas BBQ Ahumadas", precio: "$42.000", desc: "Bañadas en salsa BBQ artesanal con puré de papa." }
    ]
  },
  {
    id: "mexico-esquina",
    nombre: "México en la Esquina",
    categoria: "Mexicana",
    ubicacion: "Bello, Antioquia",
    direccion: "Calle 48 #51-30",
    lat: 6.3300,
    lng: -75.5600,
    telefono: "+57 (604) 456-5566",
    whatsapp: "+573023334455",
    logo: "imagenes/mexico-logo.jpg",
    portada: "imagenes/mexico.jpg",
    calificacion: 4.8,
    abierto: true,
    likesCount: 512,
    commentsCount: 18,
    repostsCount: 34,
    descripcion: "Auténticos tacos al pastor, quesabirrias con consomé y margaritas tradicionales mexicanas.",
    platos: [
      { id: "m1", nombre: "Tacos de Birria (3 uds)", precio: "$26.000", desc: "Tortilla de maíz artesanal, carne desmechada, queso fundido y consomé." },
      { id: "m2", nombre: "Tacos al Pastor", precio: "$24.000", desc: "Cerdo marinado con achiote, piña asada, cilantro y cebolla." }
    ]
  }
];

// Estado global y LocalStorage
let favoritos = JSON.parse(localStorage.getItem('foodly_favoritos')) || [];
let guardados = JSON.parse(localStorage.getItem('foodly_guardados')) || [];
let reposts = JSON.parse(localStorage.getItem('foodly_reposts')) || [];
let comentariosBD = JSON.parse(localStorage.getItem('foodly_comentarios')) || {};
let categoriaActual = "Todos";
let restauranteComentarioId = null;

// Inicialización
document.addEventListener('DOMContentLoaded', () => {
  verificarSesion();
  inicializarTema();
  renderizarTarjetas();
  configurarBuscadorYFiltros();
});

// Autenticación
function procesarLogin(e) {
  e.preventDefault();
  localStorage.setItem('foodly_usuario', 'usuario_activo');
  document.getElementById('pantalla-login').classList.add('hidden');
  document.getElementById('pantalla-app').classList.remove('hidden');
}

function loginSocial(proveedor) {
  localStorage.setItem('foodly_usuario', `usuario_${proveedor.toLowerCase()}`);
  document.getElementById('pantalla-login').classList.add('hidden');
  document.getElementById('pantalla-app').classList.remove('hidden');
}

function cerrarSesion() {
  localStorage.removeItem('foodly_usuario');
  document.getElementById('pantalla-app').classList.add('hidden');
  document.getElementById('pantalla-login').classList.remove('hidden');
}

function verificarSesion() {
  const usuario = localStorage.getItem('foodly_usuario');
  if (usuario) {
    document.getElementById('pantalla-login').classList.add('hidden');
    document.getElementById('pantalla-app').classList.remove('hidden');
  }
}

// Botón Inicio
function irAlInicio() {
  document.getElementById('buscador').value = '';
  categoriaActual = "Todos";
  document.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
  document.querySelector('.chip[data-categoria="Todos"]').classList.add('active');
  window.scrollTo({ top: 0, behavior: 'smooth' });
  renderizarTarjetas();
}

// Renderizar tarjetas en el Feed
function renderizarTarjetas() {
  const contenedor = document.getElementById('contenedor-restaurantes');
  const filtroTexto = document.getElementById('buscador').value.toLowerCase();

  const filtrados = restaurantesBD.filter(r => {
    const coincideCat = categoriaActual === "Todos" || r.categoria === categoriaActual;
    const coincideTxt = r.nombre.toLowerCase().includes(filtroTexto) || r.descripcion.toLowerCase().includes(filtroTexto);
    return coincideCat && coincideTxt;
  });

  contenedor.innerHTML = filtrados.map(r => {
    const esFav = favoritos.includes(r.id);
    const esGuardado = guardados.includes(r.id);
    const esRepost = reposts.includes(r.id);

    const likesNum = r.likesCount + (esFav ? 1 : 0);
    const repostsNum = r.repostsCount + (esRepost ? 1 : 0);
    const commentsNum = (comentariosBD[r.id] ? comentariosBD[r.id].length : r.commentsCount);

    return `
      <article class="card-restaurante">
        <div class="card-header">
          <img src="${r.logo}" alt="${r.nombre}" class="avatar-logo">
          <div class="header-info">
            <h3>${r.nombre}</h3>
            <p>📍 ${r.ubicacion}</p>
          </div>
        </div>

        <img src="${r.portada}" alt="Portada ${r.nombre}" class="card-banner">

        <!-- Barra de acciones estilo exacto de la imagen -->
        <div class="card-actions-bar">
          <div class="left-icons">
            <button class="action-item ${esFav ? 'liked' : ''}" onclick="toggleFavorito('${r.id}')" title="Me gusta">
              <svg viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg>
              <span>${likesNum}</span>
            </button>

            <button class="action-item" onclick="abrirModalComentarios('${r.id}')" title="Comentarios">
              <svg viewBox="0 0 24 24"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
              <span>${commentsNum}</span>
            </button>

            <button class="action-item ${esRepost ? 'reposted' : ''}" onclick="toggleRepost('${r.id}')" title="Repostear">
              <svg viewBox="0 0 24 24"><path d="M17 1l4 4-4 4"/><path d="M3 11V9a4 4 0 0 1 4-4h14"/><path d="M7 23l-4-4 4-4"/><path d="M21 13v2a4 4 0 0 1-4 4H3"/></svg>
              <span>${repostsNum}</span>
            </button>

            <button class="action-item" onclick="compartirRestaurante('${r.nombre}')" title="Enviar">
              <svg viewBox="0 0 24 24"><path d="M22 2L11 13"/><path d="M22 2l-7 20-4-9-9-4 20-7z"/></svg>
            </button>
          </div>

          <button class="action-item ${esGuardado ? 'saved' : ''}" onclick="toggleGuardado('${r.id}')" title="Guardar">
            <svg viewBox="0 0 24 24"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/></svg>
          </button>
        </div>

        <div class="card-body-info">
          <div class="badge-row">
            <span class="rating-badge">★ ${r.calificacion}</span>
            <span class="status-badge ${r.abierto ? 'open' : 'closed'}">
              ${r.abierto ? '🟢 Abierto ahora' : '🔴 Cerrado'}
            </span>
          </div>

          <p class="card-desc"><strong>${r.nombre}</strong> ${r.descripcion}</p>

          <div class="button-group">
            <button class="btn-primary" onclick="abrirModalDetalle('${r.id}')">Ver restaurante →</button>
            <a href="https://wa.me/${r.whatsapp.replace(/\+/g, '')}?text=Hola%20${encodeURIComponent(r.nombre)}" target="_blank" class="btn-whatsapp">
              📲 WhatsApp
            </a>
          </div>
        </div>
      </article>
    `;
  }).join('');
}

// Favoritos, Guardados, Reposts
function toggleFavorito(id) {
  favoritos = favoritos.includes(id) ? favoritos.filter(f => f !== id) : [...favoritos, id];
  localStorage.setItem('foodly_favoritos', JSON.stringify(favoritos));
  renderizarTarjetas();
}

function toggleGuardado(id) {
  guardados = guardados.includes(id) ? guardados.filter(g => g !== id) : [...guardados, id];
  localStorage.setItem('foodly_guardados', JSON.stringify(guardados));
  renderizarTarjetas();
}

function toggleRepost(id) {
  reposts = reposts.includes(id) ? reposts.filter(r => r !== id) : [...reposts, id];
  localStorage.setItem('foodly_reposts', JSON.stringify(reposts));
  renderizarTarjetas();
}

function compartirRestaurante(nombre) {
  if (navigator.share) {
    navigator.share({ title: 'Foodly', text: `¡Mira el restaurante ${nombre} en Foodly!`, url: window.location.href });
  } else {
    navigator.clipboard.writeText(window.location.href);
    alert(`Enlace de ${nombre} copiado al portapapeles.`);
  }
}

// Comentarios
function abrirModalComentarios(id) {
  restauranteComentarioId = id;
  const rest = restaurantesBD.find(r => r.id === id);
  document.getElementById('comentarios-titulo').textContent = `Comentarios - ${rest.nombre}`;
  renderizarComentarios();
  document.getElementById('modal-comentarios').classList.add('active');
}

function cerrarModalComentarios() {
  document.getElementById('modal-comentarios').classList.remove('active');
}

function renderizarComentarios() {
  const lista = document.getElementById('lista-comentarios');
  const items = comentariosBD[restauranteComentarioId] || ["¡Excelente servicio!", "Muy recomendada la comida."];
  lista.innerHTML = items.map(c => `<div class="comentario-item">💬 ${c}</div>`).join('');
}

function agregarComentario() {
  const input = document.getElementById('input-nuevo-comentario');
  if (!input.value.trim()) return;

  if (!comentariosBD[restauranteComentarioId]) {
    comentariosBD[restauranteComentarioId] = ["¡Excelente servicio!", "Muy recomendada la comida."];
  }
  comentariosBD[restauranteComentarioId].push(input.value.trim());
  localStorage.setItem('foodly_comentarios', JSON.stringify(comentariosBD));
  input.value = '';
  renderizarComentarios();
  renderizarTarjetas();
}

// Modo Oscuro
function inicializarTema() {
  const btnTheme = document.getElementById('btn-theme');
  const themeIcon = document.getElementById('theme-icon');
  const tema = localStorage.getItem('foodly_theme') || 'light';

  document.documentElement.setAttribute('data-theme', tema);
  themeIcon.textContent = tema === 'dark' ? '☯︎' : '☯︎';

  btnTheme.addEventListener('click', () => {
    const actual = document.documentElement.getAttribute('data-theme');
    const nuevo = actual === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', nuevo);
    localStorage.setItem('foodly_theme', nuevo);
    themeIcon.textContent = nuevo === 'dark' ? '☯︎' : '☯︎';
  });
}

// Filtros y Buscador
function configurarBuscadorYFiltros() {
  document.getElementById('buscador').addEventListener('input', renderizarTarjetas);
  document.querySelectorAll('.chip').forEach(chip => {
    chip.addEventListener('click', (e) => {
      document.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
      e.target.classList.add('active');
      categoriaActual = e.target.dataset.categoria;
      renderizarTarjetas();
    });
  });
  document.getElementById('btn-cerrar-modal').addEventListener('click', () => {
    document.getElementById('modal-detalle').classList.remove('active');
  });
}

// Modal de Detalle Estilo Tarjeta Completa
function abrirModalDetalle(id) {
  const r = restaurantesBD.find(item => item.id === id);
  if (!r) return;

  const esFav = favoritos.includes(r.id);
  const modalContenido = document.getElementById('modal-contenido');

  modalContenido.innerHTML = `
    <div class="modal-banner-container">
      <img src="${r.portada}" class="modal-banner" alt="Portada">
      <img src="${r.logo}" class="modal-logo-overlay" alt="Logo">
    </div>

    <div class="modal-body">
      <div class="modal-title-row">
        <h2>${r.nombre}</h2>
        <span class="rating-badge">⭐ ${r.calificacion}</span>
      </div>

      <span class="category-badge">${r.categoria}</span>

      <p style="margin-top: 12px; font-size: 0.9rem; color: var(--text-muted);">${r.descripcion}</p>

      <div class="info-box">
        <p>📍 <strong>Ubicación:</strong> ${r.ubicacion}</p>
        <p>🏠 <strong>Dirección:</strong> ${r.direccion}</p>
        <button class="btn-mapa" onclick="window.open('https://www.google.com/maps/search/?api=1&query=${r.lat},${r.lng}', '_blank')">
          🗺️ Cómo llegar (Abrir Mapa)
        </button>
        <p style="margin-top: 8px;">📞 <strong>Teléfono:</strong> ${r.telefono}</p>
        <p>💬 <strong>WhatsApp:</strong> ${r.whatsapp}</p>
      </div>

      <div class="menu-section">
        <h4>🍽️ Platos Principales</h4>
        ${r.platos.map(p => `
          <div class="menu-item-card">
            <div class="menu-item-header">
              <span class="menu-item-title">${p.nombre}</span>
              <span class="menu-item-price">${p.precio}</span>
            </div>
            <p class="menu-item-desc">${p.desc}</p>
          </div>
        `).join('')}
      </div>

      <div class="modal-action-btns">
        <button class="btn-fav-modal" onclick="toggleFavoritoModal('${r.id}')" id="btn-modal-fav">
          ${esFav ? '❤️ Guardado' : '♡ Guardar en Favoritos'}
        </button>

        <a href="https://wa.me/${r.whatsapp.replace(/\+/g, '')}?text=Hola%20${encodeURIComponent(r.nombre)},%20quisiera%20hacer%20un%20pedido." target="_blank" class="btn-ws-modal">
          <span>Contactar por</span>
          <strong>WhatsApp</strong>
        </a>
      </div>
    </div>
  `;

  document.getElementById('modal-detalle').classList.add('active');
}

function toggleFavoritoModal(id) {
  toggleFavorito(id);
  const esFav = favoritos.includes(id);
  const btn = document.getElementById('btn-modal-fav');
  if (btn) {
    btn.innerText = esFav ? '❤️ Guardado' : '♡ Guardar en Favoritos';
  }
}