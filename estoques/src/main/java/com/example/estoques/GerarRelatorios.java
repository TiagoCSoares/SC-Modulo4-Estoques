package com.example.estoques;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GerarRelatorios {

    private final JdbcTemplate jdbcTemplate;

    public GerarRelatorios(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void numeroCategorias() {
        String sql = "SELECT * FROM PRODUTOS";

        RowMapper<Produtos> rowMapper = ((rs, rowNum) -> new Produtos(
                rs.getInt("ID"),
                rs.getString("NOME"),
                rs.getInt("QUANTIDADE"),
                rs.getString("CATEGORIA"),
                rs.getBigDecimal("PRECO")
        ));

        List<Produtos> listaProdutos = jdbcTemplate.query(
                sql, rowMapper
        );

        long numeroCategorias = listaProdutos.stream()
                .map(Produtos::getCategoria)
                .distinct()
                .count();

        System.out.println("\n\nNúmero de categorias de produtos: " + numeroCategorias);
    }


    public void quantidadeEmEstoque() {
        String sql = "SELECT * FROM PRODUTOS";

        RowMapper<Produtos> rowMapper = ((rs, rowNum) -> new Produtos(
                rs.getInt("ID"),
                rs.getString("NOME"),
                rs.getInt("QUANTIDADE"),
                rs.getString("CATEGORIA"),
                rs.getBigDecimal("PRECO")
        ));

        List<Produtos> listaProdutos = jdbcTemplate.query(
                sql, rowMapper
        );

        Map<String, Integer> quantidadePorCategoria = listaProdutos.stream()
                .collect(Collectors.groupingBy(Produtos::getCategoria,
                        Collectors.summingInt(Produtos::getQuantidade)));

        System.out.println();
        quantidadePorCategoria.forEach((categoria, quantidade) ->
                System.out.println("Categoria: " + categoria + ", Quantidade em estoque: " + quantidade));

    }

    public void valorMedio() {
        String sql = "SELECT * FROM PRODUTOS";

        RowMapper<Produtos> rowMapper = ((rs, rowNum) -> new Produtos(
                rs.getInt("ID"),
                rs.getString("NOME"),
                rs.getInt("QUANTIDADE"),
                rs.getString("CATEGORIA"),
                rs.getBigDecimal("PRECO")
        ));

        List<Produtos> listaProdutos = jdbcTemplate.query(
                sql, rowMapper
        );


        BigDecimal precoTotal = listaProdutos.stream()
                .map(p -> p.getPreco().multiply(BigDecimal.valueOf(p.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int quantidadeTotal = listaProdutos.stream()
                .mapToInt(Produtos::getQuantidade)
                .sum();
        BigDecimal mediaPreco = precoTotal.divide(BigDecimal.valueOf(quantidadeTotal), 2, RoundingMode.HALF_UP);

        System.out.println("\nValor médio dos Produtos: " + mediaPreco);
    }

    public List<Produtos> estoqueBaixo() {
        String sql = "SELECT * FROM PRODUTOS";

        RowMapper<Produtos> rowMapper = ((rs, rowNum) -> new Produtos(
                rs.getInt("ID"),
                rs.getString("NOME"),
                rs.getInt("QUANTIDADE"),
                rs.getString("CATEGORIA"),
                rs.getBigDecimal("PRECO")
        ));

        List<Produtos> listaProdutos = jdbcTemplate.query(
                sql, rowMapper
        );

        System.out.println("\nProdutos com estoque baixo:");
        listaProdutos.stream()
                .filter(p -> p.getQuantidade() < 3)
                .forEach(p -> {
                    p.setEstoque("estoque baixo");
                    System.out.println(p.getNome());
        });

        return listaProdutos;
    }



}