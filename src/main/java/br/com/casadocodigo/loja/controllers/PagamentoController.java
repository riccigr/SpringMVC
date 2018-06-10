package br.com.casadocodigo.loja.controllers;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo.loja.models.CarrinhoCompras;
import br.com.casadocodigo.loja.models.DadosPagamento;
import br.com.casadocodigo.loja.models.Usuario;

@Controller
@RequestMapping("/pagamento")
public class PagamentoController {
	
	@Autowired
	private CarrinhoCompras carrinho;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MailSender sender;
	
	@SuppressWarnings("finally")
	@RequestMapping(value="/finalizar", method=RequestMethod.POST)
	public Callable<ModelAndView> finalizar(@AuthenticationPrincipal Usuario usuario, RedirectAttributes attributes) {
		
		return () -> {
			try {
				BigDecimal total = carrinho.getTotal();
				String uri = "http://book-payment.herokuapp.com/payment";
				String response = restTemplate.postForObject(uri, new DadosPagamento(total), String.class);
				
				attributes.addFlashAttribute("sucesso", response + "- R$" +total.toString());
				
				enviaEmailCompra(usuario);
				
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

	private void enviaEmailCompra(Usuario usuario) {
		SimpleMailMessage mail = new SimpleMailMessage();
		
		mail.setSubject("Compra aprovada");
		mail.setText("Compra aprovada no valor de " + carrinho.getTotal());
		mail.setFrom("naoresponde@teste.com.br");
		mail.setTo(usuario.getEmail());
		
		sender.send(mail);
	}

}
