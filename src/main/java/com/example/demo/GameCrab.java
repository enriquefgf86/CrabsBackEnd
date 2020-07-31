package com.example.demo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.*;

@Entity
public class GameCrab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;

    private Date gameDate;

//    private String gameCrabState;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerCrab_id")
    private PlayerCrab playerCrab;

    @OneToMany(mappedBy = "gameCrab")
    List<DiceCrab> diceCrabList = new ArrayList<>();

    @OneToMany(mappedBy = "gameCrab")
    List<StatusCrab> statusCrabList = new ArrayList<>();



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameCrab_id")
    private ScoreCrab scoreCrab;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinTable(name = "Score_Per_Game",
//            joinColumns = {@JoinColumn(name = "gameCrab_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "scoreCrab_id" , referencedColumnName = "id")})
//    private ScoreCrab scoreCrab;

//    @OneToOne(mappedBy = "gameCrab")
//    private ScoreCrab scoreCrab;

    public GameCrab() {
    }

    public GameCrab(Date gameDate, PlayerCrab playerCrab,ScoreCrab scoreCrab) {
        this.gameDate = gameDate;
        this.playerCrab=playerCrab;
        this.scoreCrab = scoreCrab;
        this.scoreCrab.addGame(this);
        playerCrab.addGameCrab(this);


    }

    //Getters & Setters

    public void addStatusCrab(StatusCrab statusCrab) {
        statusCrabList.add(statusCrab);
    }
//    public void addScoreCrab(ScoreCrab scoreCrab1){
//        scoreCrab=scoreCrab1;
//    }
//    public ScoreCrab getNewScoreCrab(){return scoreCrab;}

//    public List<StatusCrab>getAddStatusCrabList(){return statusCrabList;}

    public void addDiceResult(DiceCrab diceCrab) {
        diceCrabList.add(diceCrab);
    }
//    public List<DiceCrab>getAddDiceCrabList(){return diceCrabList;}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    public PlayerCrab getPlayerCrab() {
        return playerCrab;
    }

    public void setPlayerCrab(PlayerCrab playerCrab) {
        this.playerCrab = playerCrab;
    }

    public List<DiceCrab> getDiceCrabList() {
        return diceCrabList;
    }

    public void setDiceCrabList(List<DiceCrab> diceCrabList) {
        this.diceCrabList = diceCrabList;
    }

    public ScoreCrab getScoreCrab() {
        return scoreCrab;
    }

    public void setScoreCrab(ScoreCrab scoreCrab) {
        this.scoreCrab = scoreCrab;
    }

//    public String getGameCrabState() {
//        return gameCrabState;
//    }

//    public void setGameCrabState(String gameCrabState) {
//        this.gameCrabState = gameCrabState;
//    }

    public List<StatusCrab> getStatusCrabList() {
        return statusCrabList;
    }

    public void setStatusCrabList(List<StatusCrab> statusCrabList) {
        this.statusCrabList = statusCrabList;
    }


    @Override
    public String toString() {
        return "GameCrab{" +
                "Id=" + Id +
                ", gameDate=" + gameDate +
                ", playerCrab=" + playerCrab +
                ", diceCrabList=" + diceCrabList +
                ", statusCrabList=" + statusCrabList +
                ", scoreCrab=" + scoreCrab +
                '}';
    }
}
