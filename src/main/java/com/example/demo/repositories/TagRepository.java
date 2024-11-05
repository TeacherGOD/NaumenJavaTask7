package com.example.demo.repositories;

import com.example.demo.entities.Tag;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag,Long> {


    Optional<Tag> findByName(String name);
}