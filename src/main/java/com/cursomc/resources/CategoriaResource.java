package com.cursomc.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cursomc.domain.Categoria;
import com.cursomc.dto.CategoriaDTO;
import com.cursomc.services.CategoriaService;

/*Camada controlares rest - responsavel para envio de 
informacoes para as aplicacoes.
*/

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;

	/*
	 * Medoto responsavel por realizar consulta por ID.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}

	/*
	 * Medoto responsavel por insert novo obj.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Validated @RequestBody CategoriaDTO objDTO) {
		Categoria obj = service.fromDTO(objDTO);
		obj = service.insert(obj);
		// Retornando String do obj cadastrado, continuando no windows.
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Validated @RequestBody CategoriaDTO objDTO, @PathVariable Integer id) {
		Categoria obj = service.fromDTO(objDTO);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}

	/*
	 * Medoto responsavel por realizar delete por ID.
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();

	}

	/*
	 * Medoto responsavel por listar todas as categorias, e para retirar os produtos
	 * da categoria e feita uma convercao para categoriaDTO para utilizar os campos
	 * que enterezao...
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		List<Categoria> list = service.fidAll();
		List<CategoriaDTO> listDTO = new ArrayList<CategoriaDTO>();
		for (Categoria c : list) {
			CategoriaDTO catTDO = new CategoriaDTO(c);
			listDTO.add(catTDO);
		}
		return ResponseEntity.ok().body(listDTO);
	}

	/*
	 * Medoto responsavel por listar todas as categorias por pagina, e para retirar
	 * os produtos da categoria e feita uma convercao para categoriaDTO para
	 * utilizar os campos que enterezao...
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listDTO = list.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(listDTO);
	}

}
