package com.example.demo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class PlayerCrab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;

    private String userName;
    private String userPassword;

    @OneToMany(mappedBy = "playerCrab", fetch = FetchType.EAGER)
    Set<GameCrab> gameCrabSet = new HashSet<>();

    @OneToMany(mappedBy = "playerCrab")
    List<DiceCrab> diceCrabList = new ArrayList<>();

    @OneToMany(mappedBy = "playerCrab", fetch = FetchType.EAGER)
    List<ScoreCrab> scoreCrabList = new ArrayList<>();

    @OneToMany(mappedBy = "playerCrab" )
    List<StatusCrab> statusCrabList = new ArrayList<>();

    public PlayerCrab() {
    }

    public PlayerCrab(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }


    //Getters & Setters


    public void addStatusCrab(StatusCrab statusCrab){statusCrabList.add(statusCrab);}
    public List<StatusCrab>getAddStatusCrabList(){return statusCrabList;}

    public void addGameCrab(GameCrab gameCrab){gameCrabSet.add(gameCrab);}
    public Set<GameCrab>getAddGameCrabSet(){return gameCrabSet;}

    public void  addDicesCrab(DiceCrab diceCrab){diceCrabList.add(diceCrab);}
//    public List<DiceCrab>getAddDiceCrab(){return diceCrabList;}

    public void addScoreCrab(ScoreCrab scoreCrab){scoreCrabList.add(scoreCrab);}
    public List<ScoreCrab>getAddScoreCrabList(){return scoreCrabList; }

    public Long getId() { return Id; }
    public void setId(Long id) { this.Id = id; }

    public String getuserName() { return userName; }
    public void setuserName(String userName) {this.userName = userName; }

    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) {  this.userPassword = userPassword;    }

    public Set<GameCrab> getGameCrabSet() {return gameCrabSet; }
    public void setGameCrabSet(Set<GameCrab> gameCrabSet) { this.gameCrabSet = gameCrabSet; }

    public List<DiceCrab> getDiceCrabList() { return diceCrabList;}
    public void setDiceCrabList(List<DiceCrab> diceCrabList) { this.diceCrabList = diceCrabList; }

    public List<ScoreCrab> getScoreCrabList() {return scoreCrabList;}
    public void setScoreCrabList(List<ScoreCrab> scoreCrabList) {this.scoreCrabList = scoreCrabList;}

    public List<StatusCrab> getStatusCrabList() {return statusCrabList; }
    public void setStatusCrabList(List<StatusCrab> statusCrabList) {this.statusCrabList = statusCrabList;}

    //Overrride


    @Override
    public String toString() {
        return "PlayerCrab{" +
                "Id=" + Id +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", gameCrabSet=" + gameCrabSet +
                ", diceCrabList=" + diceCrabList +
                ", scoreCrabList=" + scoreCrabList +
                ", statusCrabList=" + statusCrabList +
                '}';
    }
}
