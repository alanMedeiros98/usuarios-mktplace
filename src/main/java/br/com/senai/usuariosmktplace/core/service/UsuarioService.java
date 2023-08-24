package br.com.senai.usuariosmktplace.core.service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import br.com.senai.usuariosmktplace.core.dao.DaoUsuario;
import br.com.senai.usuariosmktplace.core.dao.FactoryDao;
import br.com.senai.usuariosmktplace.core.domain.Usuario;

public class UsuarioService {

	private DaoUsuario dao;
	
	public UsuarioService() {
		this.dao = FactoryDao.getInstance().getDaoUsuario();
	}
	
	public Usuario criarPor(String nomeCompleto, String senha) {
		this.validar(nomeCompleto, senha);
		String login = gerarLoginPor(nomeCompleto);
		String senhaCriptografada = gerarHashDa(senha);
		Usuario novoUsuario = new Usuario(login, senhaCriptografada, nomeCompleto);
		this.dao.inserir(novoUsuario);
		Usuario usuarioSalvo = dao.buscarPor(login);
		return usuarioSalvo;
	}
	
	public Usuario atualizarPor(String login, String nomeCompleto, String senhaAntiga, String senhaNova) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(login), "O login é obrigatório para atualização");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(senhaAntiga), "A senha antiga é obrigatório para atualização");
		this.validar(nomeCompleto, senhaNova);
		Usuario usuarioSalvo = dao.buscarPor(login);
		Preconditions.checkNotNull(usuarioSalvo, "Não foi enontrado usuário vinculado ao login informado");
		String senhaAntigaCriptografada = gerarHashDa(senhaAntiga);
		boolean isSenhaValida = senhaAntigaCriptografada.equals(usuarioSalvo.getSenha());
		Preconditions.checkArgument(isSenhaValida, "A senha antiga não confere");
		Preconditions.checkArgument(!senhaAntiga.equals(senhaNova), "A senha nova não pode ser igual a senha anterior");
		String senhaNovaCriptografada = gerarHashDa(senhaNova);
		Usuario usuarioAlterado = new Usuario(login, senhaNovaCriptografada, nomeCompleto);
		this.dao.alterar(usuarioAlterado);
		usuarioAlterado = dao.buscarPor(login);
		return usuarioAlterado;
	}
	
	private String removerAcentoDo(String nomeCompleto) {
		return Normalizer.normalize(nomeCompleto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	private List<String> fracionar(String nomeCompleto){
		List<String> nomeFracionado = new ArrayList<String>();
		if (nomeCompleto != null && !nomeCompleto.isBlank()) {
			String[] partesDoNome = nomeCompleto.split(" ");
			for(String parte : partesDoNome) {
				boolean isNaoContemArtigo = !parte.equalsIgnoreCase("de")
						&& !parte.equalsIgnoreCase("e")
						&& !parte.equalsIgnoreCase("do")
						&& !parte.equalsIgnoreCase("da")
						&& !parte.equalsIgnoreCase("das");
				if (isNaoContemArtigo) {
					nomeFracionado.add(parte.toLowerCase());
				}
			}
		}
		return nomeFracionado;
	}
	
	private String gerarLoginPor(String nomeCompleto) {
		nomeCompleto = removerAcentoDo(nomeCompleto);
		List<String> partesDoNome = fracionar(nomeCompleto);
		String loginGerado = null;
		Usuario usuarioEncontrado = null;
		if (!partesDoNome.isEmpty()) {
			for (int i = 1; i < partesDoNome.size(); i++) {
					loginGerado = partesDoNome.get(0) + "." + partesDoNome.get(i);
					usuarioEncontrado = dao.buscarPor(loginGerado);
					if (usuarioEncontrado == null) {
						return loginGerado;
					}
			}
			int proximoSequencial = 0;
			String loginDisponivel = null;
			while(usuarioEncontrado != null) {
					loginDisponivel = loginGerado + ++proximoSequencial;
					usuarioEncontrado = dao.buscarPor(loginDisponivel);
				}
				loginGerado = loginDisponivel;
			}
		return loginGerado;
		}

	private String gerarHashDa(String senha) {
		return new DigestUtils(MessageDigestAlgorithms.SHA3_256).digestAsHex(senha);
	}
	
	@SuppressWarnings("deprecation")
	private void validar(String senha) {
		boolean isSenhaInvalida = Strings.isNullOrEmpty(senha)
				|| senha.length() < 6
				|| senha.length() > 15;
		Preconditions.checkArgument(!isSenhaInvalida, "A senha é obrigatória e deve conter entre 6 e 15 caracteres.");
		boolean isContemLetra = CharMatcher.inRange('a', 'z').countIn(senha.toLowerCase()) > 0;
		boolean isContemNumero = CharMatcher.inRange('0', '9').countIn(senha) > 0;
		boolean isCaracterInvalido = !CharMatcher.javaLetterOrDigit().matchesAllOf(senha);
		Preconditions.checkArgument(isContemLetra && isContemNumero && !isCaracterInvalido, "A senha deve conter letras e numeros.");
	}
	
	private void validar(String nomeCompleto, String senha) {
		List<String> partesDoNome = fracionar(nomeCompleto);
		boolean isNomeCompleto = partesDoNome.size() > 1;
		boolean isNomeValido = !Strings.isNullOrEmpty(nomeCompleto) && isNomeCompleto && nomeCompleto.length() >= 5 && nomeCompleto.length() <= 128;
		Preconditions.checkArgument(isNomeValido, "O nome é brigatório e deve conter entre 5 e 128 caracteres.");
		this.validar(senha);
	}
	
	
}
