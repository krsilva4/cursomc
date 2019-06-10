package com.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Categoria;
import com.cursomc.repositories.CategoriaRepository;
import com.cursomc.services.exceptions.ObjectNotFoundException;

/*Camada de servicos.
 * responsavel por oferecer operacoes e consultas, 
 * para as camadas controlares rest obs(regras de negocio, nao tem envolvimento com nenhuma tecnologia)
*/
@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria buscar(Integer id) {
		Categoria obj = repo.findOne(id);
// Tratamento de exceção personalizada...		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto nao encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName());
		}
		return obj;
	}
}
