package com.example.demo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ScoreCrab {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private Double ScoreCrabImport = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerCrab_id")
    private PlayerCrab playerCrab;


    @OneToMany(mappedBy = "scoreCrab")
    List<GameCrab> gameCrab = new ArrayList<>();


    public ScoreCrab() {
    }

    public ScoreCrab(Double scoreCrabImport, PlayerCrab playerCrab) {
        playerCrab.addScoreCrab(this);
        this.ScoreCrabImport = scoreCrabImport;
        this.playerCrab = playerCrab;
    }

    //Getters & Setters
public void addGame(GameCrab gameCrab1){
        gameCrab.add(gameCrab1);
}
    public Long getId() {  return id; }
    public void setId(Long id) {  this.id = id;}

    public Double getScoreCrabImport() {  return ScoreCrabImport;}
    public void setScoreCrabImport(Double scoreCrabImport) {ScoreCrabImport = scoreCrabImport; }

    public PlayerCrab getPlayerCrab() { return playerCrab;}
    public void setPlayerCrab(PlayerCrab playerCrab) {  this.playerCrab = playerCrab; }

//    public GameCrab getGameCrab() {  return gameCrab;}
//    public void setGameCrab(GameCrab gameCrab) {   this.gameCrab = gameCrab; }

    public void setGameCrab(List<GameCrab> gameCrab) {
        this.gameCrab = gameCrab;
    }

    @Override
    public String toString() {
        return "ScoreCrab{" +
                "id=" + id +
                ", ScoreCrabImport=" + ScoreCrabImport +
                ", playerCrab=" + playerCrab +
                ", gameCrab=" + gameCrab +
                '}';
    }
}
