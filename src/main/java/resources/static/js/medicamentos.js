// ===============================================
// CONFIGURAÇÃO
// ===============================================
const API_URL = "http://localhost:8080/api/medicamentos";

// ===============================================
// FUNÇÃO PRINCIPAL – LISTAR MEDICAMENTOS
// ===============================================
async function carregarMedicamentos(filtros = {}) {
  try {
    const resposta = await fetch(API_URL);
    if (!resposta.ok) throw new Error("Erro ao buscar medicamentos");

    const medicamentos = await resposta.json();

    renderizarMedicamentos(filtrarMedicamentos(medicamentos, filtros));
  } catch (erro) {
    console.error("Erro ao carregar medicamentos:", erro);
    document.getElementById("medicamentosGrid").innerHTML = `
      <div class="col-12 text-center text-danger">
        Ocorreu um erro ao carregar os medicamentos.
      </div>`;
  }
}

// ===============================================
// FUNÇÃO – RENDERIZAR CARDS
// ===============================================
function renderizarMedicamentos(lista) {
  const grid = document.getElementById("medicamentosGrid");
  grid.innerHTML = "";

  if (lista.length === 0) {
    grid.innerHTML = `<div class="col-12 text-center text-muted">Nenhum medicamento encontrado.</div>`;
    return;
  }
  lista.forEach((m) => {
    const diasParaVencer = calcularDiasParaVencer(m.validade);
    const alerta = diasParaVencer <= 30 ? `<span class="badge-alerta">Vence em ${diasParaVencer} dias</span>` : "";

    const precoFormatado = m.preco ? `R$ ${parseFloat(m.preco).toFixed(2).replace('.', ',')}` : "—";

    const card = `
      <div class="col-12 col-sm-6 col-md-4 col-lg-3">
        <div class="card-medicamento shadow-sm fadeIn">
          <img src="${m.fotoUrl}" alt="${m.nome}">
          <div class="card-overlay"></div>
          ${alerta}
          <div class="card-body">
            <h5>${m.nome}</h5>
            <p><strong>Preço:</strong> ${precoFormatado}</p>
            <p><strong>Quantidade:</strong> ${m.quantidade ?? 0}</p>
            <p><strong>Validade:</strong> ${m.validade ?? "—"}</p>
            <button class="btn btn-sm btn-outline-primary mt-2" onclick="verDetalhes(${m.id})">
              <i class="bi bi-eye"></i> Ver Detalhes
            </button>
          </div>
        </div>
      </div>
    `;

    grid.innerHTML += card;
  });
}

// ===============================================
// FUNÇÃO – FILTRAR MEDICAMENTOS
// ===============================================
function filtrarMedicamentos(lista, filtros) {
  return lista.filter((m) => {
    const dias = calcularDiasParaVencer(m.validade);

    // Filtro por dias
    if (filtros.dias > 0 && dias > filtros.dias) return false;

    // Filtro por preço
    if (filtros.preco > 0 && m.preco > filtros.preco) return false;

    // Filtro por nome (busca textual)
    if (filtros.nome && !m.nome.toLowerCase().includes(filtros.nome.toLowerCase())) return false;

    return true;
  });
}

// ===============================================
// FUNÇÃO – CALCULAR DIAS ATÉ O VENCIMENTO
// ===============================================
function calcularDiasParaVencer(validadeStr) {
  if (!validadeStr) return 9999;
  const hoje = new Date();
  const validade = new Date(validadeStr);
  const diff = validade - hoje;
  return Math.ceil(diff / (1000 * 60 * 60 * 24));
}

// ===============================================
// EVENTOS – FILTROS E BUSCA
// ===============================================
document.addEventListener("DOMContentLoaded", () => {
  const filtroDias = document.getElementById("filtroDias");
  const filtroPreco = document.getElementById("filtroPreco");
  const campoBusca = document.querySelector("input[placeholder='Buscar medicamentos...']");
  const formBusca = campoBusca.closest("form");

  formBusca.addEventListener("submit", (e) => {
    e.preventDefault();
    const filtros = {
      dias: parseInt(filtroDias.value),
      preco: parseFloat(filtroPreco.value),
      nome: campoBusca.value.trim(),
    };
    carregarMedicamentos(filtros);
  });

  filtroDias.addEventListener("change", () =>
    carregarMedicamentos({
      dias: parseInt(filtroDias.value),
      preco: parseFloat(filtroPreco.value),
      nome: campoBusca.value.trim(),
    })
  );

  filtroPreco.addEventListener("change", () =>
    carregarMedicamentos({
      dias: parseInt(filtroDias.value),
      preco: parseFloat(filtroPreco.value),
      nome: campoBusca.value.trim(),
    })
  );

  carregarMedicamentos();
});

// ===============================================
// BOTÃO – VISUALIZAR DETALHES (placeholder futuro)
// ===============================================
function verDetalhes(id) {
  alert("Visualização detalhada em desenvolvimento (ID: " + id + ")");
}
