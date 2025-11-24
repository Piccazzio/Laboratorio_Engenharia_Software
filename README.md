1. Descrição Geral do Projeto

Este projeto é uma aplicação full stack destinada ao gerenciamento de medicamentos. Ele permite o cadastro, edição, listagem e controle de instituições e medicamentos. Possui:

Backend em Spring Boot.

Frontend web utilizando HTML, CSS, Bootstrap e JavaScript.

Banco MySQL para persistência de dados.

O principal objetivo é oferecer uma interface simples e eficiente para controle de medicamentos.

<br>
2. Estrutura do Projeto

2.1 Backend (Spring Boot)

API REST completa.

Persistência via Spring Data JPA.

Configuração via application.properties.

Endpoints para medicamentos e instituições.

2.2 Frontend

Páginas: login, listagem e edição.

Gerenciamento de sessão via localStorage.

Consumo das APIs por fetch.

2.3 Banco de Dados

MySQL 8.

Tabelas de medicamentos, instituições, consumidor, pedido e pedido_medicamento, com relacionamentos simples.

3. Requisitos
Backend

Java 17+

Maven

MySQL ou container Docker/Colima

Frontend

Navegador moderno

4. Instalação do Backend
   
4.1 Clonando o repositório
git clone [https://github.com/usuario/repositorio.git](https://github.com/Piccazzio/Laboratorio_Engenharia_Software
cd repositorio

4.2 Criando o banco
CREATE DATABASE farmacia_db_mackenzie;

4.3 Configurando o application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/farmacia_db_mackenzie
spring.datasource.username=root
spring.datasource.password=suasenha
spring.jpa.hibernate.ddl-auto=update

4.4 Rodando o servidor
mvn clean install
mvn spring-boot:run

Backend disponível em:

http://localhost:8080

5. Instalação do Frontend
5.1 Acessando o sistema

Abra o arquivo login.html no navegador ou use um servidor simples como:

node server.js

A aplicação pode ser acessada via:

http://localhost:8000

6. Manual de Utilização
6.1 Login

Abra o arquivo login.html.

Preencha e-mail, senha e selecione o tipo de usuário

Se autenticado, você será redirecionado para o painel principal.

6.2 Dashboard de Medicamentos

Você poderá:

Cadastrar medicamentos

Listar medicamentos existentes

Editar medicamentos

Excluir medicamentos

Pesquisar por nome

6.3 Cadastro de Medicamento

Clique em “Adicionar Medicamento”.

Informe os dados solicitados.

Clique em “Salvar”.

6.4 Edição de Medicamento

Selecione o medicamento desejado.

Clique em “Editar”.

Atualize os campos.

Clique em “Salvar”.

6.5 Exclusão de Medicamento

Clique no botão “Excluir”.

Confirme a operação.

O item será removido do banco.

6.6 Logout

Clique em “Sair”.

A sessão será encerrada.

Você será redirecionado ao login.

7. Endpoints Principais
Medicamentos
GET    /medicamentos
GET    /medicamentos/{id}
POST   /medicamentos
PUT    /medicamentos/{id}
DELETE /medicamentos/{id}

Instituições
POST /instituicoes
GET  /instituicoes/login

8. Erros Comuns e Soluções
Erro: Failed to fetch

Backend não está rodando.
Solução: verifique se o Spring Boot está ativo.

Erro: CORS

O frontend está em outra porta.
Solução: revisar permissões CORS no backend.

Login não funciona

A instituição pode não existir.
Solução: cadastrar manualmente.

9. Executando MySQL via Docker/Colima (Opcional)
Criando o container
docker run -d --name mysql_farmacia \
-e MYSQL_ROOT_PASSWORD=suasenha \
-e MYSQL_DATABASE=farmacia_db_mackenzie \
-p 3306:3306 \
mysql:8

Verificar container
docker ps


10. Testes

Para a realização de testes, basta utilizar o comando via terminal: mvn clean test
