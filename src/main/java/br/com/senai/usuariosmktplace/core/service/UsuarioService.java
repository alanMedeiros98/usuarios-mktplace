package br.com.senai.usuariosmktplace.core.service;

import br.com.senai.usuariosmktplace.core.dao.DaoUsuario;
import br.com.senai.usuariosmktplace.core.dao.FactoryDao;
import br.com.senai.usuariosmktplace.core.domain.Usuario;

public class UsuarioService {

	private DaoUsuario dao;
	
	public UsuarioService() {
		
		this.dao = FactoryDao.getInstance().getDaoUsuario();
		
	}
	
	public void inserir(Usuario usuario) {
		
		this.validar(usuario);
		boolean isUsuarioOK = usuario.getLogin() != null;
		
		if (isUsuarioOK) {
			
			this.dao.alterar(usuario);
			
		} else {
			
			this.dao.inserir(usuario);
			
		}
		
	}
	
	private void validar(Usuario usuario) {
		
		if (usuario != null) {
			
		}
		
	}
	
}
