package com.senac.diogoboechat.receptor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senac.diogoboechat.receptor.dtos.UsuarioDTO;

public interface UsuarioRepository extends JpaRepository<UsuarioDTO, Integer> {
}
