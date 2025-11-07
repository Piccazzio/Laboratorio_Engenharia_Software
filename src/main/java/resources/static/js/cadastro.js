document.addEventListener("DOMContentLoaded", () => {
  // ======================================================
  // Seletores de elementos
  // ======================================================
  const form = document.getElementById("cadastroForm");
  const btnSubmit = document.getElementById("btnSubmit");
  const mensagem = document.getElementById("mensagem");

  // Campos comuns
  const emailInput = document.getElementById("email");
  const senhaInput = document.getElementById("senha");

  // Checklist senha
  const checkTamanho = document.getElementById("check-tamanho");
  const checkMaiuscula = document.getElementById("check-maiuscula");
  const checkMinuscula = document.getElementById("check-minuscula");
  const checkEspecial = document.getElementById("check-especial");

  // Campos Pessoa
  const pessoaFields = document.getElementById("pessoaFields");
  const nomeInput = document.getElementById("nome");
  const sobrenomeInput = document.getElementById("sobrenome");
  const cpfInput = document.getElementById("cpf");

  // Campos Instituição
  const instituicaoFields = document.getElementById("instituicaoFields");
  const nomeInstituicaoInput = document.getElementById("nomeInstituicao");
  const cnpjInput = document.getElementById("cnpj");
  const responsavelInput = document.getElementById("responsavel");

  // Checkbox tipo usuário
  const checkboxes = document.querySelectorAll(".tipoUsuarioCheckbox");

  // ======================================================
  // Funções utilitárias
  // ======================================================
  const tipoSelecionado = () => {
    const selecionado = document.querySelector(".tipoUsuarioCheckbox:checked");
    return selecionado ? selecionado.value : null;
  };

  const atualizarChecklist = (elemento, valido) => {
    const icon = elemento.querySelector(".icon");
    if (valido) {
      elemento.classList.replace("text-red-500", "text-green-500");
      icon.textContent = "✅";
    } else {
      elemento.classList.replace("text-green-500", "text-red-500");
      icon.textContent = "❌";
    }
  };

  const validarSenha = (senha) => {
    const regras = {
      tamanho: senha.length >= 7,
      maiuscula: /[A-Z]/.test(senha),
      minuscula: /[a-z]/.test(senha),
      especial: /[!@#$%^&*(),.?":{}|<>]/.test(senha)
    };

    atualizarChecklist(checkTamanho, regras.tamanho);
    atualizarChecklist(checkMaiuscula, regras.maiuscula);
    atualizarChecklist(checkMinuscula, regras.minuscula);
    atualizarChecklist(checkEspecial, regras.especial);

    return Object.values(regras).every(v => v === true);
  };

  const validarCampos = () => {
    const senhaValida = validarSenha(senhaInput.value);
    const tipo = tipoSelecionado();
    let camposValidos = emailInput.value.trim() !== "" && senhaValida && tipo;

    if (tipo === "pessoa") {
      camposValidos &&= nomeInput.value.trim() && sobrenomeInput.value.trim() && cpfInput.value.trim();
    } else if (tipo === "instituicao") {
      camposValidos &&= nomeInstituicaoInput.value.trim() && cnpjInput.value.trim() && responsavelInput.value.trim();
    }

    btnSubmit.disabled = !camposValidos;
  };

  // ======================================================
  // Eventos de input
  // ======================================================
  senhaInput.addEventListener("input", validarCampos);
  emailInput.addEventListener("input", validarCampos);
  [nomeInput, sobrenomeInput, cpfInput, nomeInstituicaoInput, cnpjInput, responsavelInput]
    .forEach(input => input.addEventListener("input", validarCampos));
  checkboxes.forEach(cb => cb.addEventListener("change", validarCampos));

  // ======================================================
  // Submissão do formulário
  // ======================================================
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    mensagem.textContent = "";

    const tipo = tipoSelecionado();
    if (!tipo) {
      mensagem.textContent = "Selecione o tipo de usuário.";
      return;
    }

    // Montar payload de acordo com o tipo
    let usuario;
    let endpoint;

    if (tipo === "pessoa") {
      usuario = {
        nome: nomeInput.value.trim(),
        cpf: cpfInput.value.trim(),
        email: emailInput.value.trim(),
        senha: senhaInput.value.trim()
      };
      endpoint = "http://localhost:8080/api/consumidor";
    } else {
      usuario = {
        nome: nomeInstituicaoInput.value.trim(),
        cnpj: cnpjInput.value.trim(),
        email: emailInput.value.trim(),
        senha: senhaInput.value.trim()
      };
      endpoint = "http://localhost:8080/api/instituicao";
    }

    try {
      const resposta = await fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(usuario)
      });

      if (resposta.ok) {
        const usuarioCriado = await resposta.json(); // pega o objeto criado
        localStorage.setItem("usuarioLogado", JSON.stringify(usuarioCriado)); // salva no navegador

        mensagem.classList.replace("text-red-500", "text-green-500");
        mensagem.textContent = "Cadastro realizado com sucesso!";
        form.reset();
        btnSubmit.disabled = true;
        pessoaFields.classList.add("hidden");
        instituicaoFields.classList.add("hidden");

        setTimeout(() => window.location.href = "meu_perfil.html", 1500);
      } else if (resposta.status === 409) {
        mensagem.textContent = tipo === "pessoa"
          ? "CPF já cadastrado."
          : "CNPJ já cadastrado.";
      } else {
        const erro = await resposta.text();
        mensagem.textContent = erro || "Erro ao cadastrar usuário.";
      }

    } catch (err) {
      console.error(err);
      mensagem.textContent = "Erro de conexão com o servidor.";
    }
  });
});
