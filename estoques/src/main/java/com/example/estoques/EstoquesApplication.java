package com.example.estoques;

import com.example.estoques.arquivos.LerArquivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sound.sampled.Port;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class EstoquesApplication implements CommandLineRunner {

	private final JdbcTemplate jdbcTemplate;

	public EstoquesApplication(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(EstoquesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//deleteTableIfExists();
		createTableIfNotExists();
		limparTabela();

		List<Produtos> listaProdutos = new ArrayList<>();
		new LerArquivo().lerArquivo(listaProdutos, "src/main/java/com/example/estoques/arquivosCSV/lista_de_produtos.csv");

		listaProdutos.forEach(p -> {
					insert(p);
				});

		//Gerar Relatorios
		GerarRelatorios gerarRelatorios = new GerarRelatorios(jdbcTemplate);

		gerarRelatorios.numeroCategorias();
		gerarRelatorios.quantidadeEmEstoque();
		gerarRelatorios.valorMedio();

		listaProdutos = gerarRelatorios.estoqueBaixo();
		listaProdutos = listaProdutos.stream()
				.map(p -> {
					if (p.getEstoque() == null) {
						p.setEstoque("normal");
					}
					return p;
				})
				.collect(Collectors.toList());

		listaProdutos.forEach(p -> {
				update(p);
		});
	}

	private void selectProdutos() {
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

		listaProdutos.forEach(p -> {
			System.out.println(p.getId());
		});
	}



	private void delete(Integer id) {
		String sql = "delete from PRODUTOS where id = ?";
		jdbcTemplate.update(sql, id);
	}

	private void update(Produtos produto) {
		String sql = "UPDATE PRODUTOS SET nome = ?, quantidade = ?, categoria = ?, preco = ?, estoque = ? WHERE id = ?";
		jdbcTemplate.update(sql,
				produto.getNome(),
				produto.getQuantidade(),
				produto.getCategoria(),
				produto.getPreco(),
				produto.getEstoque(),
				produto.getId());
	}

	private void insert(Produtos produto) {
		String sql = "INSERT INTO PRODUTOS (nome, quantidade, categoria, preco, estoque) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql,
				produto.getNome(),
				produto.getQuantidade(),
				produto.getCategoria(),
				produto.getPreco(),
				produto.getEstoque());
	}



	private void createTableIfNotExists() {
		String sql = "CREATE TABLE IF NOT EXISTS PRODUTOS (" +
				"ID INT PRIMARY KEY AUTO_INCREMENT," +
				"NOME VARCHAR(100) NOT NULL," +
				"QUANTIDADE INT," +
				"CATEGORIA VARCHAR(100)," +
				"PRECO DECIMAL(10,2)," +
				"ESTOQUE VARCHAR(100)" +
				")";
		jdbcTemplate.execute(sql);
	}

	private void limparTabela() {
		String sql = "DELETE FROM PRODUTOS";
		jdbcTemplate.update(sql);
	}

	private void deleteTableIfExists() {
		String sql = "DROP TABLE IF EXISTS PRODUTOS";
		jdbcTemplate.execute(sql);
	}
}
