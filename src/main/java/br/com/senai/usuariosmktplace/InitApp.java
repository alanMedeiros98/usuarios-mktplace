package br.com.senai.usuariosmktplace;

import br.com.senai.usuariosmktplace.core.service.UsuarioService;

public class InitApp {

	public static void main(String[] args) {
		
		UsuarioService service = new UsuarioService();
		
		System.out.println(service.removerAcentoDo("Jacó da Pinheira"));
		System.out.println(service.fracionar("José da Silva"));
		System.out.println(service.gerarLoginPor("José da Silva dos Anjos"));
		System.out.println(service.gerarHashDa("jose1234"));
		
	};
	
}
