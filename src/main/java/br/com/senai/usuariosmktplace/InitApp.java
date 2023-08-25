package br.com.senai.usuariosmktplace;

import br.com.senai.usuariosmktplace.core.dao.DaoUsuario;
import br.com.senai.usuariosmktplace.core.dao.FactoryDao;
import br.com.senai.usuariosmktplace.core.domain.Usuario;

public class InitApp {

	public static void main(String[] args) {
		
		DaoUsuario dao = FactoryDao.getInstance().getDaoUsuario();
		
		Usuario usuario = new Usuario("alan teste", "alan12345566", "Alan Duarte de Medeiros");
		System.out.println(usuario);
		
	}
	
}
