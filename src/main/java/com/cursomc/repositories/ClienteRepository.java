package com.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cursomc.domain.Cliente;;

/*Camada de acesso a dados obs(Conversar com o banco de dados , contendo todas as operacoes do 
	banco de dados exemplo consultas, inserts, updates, deletes)
*/

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    //Metodo responsavel de verificar se existe cliente para tal email.
	@Transactional(readOnly = true)
	Cliente findByEmail(String email);
}
