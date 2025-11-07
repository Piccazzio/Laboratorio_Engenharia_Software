package com.projeto_faculdade;

public class TestMySQLDriver {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Driver MySQL carregado com sucesso!");
    }
}
