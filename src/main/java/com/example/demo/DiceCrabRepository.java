package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface DiceCrabRepository extends JpaRepository<DiceCrab,Long> {

}
//inicializandose el repositorio d ela clase
