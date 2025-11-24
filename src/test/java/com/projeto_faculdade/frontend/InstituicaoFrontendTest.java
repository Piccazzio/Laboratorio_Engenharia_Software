package com.projeto_faculdade.frontend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InstituicaoFrontendTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        driver = new SafariDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFluxoCompletoInstituicao() throws InterruptedException {
        login();
        acessarMedicamentos();
        cadastrarMedicamento();
        editarMedicamento();
        excluirMedicamento();
    }

    private void login() throws InterruptedException {
        driver.get("http://localhost:8000/index.html");
        WebElement inputEmail = driver.findElement(By.id("email"));
        WebElement inputSenha = driver.findElement(By.id("senha"));
        WebElement btnLogin = driver.findElement(By.id("btnLogin"));

        inputEmail.sendKeys("contato@vidasustentavel.com");
        Thread.sleep(1000);
        inputSenha.sendKeys("senha123");
        Thread.sleep(1000);
        btnLogin.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("nav.navbar")));
        Thread.sleep(1000);
    }

    private void acessarMedicamentos() throws InterruptedException {
        WebElement perfilDropdown = driver.findElement(By.id("perfilDropdown"));
        perfilDropdown.click();
        Thread.sleep(1000);

        WebElement linkMedicamentos = driver.findElement(
                By.cssSelector("a.dropdown-item[href='medicamentos_instituicao.html']"));

        // Tornar visível, pois está com display:none
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", linkMedicamentos);
        Thread.sleep(500);

        linkMedicamentos.click();
        wait.until(ExpectedConditions.urlContains("medicamentos_instituicao.html"));
        assertTrue(driver.getCurrentUrl().contains("medicamentos_instituicao.html"));
        Thread.sleep(1000);
    }

    private void cadastrarMedicamento() throws InterruptedException {
        driver.findElement(By.id("btnAbrirModalAdicionar")).click();
        Thread.sleep(1000);

        driver.findElement(By.id("adicionarNome")).sendKeys("Paracetamol");
        Thread.sleep(2000);
        driver.findElement(By.id("adicionarLote")).sendKeys("L12345");
        Thread.sleep(2000);

        // Preenchendo a data via JS para compatibilidade Safari
        WebElement inputValidade = driver.findElement(By.id("adicionarValidade"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='2026-12-31';", inputValidade);
        Thread.sleep(2000);

        driver.findElement(By.id("adicionarPreco")).sendKeys("12.50");
        Thread.sleep(2000);
        driver.findElement(By.id("adicionarQuantidade")).sendKeys("50");
        Thread.sleep(2000);
        driver.findElement(By.id("adicionarFotoUrl")).sendKeys("https://product-data.raiadrogasil.io/images/10724941.webp");
        Thread.sleep(2000);

        // Submeter o formulário
        driver.findElement(By.cssSelector("#formAdicionarMedicamento button[type='submit']")).click();

        // ======== NOVO: esperar e aceitar o alert ========
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        System.out.println("Alerta exibido: " + alert.getText()); // opcional
        alert.accept();
        // ================================================

        Thread.sleep(1500); // pequena espera para atualização do container

        // Validação final
        WebElement container = driver.findElement(By.id("medicamentosContainer"));
        assertTrue(container.getText().contains("Paracetamol"), "Cadastro falhou!");
    }

    // ==================== EDITAR MEDICAMENTO ====================
    private void editarMedicamento() throws InterruptedException {
        WebElement container = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("medicamentosContainer")));

        java.util.List<WebElement> cards = container.findElements(By.cssSelector(".med-card"));
        if (cards.isEmpty()) {
            System.out.println("Nenhum medicamento encontrado, pulando edição.");
            return;
        }

        WebElement card = cards.get(0);
        WebElement btnEditar = card.findElement(By.cssSelector(".bi-pencil-fill"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnEditar);

        WebElement inputNome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editarNome")));
        Thread.sleep(1500); // pequena espera para atualização do container

        inputNome.clear();
        inputNome.sendKeys("Ibuprofeno Atualizado");
        Thread.sleep(1500); // pequena espera para atualização do container

        driver.findElement(By.cssSelector("#formEditarMedicamento button[type='submit']")).click();

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        System.out.println("Edição simulada com sucesso!");
        Thread.sleep(1500); // pequena espera para atualização do container
    }

    private void excluirMedicamento() throws InterruptedException {
        WebElement container = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("medicamentosContainer")));

        java.util.List<WebElement> cards = container.findElements(By.cssSelector(".med-card"));
        if (cards.isEmpty()) {
            System.out.println("Nenhum medicamento encontrado, pulando exclusão.");
            return;
        }

        WebElement card = cards.get(0);
        WebElement btnExcluir = card.findElement(By.cssSelector(".bi-trash-fill"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnExcluir);

        Thread.sleep(2000);
        driver.findElement(By.id("confirmarExclusao")).click();

        Thread.sleep(2000);
        System.out.println("Exclusão simulada com sucesso!");
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            System.out.println("Alerta de exclusão aceito.");
        } catch (Exception e) {
            System.out.println("Nenhum alerta pendente.");
        }
        Thread.sleep(2000);
    }

}
