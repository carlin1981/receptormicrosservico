package com.senac.diogoboechat.receptor.services;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senac.diogoboechat.receptor.dtos.UsuarioDTO;
import com.senac.diogoboechat.receptor.entities.ObraDeArte;
import com.senac.diogoboechat.receptor.repositories.ObraDeArteRepository;

@Service
public class ObraDeArteService {

    private final ObraDeArteRepository obraDeArteRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public ObraDeArteService(ObraDeArteRepository obraDeArteRepository) {
        this.obraDeArteRepository = obraDeArteRepository;
    }
    
    // Lista apenas obras disponíveis (status = 1)
    public List<ObraDeArte> listarObrasDisponiveis() {
        return obraDeArteRepository.findByStatus(1);
    }

    public ObraDeArte listarObraPorId(int id) {
        return obraDeArteRepository.findById(id).orElse(null);
    }

    public ObraDeArte adicionarObra(ObraDeArte obraDeArte) {
        obraDeArte.setStatus(1); // Define status como ativo ao adicionar
        
        // Enviar uma mensagem de aviso para a fila RabbitMQ
        rabbitTemplate.convertAndSend("fila-galeria", obraDeArte);
        
        return obraDeArteRepository.save(obraDeArte);
    }

    public String apagarObraLogicamente(int id) {
        ObraDeArte obra = obraDeArteRepository.findById(id).orElse(null);
        if (obra == null) {
            return "Obra de arte não encontrada";
        }
        obra.setStatus(-1); // Define status como apagado
        obraDeArteRepository.save(obra);
        return "Obra apagada logicamente com sucesso!";
    }

    public ObraDeArte atualizarObra(int id, ObraDeArte obraDeArte) {
        if (obraDeArteRepository.existsById(id)) {
            return obraDeArteRepository.save(obraDeArte);
        } else {
            return null;
        }
    }
    
    @RabbitListener(queues = "fila-ecommerce")
	private void subscribe(UsuarioDTO usuario) {
    	System.out.println("Novo usuário adicionado: " + usuario.getNome() + " " + usuario.getSobrenome() + ", " + usuario.getTelefone());
	    //System.out.println(usuario);
	}
    
}