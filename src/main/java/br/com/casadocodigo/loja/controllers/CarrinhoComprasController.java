package br.com.casadocodigo.loja.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.casadocodigo.loja.daos.ProdutoDAO;
import br.com.casadocodigo.loja.models.CarrinhoCompras;
import br.com.casadocodigo.loja.models.CarrinhoItem;
import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoComprasController {

	@Autowired
	private ProdutoDAO produtoDAO;
	
	@Autowired
	private CarrinhoCompras carrinho;

	@RequestMapping("/add")
	public ModelAndView add(Integer produtoId, TipoPreco tipo) {
		ModelAndView modelAndView = new ModelAndView("redirect:/carrinho");
		CarrinhoItem item = criaItem(produtoId, tipo);
		
		carrinho.add(item);
		
		return modelAndView;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView itens() {
		return new ModelAndView("/carrinho/itens");
	}

	private CarrinhoItem criaItem(Integer produtoId, TipoPreco tipo) {
		Produto produto = produtoDAO.buscar(produtoId);
		CarrinhoItem item = new CarrinhoItem(produto, tipo);
		
		return item;
	}
	
	@RequestMapping(value="/remover", method=RequestMethod.POST)
	public ModelAndView remover(Integer produtoId, TipoPreco tipo) {
		carrinho.remover(produtoId, tipo);
		
		return new ModelAndView("redirect:/carrinho");
	}
	
}
