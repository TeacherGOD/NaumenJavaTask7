package com.example.demo.repositories;

import com.example.demo.entities.Tag;

import com.example.demo.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

@RepositoryRestResource
public interface TagRepository extends CrudRepository<Tag,Long> {


    Optional<Tag> findByName(String name);

    List<Tag> findAllByUser(User currentUser);

}
