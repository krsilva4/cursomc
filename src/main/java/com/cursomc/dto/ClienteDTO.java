package com.cursomc.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.cursomc.domain.Cliente;

public class ClienteDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;

	//Realizando consistencia no campo abaixo
	@NotEmpty(message = "Preenchimento obrigatorio!")
	@Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 a 80 caracteres")
	private String nome;
	@NotEmpty(message = "Preenchimento obrigatorio!")
	@Email(message = "Email invalido!")
	private String email;

	public ClienteDTO() {
	}

	public ClienteDTO(Cliente obj) {
		super();
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.email = obj.getEmail();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
