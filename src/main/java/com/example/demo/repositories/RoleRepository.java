package com.example.demo.repositories;

import com.example.demo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository

@RepositoryRestResource
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}