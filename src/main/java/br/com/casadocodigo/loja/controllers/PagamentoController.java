package br.com.casadocodigo.loja.controllers;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo.loja.models.CarrinhoCompras;
import br.com.casadocodigo.loja.models.DadosPagamento;

@Controller
@RequestMapping("/pagamento")
public class PagamentoController {
	
	@Autowired
	private CarrinhoCompras carrinho;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@SuppressWarnings("finally")
	@RequestMapping(value="/finalizar", method=RequestMethod.POST)
	public Callable<ModelAndView> finalizar(RedirectAttributes attributes) {
		
		return () -> {
			try {
				BigDecimal total = carrinho.getTotal();
				String uri = "http://book-payment.herokuapp.com/payment";
				String response = restTemplate.postForObject(uri, new DadosPagamento(total), String.class);
				
				attributes.addFlashAttribute("sucesso", response + "- R$" +total.toString());
				
			}catch(HttpClientErrorException ex) {
				ex.printStackTrace();
				attributes.addFlashAttribute("falha", "Valor maior que o permitido!");
			}
			catch(Exception ex) {
				ex.printStackTrace();
				attributes.addFlashAttribute("falha", "Erro durante o pagamento!");
				
			}finally{
				return new ModelAndView("redirect:/produtos");
			}
			
		};
		
		
	}

}
