document.addEventListener('DOMContentLoaded', async () => {
  const instituicaoLogada = JSON.parse(localStorage.getItem('usuarioLogado'));
  if (!instituicaoLogada) {
    window.location.href = "login.html";
    return;
  }

  // Logout
  const btnLogout = document.getElementById('btnLogout');
  if (btnLogout) {
    btnLogout.addEventListener('click', () => {
      localStorage.removeItem('usuarioLogado');
      window.location.href = 'login.html';
    });
  }

  const container = document.getElementById('medicamentosContainer');
  const editarModal = new bootstrap.Modal(document.getElementById('editarModal'));
  const excluirModal = new bootstrap.Modal(document.getElementById('excluirModal'));
  const adicionarModal = new bootstrap.Modal(document.getElementById('adicionarModal'));
  let medicamentoSelecionado = null;

  // Função para exibir os medicamentos
  async function carregarMedicamentos() {
    try {
      const response = await fetch(`http://localhost:8080/api/instituicao/medicamentos/${instituicaoLogada.id}`);
      if (!response.ok) throw new Error('Erro ao buscar medicamentos.');

      const medicamentos = await response.json();
      container.innerHTML = '';

      if (medicamentos.length === 0) {
        container.innerHTML = '<p class="text-center mt-4">Nenhum medicamento cadastrado.</p>';
      } else {
        medicamentos.forEach(med => {
          const card = document.createElement('div');
          card.className = 'card med-card shadow-sm';
          card.innerHTML = `
            <div class="icon-actions">
              <i class="bi bi-pencil-fill text-warning" data-id="${med.id}" title="Editar"></i>
              <i class="bi bi-trash-fill text-danger" data-id="${med.id}" title="Excluir"></i>
            </div>
            <img src="${med.fotoUrl || '/images/default.png'}" class="card-img-top" alt="${med.nome}">
            <div class="card-body">
              <h5 class="card-title">${med.nome}</h5>
              <p class="card-text">
                Lote: ${med.lote || '-'}<br>
                Validade: ${med.validade || '-'}<br>
                Quantidade: ${med.quantidade || '-'}<br>
                Preço: R$ ${med.preco ? med.preco.toFixed(2) : '-'}
              </p>
            </div>
          `;
          container.appendChild(card);
        });

        // Eventos de edição e exclusão (delegação)
        container.addEventListener('click', (e) => {
          if (e.target.classList.contains('bi-pencil-fill')) {
            const id = e.target.getAttribute('data-id');
            const medicamento = medicamentos.find(m => m.id == id);
            if (medicamento) abrirModalEdicao(medicamento);
          }
          if (e.target.classList.contains('bi-trash-fill')) {
            const id = e.target.getAttribute('data-id');
            medicamentoSelecionado = id;
            excluirModal.show();
          }
        });
      }

    } catch (err) {
      console.error(err);
      container.innerHTML = '<p class="text-danger text-center mt-4">Erro ao carregar medicamentos.</p>';
    }
  }

  // Carrega os medicamentos ao iniciar
  await carregarMedicamentos();

  // Função para abrir modal de edição
  function abrirModalEdicao(med) {
    document.getElementById('editarId').value = med.id;
    document.getElementById('editarNome').value = med.nome || '';
    document.getElementById('editarLote').value = med.lote || '';
    document.getElementById('editarValidade').value = med.validade || '';
    document.getElementById('editarPreco').value = med.preco || '';
    document.getElementById('editarQuantidade').value = med.quantidade || '';
    document.getElementById('editarFotoUrl').value = med.fotoUrl || '';
    editarModal.show();
  }

  // Submeter edição
  document.getElementById('formEditarMedicamento').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('editarId').value;

    const dados = {
      nome: document.getElementById('editarNome').value,
      lote: document.getElementById('editarLote').value,
      validade: document.getElementById('editarValidade').value,
      preco: parseFloat(document.getElementById('editarPreco').value),
      quantidade: parseInt(document.getElementById('editarQuantidade').value),
      fotoUrl: document.getElementById('editarFotoUrl').value
    };

    try {
      const response = await fetch(`http://localhost:8080/api/instituicao/medicamentos/${instituicaoLogada.id}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
      });

      if (response.ok) {
        editarModal.hide();
        alert('Medicamento atualizado com sucesso!');
        carregarMedicamentos();
      } else {
        alert('Erro ao atualizar medicamento.');
      }
    } catch (err) {
      console.error(err);
      alert('Erro de conexão com o servidor.');
    }
  });

  // Confirmar exclusão
  document.getElementById('confirmarExclusao').addEventListener('click', async () => {
    if (!medicamentoSelecionado) return;

    try {
      const response = await fetch(`http://localhost:8080/api/instituicao/medicamentos/${instituicaoLogada.id}/${medicamentoSelecionado}`, {
        method: 'DELETE'
      });

      if (response.ok) {
        excluirModal.hide();
        alert('Medicamento excluído com sucesso!');
        carregarMedicamentos();
      } else {
        alert('Erro ao excluir medicamento.');
      }
    } catch (err) {
      console.error(err);
      alert('Erro de conexão com o servidor.');
    }
  });

  // Abrir modal de adição
  const btnAbrirModalAdicionar = document.getElementById('btnAbrirModalAdicionar');
  btnAbrirModalAdicionar.addEventListener('click', () => {
    document.getElementById('formAdicionarMedicamento').reset();
    adicionarModal.show();
  });

  // Submeter novo medicamento
  document.getElementById('formAdicionarMedicamento').addEventListener('submit', async (e) => {
    e.preventDefault();

    const novoMedicamento = {
      nome: document.getElementById('adicionarNome').value,
      lote: document.getElementById('adicionarLote').value,
      validade: document.getElementById('adicionarValidade').value,
      preco: parseFloat(document.getElementById('adicionarPreco').value),
      quantidade: parseInt(document.getElementById('adicionarQuantidade').value),
      fotoUrl: document.getElementById('adicionarFotoUrl').value
    };

    try {
      const response = await fetch(`http://localhost:8080/api/instituicao/medicamentos/${instituicaoLogada.id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(novoMedicamento)
      });

      if (response.ok) {
        adicionarModal.hide();
        alert('Medicamento adicionado com sucesso!');
        carregarMedicamentos();
      } else {
        alert('Erro ao adicionar medicamento.');
      }
    } catch (err) {
      console.error(err);
      alert('Erro de conexão com o servidor.');
    }
  });
});
