package com.cursomc.services;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cursomc.domain.Cidade;
import com.cursomc.domain.Cliente;
import com.cursomc.domain.Endereco;
import com.cursomc.domain.enums.Perfil;
import com.cursomc.domain.enums.TipoCliente;
import com.cursomc.dto.ClienteDTO;
import com.cursomc.dto.ClienteNewDTO;
import com.cursomc.repositories.ClienteRepository;
import com.cursomc.repositories.EnderecoRepository;
import com.cursomc.security.UserSS;
import com.cursomc.services.exceptions.AuthorizationException;
import com.cursomc.services.exceptions.DataIntegrityException;
import com.cursomc.services.exceptions.ObjectNotFoundException;

/*Camada de servicos.
 * responsavel por oferecer operacoes e consultas, 
 * para as camadas controlares rest obs(regras de negocio, nao tem envolvimento com nenhuma tecnologia)
*/
@Service
public class ClienteService {

	private final String msgErro = "Nao e possivel excluir, porque ha pedidos relacionadas!";
	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private BCryptPasswordEncoder pe;
	@Autowired
	private S3Service s3Service;

	public Cliente find(Integer id) {
		Cliente obj = repo.findOne(id);

		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}

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
		repo.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	}

	/*
	 * Medoto responsavel por atualizar a cliente
	 */
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(obj, newObj);
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
		return new Cliente(obj.getId(), obj.getNome(), obj.getEmail(), null, null, null);
	}

	/*
	 * Medoto responsavel para converter um ClienteNewDTO.
	 */
	public Cliente fromDTO(ClienteNewDTO obj) {
		Cliente cli = new Cliente(null, obj.getNome(), obj.getEmail(), obj.getCpfOuCnpj(),
				TipoCliente.toEnum(obj.getTipo()), pe.encode(obj.getSenha()));
		Cidade cid = new Cidade(obj.getCidadeId(), null, null);
		Endereco end = new Endereco(null, obj.getLogradouro(), obj.getComplemento(), obj.getBairro(),
				obj.getComplemento(), cli, cid, obj.getCidadeId());
		cli.getEnderecos().add(end);
		cli.getTelefone().add(obj.getTelefone1());
		System.out.print("Telefone 1" + obj.getTelefone1() + "Telefone 2" + obj.getTelefone2());
		if (obj.getTelefone2() != null) {
			cli.getTelefone().add(obj.getTelefone2());
		}
		if (obj.getTelefone3() != null) {
			cli.getTelefone().add(obj.getTelefone3());
		}

		return cli;
	}

	/*
	 * Medoto responsavel para conpletar dados vindo da API com dados vindo da
	 * tabela.
	 */
	private void updateData(Cliente obj, Cliente newObj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	/*
	 * Medoto responsavel para gravar imagem na tabela.
	 */
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		URI uri = s3Service.uploadFile(multipartFile);
		Cliente cli = repo.findOne(user.getId());
		cli.setImageUrl(uri.toString());
		repo.save(cli);
		return uri;
	}
}
