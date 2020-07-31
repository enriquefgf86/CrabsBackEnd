package com.example.demo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StatusCrab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String status;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="playerCrab_id")
    private PlayerCrab playerCrab;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="gameCrab_id")
    private GameCrab gameCrab;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="diceCrab_id")
    private DiceCrab diceCrab;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinTable(name = "Status_Per_Dice_Result",
//            joinColumns = {@JoinColumn(name = "diceCrab_id")},
//            inverseJoinColumns = {@JoinColumn(name = "statusCrab_id")})
//    private DiceCrab diceCrab;

//    @OneToOne(mappedBy = "statusCrab")
//    private DiceCrab diceCrab;

    public StatusCrab(){}
    public StatusCrab(String status
            ,PlayerCrab playerCrab,GameCrab gameCrab
            ,DiceCrab diceCrab
//                      ,StateCase stateCase
    ) {
        this.status = status;
        this.playerCrab=playerCrab;
        this.gameCrab=gameCrab;
        this.diceCrab=diceCrab;
       this.playerCrab.addStatusCrab(this);
        this.gameCrab.addStatusCrab(this);
//        this.stateCase=stateCase;
//        stateCase.addStatus(this);
        this.diceCrab.addStatusCrab(this);

    }


    //Getters & Setters

    public GameCrab getGameCrab() {   return gameCrab; }
    public void setGameCrab(GameCrab gameCrab) {this.gameCrab = gameCrab;}

    public Long getId() {   return id; }
    public void setId(Long id) {  this.id = id;}

    public String getStatus() {   return status; }
    public void setStatus(String status) {  this.status = status;}

    public PlayerCrab getPlayerCrab() {   return playerCrab;}
    public void setPlayerCrab(PlayerCrab playerCrab) {  this.playerCrab = playerCrab; }

    public GameCrab getGamerab() {return gameCrab;}
    public void setGamerab(GameCrab gameCrab) {this.gameCrab = gameCrab; }

    public DiceCrab getDiceCrab() {  return diceCrab;}
    public void setDiceCrab(DiceCrab diceCrab) {   this.diceCrab = diceCrab;}

    @Override
    public String toString() {
        return "StatusCrab{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", playerCrab=" + playerCrab +
                ", gameCrab=" + gameCrab +
                ", diceCrab=" + diceCrab +
                '}';
    }
}
