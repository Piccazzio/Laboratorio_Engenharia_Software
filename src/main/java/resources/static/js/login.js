document.addEventListener("DOMContentLoaded", () => {
  // ======================================================
  // Seletores principais
  // ======================================================
  const form = document.getElementById("loginForm");
  const emailInput = document.getElementById("email");
  const senhaInput = document.getElementById("senha");
  const tipoUsuarioInput = document.getElementById("tipoUsuario");
  const mensagem = document.getElementById("mensagem");
  const btnLogin = document.getElementById("btnLogin");

  // Habilitar botão apenas se os campos estiverem preenchidos
  [emailInput, senhaInput].forEach(input => {
    input.addEventListener("input", () => {
      btnLogin.disabled = !(emailInput.value.trim() && senhaInput.value.trim());
    });
  });

  // ======================================================
  // Envio do formulário
  // ======================================================
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    mensagem.textContent = "";

    const dados = {
      email: emailInput.value.trim(),
      senha: senhaInput.value.trim()
    };

    const tipoUsuario = tipoUsuarioInput.value;
    const endpoint = tipoUsuario === "instituicao"
      ? "http://localhost:8080/api/instituicao/login"
      : "http://localhost:8080/api/consumidor/login";

    try {
      const response = await fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dados)
      });

      if (!response.ok) {
        throw new Error(response.status === 401 ? "E-mail ou senha incorretos." : "Erro ao realizar login.");
      }

      const usuario = await response.json();

      // Salva dados do usuário e flag de instituição
      localStorage.setItem("usuarioLogado", JSON.stringify(usuario));
      localStorage.setItem("isInstituicao", tipoUsuario === "instituicao" ? "true" : "false");

      mensagem.textContent = "Login realizado com sucesso!";
      mensagem.classList.remove("text-red-500");
      mensagem.classList.add("text-green-600");

      setTimeout(() => window.location.href = "index.html", 1000);

    } catch (erro) {
      console.error("Erro de login:", erro);
      mensagem.textContent = erro.message;
      mensagem.classList.remove("text-green-600");
      mensagem.classList.add("text-red-500");
    }
  });
});
