package br.com.senai.usuariosmktplace.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Data
public class Usuario {
	@EqualsAndHashCode.Include
	private String login;
	private String senha;
	@ToString.Exclude
	private String nomeCompleto;
}
