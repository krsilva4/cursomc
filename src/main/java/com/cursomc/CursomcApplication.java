package com.cursomc;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cursomc.domain.Categoria;
import com.cursomc.domain.Cidade;
import com.cursomc.domain.Cliente;
import com.cursomc.domain.Endereco;
import com.cursomc.domain.Estado;
import com.cursomc.domain.Produto;
import com.cursomc.domain.enums.TipoCliente;
import com.cursomc.repositories.CategoriaRepository;
import com.cursomc.repositories.CidadeRepository;
import com.cursomc.repositories.ClienteRepository;
import com.cursomc.repositories.EnderecoRepository;
import com.cursomc.repositories.EstadoRepository;
import com.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepositorio;

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private ClienteRepository clinteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

		Categoria cat1 = new Categoria(null, "Informatica");
		Categoria cat2 = new Categoria(null, "Escritorio");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		categoriaRepositorio.save(Arrays.asList(cat1, cat2));
		produtoRepository.save(Arrays.asList(p1, p2, p3));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "Sao Paulo");

		Cidade cid1 = new Cidade(null, "Uberlandia", est1);
		Cidade cid2 = new Cidade(null, "Sao Paulo", est2);
		Cidade cid3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().addAll(Arrays.asList(cid1));
		est2.getCidades().addAll(Arrays.asList(cid2, cid3));
		
		estadoRepository.save(Arrays.asList(est1,est2));
		cidadeRepository.save(Arrays.asList(cid1,cid2,cid3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "90909090909", TipoCliente.PESSOAFISICA);
		cli1.getTelefone().addAll(Arrays.asList("9999-9999", "98888-9999"));
		Endereco end1 = new Endereco(null, "Rua flores","Perto do hospital", "Cavaleiro", "7687676-09", cli1, cid1 , 400);
		Endereco end2 = new Endereco(null, "Avenida Matos","Perto do hospital", "Cond da Boa Vista", "9090900-09", cli1, cid2 ,600);
		cli1.getEnderecos().addAll(Arrays.asList(end1, end2));
		
		clinteRepository.save(Arrays.asList(cli1));
		enderecoRepository.save(Arrays.asList(end1,end2));
	}

}
