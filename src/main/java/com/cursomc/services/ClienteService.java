package com.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Cliente;
import com.cursomc.domain.Cliente;
import com.cursomc.dto.ClienteDTO;
import com.cursomc.repositories.ClienteRepository;
import com.cursomc.services.exceptions.DataIntegrityException;
import com.cursomc.services.exceptions.ObjectNotFoundException;

/*Camada de servicos.
 * responsavel por oferecer operacoes e consultas, 
 * para as camadas controlares rest obs(regras de negocio, nao tem envolvimento com nenhuma tecnologia)
*/
@Service
public class ClienteService {

	private final String msgErro = "Nao e possivel excluir, porque ha entidades relacionadas!";
	@Autowired
	private ClienteRepository repo;

	public Cliente find(Integer id) {
		Cliente obj = repo.findOne(id);
// Tratamento de exceção personalizada...		
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto nao encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}

	/*
	 * Medoto responsavel por criar uma nova cliente
	 */
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	/*
	 * Medoto responsavel por atualizar a cliente
	 */
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(obj , newObj); 
		return repo.save(newObj);
	}

	/*
	 * Medoto responsavel por deletar a cliente
	 */
	public void delete(Integer id) {
		find(id);
		try {
			repo.delete(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(msgErro);
		}

	}

	public List<Cliente> fidAll() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	/*
	 * Medoto responsavel por paginacao e ordenacao dos registros.
	 */
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	/*
	 * Medoto responsavel para converter uma DTO para cliente.
	 */
	public Cliente fromDTO(ClienteDTO obj) {
		return new Cliente(obj.getId(), obj.getNome(), obj.getEmail(), null , null);
	}
	/*
	 * Medoto responsavel para conpletar dados vindo da API com dados vindo da tabela.
	 */
	private void updateData(Cliente obj , Cliente newObj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
