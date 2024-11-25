package com.example.demo.repositories;

import com.example.demo.entities.Category;
import com.example.demo.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource
public interface CategoryRepository extends CrudRepository<Category, Long> {

    Category findByName(String name);

    List<Category> findAllByUser(User user);
}