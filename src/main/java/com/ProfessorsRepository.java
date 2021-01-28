package com;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProfessorsRepository extends JpaRepository<Professors, Long> {
    Professors findByNombre (@Param("nombre") String nombre);
}
