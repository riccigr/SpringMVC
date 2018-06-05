package br.com.casadocodigo.loja.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo.loja.models.CarrinhoCompras;

@Controller
@RequestMapping("/pagamento")
public class PagamentoController {
	
	@Autowired
	private CarrinhoCompras carrinho;
	
	@RequestMapping(value="/finalizar", method=RequestMethod.POST)
	public ModelAndView finalizar(RedirectAttributes attributes) {
		
		BigDecimal total = carrinho.getTotal();
		attributes.addFlashAttribute("sucesso", "pagamento realizado com sucesso: R$" + total.toString());
		
		return new ModelAndView("redirect:/produtos");
	}

}
