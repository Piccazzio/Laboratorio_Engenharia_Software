// server.js
const express = require('express');
const fs = require('fs');
const path = require('path');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const PORT = 8000;

// --- Middlewares ---
app.use(cors());
app.use(bodyParser.json({ limit: '5mb' })); // permite upload Base64 até 5MB
app.use(bodyParser.urlencoded({ extended: true, limit: '5mb' }));

// --- Caminhos ---
const templatesPath = path.join(__dirname, '../../templates');
const staticPath = path.join(__dirname, '../');
const usuariosPath = path.join(__dirname, '../mock/usuarios.json');

// --- Servir arquivos estáticos ---
app.use('/static', express.static(staticPath)); // JS, CSS, imagens
app.use('/mock', express.static(path.join(__dirname, '../mock')));

// --- Funções auxiliares ---
function lerUsuarios() {
    if (!fs.existsSync(usuariosPath)) return [];
    const data = fs.readFileSync(usuariosPath, 'utf-8');
    return JSON.parse(data || '[]');
}

function salvarUsuarios(usuarios) {
    fs.writeFileSync(usuariosPath, JSON.stringify(usuarios, null, 2));
}

// --- Rotas API ---
// Cadastro
app.post('/api/usuarios/cadastro', (req, res) => {
    const { nome, email, senha } = req.body;
    if (!nome || !email || !senha) return res.status(400).send("Todos os campos são obrigatórios");

    const usuarios = lerUsuarios();
    if (usuarios.some(u => u.email === email)) return res.status(409).send("Usuário já cadastrado");

    usuarios.push({ nome, email, senha, foto: "/mock/default.png" });
    salvarUsuarios(usuarios);
    return res.send("Cadastro realizado com sucesso");
});

// Login
app.post('/api/login', (req, res) => {
    const { email, senha } = req.body;
    if (!email || !senha) return res.status(400).send("E-mail e senha são obrigatórios");

    const usuarios = lerUsuarios();
    const usuario = usuarios.find(u => u.email === email && u.senha === senha);
    if (!usuario) return res.status(401).send("Usuário ou senha incorretos");

    return res.json({
        nome: usuario.nome,
        email: usuario.email,
        senha: usuario.senha,
        foto: usuario.foto || "/mock/default.png"
    });
});

// Buscar usuário por e-mail
app.get('/api/usuarios/:email', (req, res) => {
    const { email } = req.params;
    const usuarios = lerUsuarios();
    const usuario = usuarios.find(u => u.email === email);
    if (!usuario) return res.status(404).send("Usuário não encontrado");
    return res.json(usuario);
});

// Atualizar perfil
app.put('/perfil', (req, res) => {
    const { nome, email, senha, foto } = req.body;
    if (!email) return res.status(400).send("E-mail é obrigatório");

    const usuarios = lerUsuarios();
    const index = usuarios.findIndex(u => u.email === email);
    if (index === -1) return res.status(404).send("Usuário não encontrado");

    usuarios[index] = { ...usuarios[index], nome, senha, foto };
    salvarUsuarios(usuarios);
    res.json({ mensagem: "Perfil atualizado com sucesso" });
});

// --- Rotas HTML ---
app.get('/', (req, res) => res.sendFile(path.join(templatesPath, 'login.html')));
app.get('/index.html', (req, res) => res.sendFile(path.join(templatesPath, 'index.html')));
app.get('/login.html', (req, res) => res.sendFile(path.join(templatesPath, 'login.html')));
app.get('/cadastro.html', (req, res) => res.sendFile(path.join(templatesPath, 'cadastro.html')));
app.get('/sobre.html', (req, res) => res.sendFile(path.join(templatesPath, 'sobre.html')));
app.get('/medicamentos.html', (req, res) => res.sendFile(path.join(templatesPath, 'medicamentos.html')));
app.get('/sustentabilidade.html', (req, res) => res.sendFile(path.join(templatesPath, 'sustentabilidade.html')));
app.get('/meu_perfil.html', (req, res) => res.sendFile(path.join(templatesPath, 'meu_perfil.html')));
// Rota para página de medicamentos da instituição
app.get('/medicamentos_instituicao.html', (req, res) => 
    res.sendFile(path.join(templatesPath, 'medicamentos_instituicao.html'))
);


// --- Inicia servidor ---
app.listen(PORT, () => {
    console.log(`Servidor rodando em http://localhost:${PORT}`);
});
