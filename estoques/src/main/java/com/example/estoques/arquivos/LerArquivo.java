package com.example.estoques.arquivos;

import com.example.estoques.Produtos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class LerArquivo {

    public void lerArquivo(List<Produtos> listaProdutos, String nomeArquivo) {
        File arquivo = new File(nomeArquivo);

        try {
            if (!arquivo.exists()) {
                System.err.println("O arquivo não existe.");
            } else if (arquivo.length() > 0) {
                try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        String[] elementos = linha.split(",");
                        if (elementos.length < 5) {
                            System.err.println("Linha com formato incorreto: " + linha);
                        } else {
                            listaProdutos.add(novoProduto(elementos));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Produtos novoProduto(String[] elementos) {
        Integer id = Integer.valueOf(elementos[0]);
        String nome = elementos[1];
        Integer quantidade = Integer.valueOf(elementos[2]);
        String categoria = elementos[3];
        BigDecimal preco = new BigDecimal(elementos[4]);

        return new Produtos(id, nome, quantidade, categoria, preco);
    }
}
