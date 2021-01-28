package com;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface SubjectsRepository extends JpaRepository<Subjects, Long> {
    Subjects findByNombre (String nombre);
}
