package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface PlayerCrabRepository extends JpaRepository<PlayerCrab, String> {

PlayerCrab findByUserName(String userName);


//    PlayerCrab findById(Long Id);

//
//    PlayerCrab findUserByScore(Integer scoreCrab);
}
