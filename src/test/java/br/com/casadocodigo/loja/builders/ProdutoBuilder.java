package br.com.casadocodigo.loja.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.casadocodigo.loja.models.Preco;
import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;

public class ProdutoBuilder {
	
	private List<Produto> produtos = new ArrayList<>();

	public ProdutoBuilder(Produto produto) {
		this.produtos.add(produto);
	}
	
	public static ProdutoBuilder novoProduto(TipoPreco tipo, BigDecimal valor) {
		Produto livro = criar("livro 1", tipo, valor);
		return new ProdutoBuilder(livro);
	}

	public static ProdutoBuilder novoProduto() {
		Produto livro = criar("livro principal", TipoPreco.COMBO, BigDecimal.TEN);
		return new ProdutoBuilder(livro);
	}

	
	private static Produto criar(String nome, TipoPreco tipo, BigDecimal valor) {
		Produto livro = new Produto();
		livro.setTitulo(nome);
		livro.setDataLancamento(Calendar.getInstance());
		livro.setDescricao("descricao de testes...");
		livro.setPaginas(100);

		Preco preco = new Preco();
		preco.setTipo(tipo);
		preco.setValor(valor);
		
		livro.getPrecos().add(preco);
		return livro;
	}
	
	public ProdutoBuilder mais(int quantidade) {
		Produto base = produtos.get(0);
		Preco preco = base.getPrecos().get(0);
		
		for(int i = 0; i < quantidade; i++) {
			produtos.add(criar("Livro " + i, preco.getTipo(), preco.getValor()));
		}
		
		return this;
	}
	
	public Produto buildOne() {
		return produtos.get(0);
	}
	
	public List<Produto> buildAll() {
		return produtos;
	}
	
	

}
