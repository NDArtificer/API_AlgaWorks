package com.artificer.osworks.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.artificer.osworks.api.model.Comentario;
import com.artificer.osworks.api.model.ComentarioInput;
import com.artificer.osworks.api.model.ComentarioModel;
import com.artificer.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.artificer.osworks.domain.model.OrdemServico;
import com.artificer.osworks.domain.repository.OrdemServicoRepository;
import com.artificer.osworks.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentarios")
public class CometarioController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServicoService;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	
	@Autowired
	private ModelMapper modelmapper;
	
	
	@GetMapping
	public List<ComentarioModel> listar(@PathVariable Long ordemServicoId){
		OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(()-> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada!"));
		
		return toColletionModel(ordemServico.getComentarios());
	}
	
	private List<ComentarioModel> toColletionModel(List<Comentario> comentarios) {
		// TODO Auto-generated method stub
		return comentarios.stream()
				.map(comentario -> toModel(comentario))
				.collect(Collectors.toList());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioModel adicionar(@PathVariable Long ordemServicoId, 
			@Valid @RequestBody ComentarioInput comentarioInput) {
		Comentario comentario = gestaoOrdemServicoService.adcionarComentario(ordemServicoId, comentarioInput.getDescricao());
		
		return toModel(comentario);
	}


	private ComentarioModel toModel(Comentario comentario) {
		// TODO Auto-generated method stub
		return modelmapper.map(comentario, ComentarioModel.class);
	}
	
	
}

