package br.com.casadocodigo.loja.daos;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.casadocodigo.loja.builders.ProdutoBuilder;
import br.com.casadocodigo.loja.conf.DataSourceConfigurationTest;
import br.com.casadocodigo.loja.conf.JPAConfiguration;
import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes= {JPAConfiguration.class, ProdutoDAO.class, 
								DataSourceConfigurationTest.class})
public class ProdutoDAOTest {
	
	@Autowired
	ProdutoDAO produtoDAO = new ProdutoDAO();
	
	
	@Test
	@Transactional
	public void deveSomarTodosPrecosPorTipo() {
		
		List<Produto> impressos = ProdutoBuilder
				.novoProduto(TipoPreco.IMPRESSO, BigDecimal.TEN).mais(3).buildAll();
		List<Produto> ebooks = ProdutoBuilder
				.novoProduto(TipoPreco.EBOOK, BigDecimal.TEN).mais(3).buildAll();
		
		impressos.stream().forEach(produtoDAO::gravar);
		ebooks.stream().forEach(produtoDAO::gravar);
		
		BigDecimal valor = produtoDAO.somaPrecosPorTipo(TipoPreco.EBOOK);
		Assert.assertEquals(new BigDecimal(40).setScale(2), valor);
	}
}
