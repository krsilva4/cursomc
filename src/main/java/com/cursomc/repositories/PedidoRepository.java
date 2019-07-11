package com.cursomc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cursomc.domain.Cliente;
import com.cursomc.domain.Pedido;

/*Camada de acesso a dados obs(Conversar com o banco de dados , contendo todas as operacoes do 
	banco de dados exemplo consultas, inserts, updates, deletes)
*/

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	@Transactional(readOnly=true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
}
