package com.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cursomc.domain.Estado;


/*Camada de acesso a dados obs(Conversar com o banco de dados , contendo todas as operacoes do 
	banco de dados exemplo consultas, inserts, updates, deletes)
*/

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer>{

}
