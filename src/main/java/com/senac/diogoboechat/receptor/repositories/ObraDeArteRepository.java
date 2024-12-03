package com.senac.diogoboechat.receptor.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.senac.diogoboechat.receptor.entities.ObraDeArte;

public interface ObraDeArteRepository extends JpaRepository<ObraDeArte, Integer> {
	List<ObraDeArte> findByStatus(int status);

}