package com.backend.servicio.usuarios.models.repository;

import com.backend.servicio.usuarios.models.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelefonoRepository extends JpaRepository<Telefono,String> {
}
