package com.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Categoria;
import com.cursomc.dto.CategoriaDTO;
import com.cursomc.repositories.CategoriaRepository;
import com.cursomc.services.exceptions.DataIntegrityException;
import com.cursomc.services.exceptions.ObjectNotFoundException;

/*Camada de servicos.
 * responsavel por oferecer operacoes e consultas, 
 * para as camadas controlares rest obs(regras de negocio, nao tem envolvimento com nenhuma tecnologia)
*/
@Service
public class CategoriaService {

	private final String msgErro = "Nao e possivel excluir uma categoria que possui produtos!";
	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		Categoria obj = repo.findOne(id);
// Tratamento de exceção personalizada...		
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto nao encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName());
		}
		return obj;
	}

	/*
	 * Medoto responsavel por criar uma nova categoria
	 */
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	/*
	 * Medoto responsavel por atualizar a categoria
	 */
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}

	/*
	 * Medoto responsavel por deletar a categoria
	 */
	public void delete(Integer id) {
		find(id);
		try {
			repo.delete(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(msgErro);
		}

	}

	public List<Categoria> fidAll() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	/*
	 * Medoto responsavel por paginacao e ordenacao dos registros.
	 */
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	/*
	 * Medoto responsavel para converter uma DTO para categoria. 
	 */
	public Categoria fromDTO(CategoriaDTO obj) {
		Categoria categoria = new Categoria(obj.getId(), obj.getNome());
		return categoria;
	}

}
