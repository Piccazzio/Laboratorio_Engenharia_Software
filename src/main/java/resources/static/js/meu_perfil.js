document.addEventListener("DOMContentLoaded", () => {
  // ======================================================
  // Seletores de elementos
  // ======================================================
  const nomeInput = document.getElementById('nome');
  const emailInput = document.getElementById('email');
  const senhaInput = document.getElementById('senha');
  const cpfInput = document.getElementById('cpf');
  const cnpjInput = document.getElementById('cnpj');
  const telefoneInput = document.getElementById('telefone');
  const enderecoInput = document.getElementById('endereco');
  const fotoInput = document.getElementById('foto');
  const fotoPreview = document.getElementById('fotoPerfil');
  const btnSalvar = document.getElementById('btnSalvar');
  const mensagemDiv = document.getElementById('mensagem');

  const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado'));
  const isInstituicao = localStorage.getItem('isInstituicao') === 'true';

  const btnLogout = document.getElementById('btnLogout');
  if (btnLogout) {
    btnLogout.addEventListener('click', () => {
      localStorage.removeItem('usuarioLogado');
      localStorage.removeItem('isInstituicao');
      window.location.href = "login.html";
    });
  }

  if (!usuarioLogado) {
    window.location.href = 'login.html';
    return;
  }

  // ======================================================
  // Preencher campos do usuário
  // ======================================================
  nomeInput.value = usuarioLogado.nome || '';
  emailInput.value = usuarioLogado.email || '';
  senhaInput.value = usuarioLogado.senha || '';
  telefoneInput.value = usuarioLogado.telefone || '';
  enderecoInput.value = usuarioLogado.endereco || '';
  fotoPreview.src = usuarioLogado.fotoUrl || '/images/default.png';

  if (usuarioLogado.cpf) {
    cpfInput.value = usuarioLogado.cpf;
    cpfInput.parentElement.style.display = 'block';
    cnpjInput.parentElement.style.display = 'none';
  } else if (usuarioLogado.cnpj) {
    cnpjInput.value = usuarioLogado.cnpj;
    cnpjInput.parentElement.style.display = 'block';
    cpfInput.parentElement.style.display = 'none';
  }

  // ======================================================
  // Preview da foto
  // ======================================================
  fotoInput.addEventListener('change', function() {
    const file = this.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = function(e) {
      fotoPreview.src = e.target.result;
      fotoPreview.dataset.base64 = e.target.result;
    };
    reader.readAsDataURL(file);
  });

  // ======================================================
  // Validação simples dos campos
  // ======================================================
  function camposValidos() {
    return nomeInput.value.trim() && senhaInput.value.trim();
  }

  function atualizarBotao() {
    btnSalvar.disabled = !camposValidos();
    mensagemDiv.textContent = "";
  }

  [nomeInput, senhaInput].forEach(input => input.addEventListener('input', atualizarBotao));

  function mostrarMensagem(tipo, texto) {
    mensagemDiv.innerHTML = `<div class="alert alert-${tipo} fadeIn" role="alert">${texto}</div>`;
  }

  // ======================================================
  // Botão Gerenciar Medicamentos (apenas para instituições)
  // ======================================================
  if (isInstituicao) {
    const dropdown = document.getElementById('perfilDropdown');

    const li = document.createElement('li');
    const a = document.createElement('a');
    a.className = 'dropdown-item';
    a.href = 'medicamentos_instituicao.html';
    a.textContent = 'Meus Medicamentos';
    li.appendChild(a);

    // Inserir antes do divider
    const dividerLi = dropdown.querySelector('li > hr').parentElement;
    dropdown.insertBefore(li, dividerLi);

  }

  // Adiciona link global "Meus Medicamentos" no dropdown
  const dropdown = document.getElementById('perfilDropdown');
  if (isInstituicao && dropdown) {
    const li = document.createElement('li');
    li.innerHTML = '<a class="dropdown-item" href="medicamentos_instituicao.html">Meus Medicamentos</a>';
    dropdown.appendChild(li);
  }

  // ======================================================
  // Atualizar perfil
  // ======================================================
  btnSalvar.addEventListener('click', async function() {
    if (!camposValidos()) return;

    const dadosAtualizados = {
      id: usuarioLogado.id,
      nome: nomeInput.value.trim(),
      email: emailInput.value.trim(),
      senha: senhaInput.value.trim(),
      telefone: telefoneInput.value.trim(),
      endereco: enderecoInput.value.trim(),
      fotoUrl: fotoPreview.dataset.base64 || usuarioLogado.fotoUrl || '/images/default.png'
    };

    if (usuarioLogado.cpf) dadosAtualizados.cpf = cpfInput.value.trim();
    if (usuarioLogado.cnpj) dadosAtualizados.cnpj = cnpjInput.value.trim();

    // Determina endpoint correto
    const endpoint = usuarioLogado.cpf
      ? `http://localhost:8080/api/consumidor/${usuarioLogado.id}`
      : `http://localhost:8080/api/instituicao/${usuarioLogado.id}`;

    try {
      const resposta = await fetch(endpoint, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dadosAtualizados)
      });

      if (!resposta.ok) {
        const erro = await resposta.text();
        throw new Error(erro || 'Erro ao atualizar perfil.');
      }

      const usuarioAtualizado = await resposta.json();
      localStorage.setItem('usuarioLogado', JSON.stringify(usuarioAtualizado));
      mostrarMensagem('success', '✅ Perfil atualizado com sucesso!');
    } catch (err) {
      console.error(err);
      mostrarMensagem('danger', `⚠️ ${err.message}`);
    }
  });
});
