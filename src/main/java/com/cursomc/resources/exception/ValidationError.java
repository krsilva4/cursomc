package com.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsavel por tratar formatar erro de validacao...
 */

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;
	private List<FieldMessage> erros = new ArrayList<FieldMessage>();

	public ValidationError(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
		// TODO Auto-generated constructor stub
	}

	public List<FieldMessage> getErros() {
		return erros;
	}

	public void addList(String fieldName, String message) {
		this.erros.add(new FieldMessage(fieldName, message));
	}

}
