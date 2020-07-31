package com.example.demo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DiceCrab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    //Estableciendose el id d ela tabala para los dados y demas, asi como el tipo de entidad

    private Integer dice1Result;
    private Integer dice2Result;
    private Integer dicesResult;
    //inicializandose las variables de tipo integaer referente a los resultados para cada dado asi como la suma
    //de ambos

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "diceCrab_player_id")
    private PlayerCrab playerCrab;
    //un jugador puede tener varios resultados de dados de ahi larelacion de clase ManyTo One, o sea varios
    //resultados de dados pueden ser arrojados por el mismo jugador

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "diceCrab_game_id")
    private GameCrab gameCrab;
    //un juego puede tener varios resultados de dados de ahi la relacion de clase ManyTo One, o sea varios
    //resultados de dados pueden ser arrojadosen  el mismo juego

    @OneToMany(mappedBy = "diceCrab")
    List<StatusCrab> statusCrabList = new ArrayList<>();
    //En este caso se plantea que un tiro de dados puede se r de varios tipo s segun la regla del juego,
    // y cada uno de dichos tiros refiere una accion o un status de ahi su relacion OneToMany contra
    //la clase StatusCrab

    //CONSTRUCTORS
    public DiceCrab() {}
    public DiceCrab(Integer dice1Result, Integer dice2Result, Integer dicesResult,
                    PlayerCrab playerCrab, GameCrab gameCrab) {
        this.dice1Result = dice1Result;
        this.dice2Result = dice2Result;
        this.dicesResult = dicesResult;
        this.playerCrab = playerCrab;
        this.gameCrab = gameCrab;
        playerCrab.addDicesCrab(this);
    }
    //En est constructor simplemente se inicalizan las variables que en su inicio se setearon en la clase,
    //ademas de accederse al metodo addDicesCrab de la clase playerCrab y pasrle como parametro
    //los valores del constructor en esta clase

    //Getters & Setters
    public void addStatusCrab(StatusCrab statusCrab) {
        statusCrabList.add(statusCrab);
    }
    //metodod inicializado en esta clase proveniente de referente a la clase StatusCrab, especificandose
    //que se le adicionara al listado inicializado en esta clase correspondiente a la misma , cualesquiera
    //ls data que el constructor de la misma pasa en dicho metodo

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDice1Result() {
        return dice1Result;
    }
    public void setDice1Result(Integer dice1Result) {
        this.dice1Result = dice1Result;
    }

    public Integer getDice2Result() {
        return dice2Result;
    }
    public void setDice2Result(Integer dice2Result) {
        this.dice2Result = dice2Result;
    }

    public Integer getDicesResult() {
        return dicesResult;
    }
    public void setDicesResult(Integer dicesResult) {
        this.dicesResult = dicesResult;
    }

    public PlayerCrab getPlayerCrab() {
        return playerCrab;
    }
    public void setPlayerCrab(PlayerCrab playerCrab) {
        this.playerCrab = playerCrab;
    }

    public GameCrab getGameCrab() {
        return gameCrab;
    }
    public void setGameCrab(GameCrab gameCrab) {
        this.gameCrab = gameCrab;
    }

    public List<StatusCrab> getStatusCrabList() {        return statusCrabList;  }
    public void setStatusCrabList(List<StatusCrab> statusCrabList) {  this.statusCrabList = statusCrabList;  }


    @Override
    public String toString() {
        return "DiceCrab{" +
                "id=" + id +
                ", dice1Result=" + dice1Result +
                ", dice2Result=" + dice2Result +
                ", dicesResult=" + dicesResult +
                ", playerCrab=" + playerCrab +
                ", gameCrab=" + gameCrab +
                '}';
    }
}
