package com;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByDni (@Param("dni") String dni);
    Users findByRol (@Param("rol") String rol);
}
