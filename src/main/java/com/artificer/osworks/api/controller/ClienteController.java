package com.artificer.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.artificer.osworks.api.model.ClienteModel;
import com.artificer.osworks.domain.model.Cliente;
import com.artificer.osworks.domain.repository.ClienteRepository;
import com.artificer.osworks.domain.service.CadastroClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CadastroClienteService cadastroCliente;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<ClienteModel> listar() {
		return toCollectionModel(clienteRepository.findAll());
		
	}

	private List<ClienteModel> toCollectionModel(List<Cliente> clientes) {
		// TODO Auto-generated method stub
		return  clientes.stream()
				.map(cliente -> toModel(cliente))
				.collect(Collectors.toList());
	}

	@GetMapping("/{clienteId}")
	public ResponseEntity<ClienteModel> buscar(@PathVariable Long clienteId) {

		Optional<Cliente> cliente = clienteRepository.findById(clienteId);

		if (!cliente.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		ClienteModel clienteModel = toModel(cliente.get());
		return ResponseEntity.ok(clienteModel);
	}

	private ClienteModel toModel(Cliente cliente) {
		return modelMapper.map(cliente, ClienteModel.class);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
		return cadastroCliente.salvar(cliente);
	}

	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> atualizar(@Valid @PathVariable Long clienteId, @RequestBody Cliente cliente) {

		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}

		cliente.setId(clienteId);
		cliente = cadastroCliente.salvar(cliente);

		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping("/{clienteId} ")
	public ResponseEntity<Void> deletar(@PathVariable Long clienteId) {

		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}

		cadastroCliente.excluir(clienteId);

		return ResponseEntity.noContent().build();
	}

}
