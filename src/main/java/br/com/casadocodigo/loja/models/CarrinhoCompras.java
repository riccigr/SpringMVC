package br.com.casadocodigo.loja.models;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CarrinhoCompras {
	
	private Map<CarrinhoItem, Integer> listaItens = new LinkedHashMap<CarrinhoItem, Integer>();

	public void add(CarrinhoItem item) {
		listaItens.put(item, getQuantidade(item) + 1);
	}

	private int getQuantidade(CarrinhoItem item) {
		if(!listaItens.containsKey(item)) {
			listaItens.put(item, 0);
		}
		return listaItens.get(item);
	}
	
	public int getQuantidade() {
		return listaItens.values()
						.stream()
						.reduce(0, (proximo, acumulado) -> proximo + acumulado);
	}
}
