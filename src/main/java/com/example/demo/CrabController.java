package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class CrabController {

    @Autowired
    DiceCrabRepository diceCrabRepository;
    @Autowired
    GameCrabRepository gameCrabRepository;
    @Autowired
    PlayerCrabRepository playerCrabRepository;
    @Autowired
    ScoreCrabRepository scoreCrabRepository;
    @Autowired
    StatusCrabRepository statusCrabRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public enum State {KEEPS, WINS, LOOSES}

    private Random randomDiceShots = new Random();
    private final static int TWICE_1 = 2;
    private final static int THREE = 3;
    private final static int SEVEN = 7;
    private final static int ELEVEN = 11;
    private final static int TWELVE = 12;

    public int shot1;
    public int shot2;
    public List<Integer> sumsResult = new ArrayList<>();


    public int shotDices() {
//        sumsResult.add(shot1 + shot2);
        // elige valores aleatorios para los dados
        int dice1 = 1 + randomDiceShots.nextInt(6); // primer tiro del dice
        int dice2 = 1 + randomDiceShots.nextInt(6); // segundo tiro del dice
        int sum = dice1 + dice2;
        shot1 = dice1;
        shot2 = dice2;

        return sum;
    }

    //====================================================================================================
    //trayendo todos los juegos con usuario identificado o no
    //====================================================================================================
    @RequestMapping(value = "/crabs/game/all", method = RequestMethod.GET)
    public Map<String, Object> getAllgames(Authentication authentication) {
        Map<String, Object> dto = new HashMap<>();

        dto.put("all_games", gameCrabRepository.findAll().stream().map(gameCrab -> makeAllGamesDTO(gameCrab)).collect(Collectors.toList()));
        if (authentication == null) {
            dto.put("player", null);
        } else {
            dto.put("player", makePlayerDTO(playerLoggedDetails(authentication)));

        }

        return dto;
    }

    //==========================================================
    //creando nuevo por usuario autenticado
    //==========================================================
    @RequestMapping(value = "/crabs/game/create", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createNewCrabGame(Authentication authentication) {

        PlayerCrab playerCrab = playerLoggedDetails(authentication);
        if (authentication == null) {
            return new ResponseEntity<>(makeMapCreateGameCrab("Error", "you should be looged in!"), HttpStatus.FORBIDDEN);
        }
        if (playerCrab == null) {
            return new ResponseEntity<>(
                    makeMapCreateGameCrab("Error", " please try with other player"), HttpStatus.FORBIDDEN);
        }
        String state = "Keeps";

        ScoreCrab scoreCrab = scoreCrabRepository.getOne(playerCrab.getId());

        scoreCrab.setScoreCrabImport(0.0);

        GameCrab game = new GameCrab(new Date(), playerCrab, scoreCrab);

        DiceCrab diceDefault = new DiceCrab(0, 0, 0, playerCrab, game);

        StatusCrab statusCrab = new StatusCrab(state, playerCrab, game, diceDefault);

        gameCrabRepository.save(game);
        scoreCrabRepository.save(scoreCrab);
        diceCrabRepository.save(diceDefault);
        statusCrabRepository.save(statusCrab);

        return new ResponseEntity<>(makeMapCreateGameCrab("idCreated ", game.getId()), HttpStatus.CREATED);
    }

    private Map<String, Object> makeMapCreateGameCrab(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    //====================================================================================================
    //accediendo al juego seleccionado en cuestion
    //====================================================================================================
    @RequestMapping(value = "/crabs/game/{gameCrabId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getGameselected(@PathVariable("gameCrabId") Long id, Authentication authentication) {
        Map<String, Object> dto = new HashMap<>();

        PlayerCrab playerCrab = playerLoggedDetails(authentication);

        if (authentication == null) {
            return new ResponseEntity<>(makeResponseEntity("Error", "No Player Authenticated"), HttpStatus.UNAUTHORIZED);

        }
        if (playerCrab == null) {
            return new ResponseEntity<>(makeResponseEntity("Error", "No Player Authenticated,Register the Player"), HttpStatus.UNAUTHORIZED);

        }

        GameCrab gameCrab = gameCrabRepository.getOne(id);

        dto.put("game_id", gameCrab.getId());
        dto.put("game_status", gameCrab.getStatusCrabList().stream().map(statusCrab -> makeStatusDTO(statusCrab)).collect(Collectors.toList()));
        dto.put("shots", gameCrab.getDiceCrabList().stream().map(diceCrab -> makeDiceCrabDto(diceCrab)).collect(Collectors.toList()));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //====================================================================================================
    //disparo de dados aleatorio
    //====================================================================================================
    @RequestMapping(value = "crabs/game/play/{gameDiceId}", method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<Map<String, Object>> throwDice(Authentication authentication, @PathVariable("gameDiceId") Long idGame
    ) {
        Map<String, Object> dto = new HashMap<>();

        PlayerCrab playerCrab = playerLoggedDetails(authentication);//Jugador autentificado para poder jugar

        GameCrab gameCrab = gameCrabRepository.getOne(idGame);//juego en cuestion para poder jugar

        int winPoint = 0;

        State state;

        String stateGame;
        int diceSum = shotDices();


        ScoreCrab scoreCrabImport = scoreCrabRepository.getOne(playerCrab.getId());

        StatusCrab lastState = gameCrab.getStatusCrabList().get(gameCrab.getStatusCrabList().size() - 1);


        if (authentication == null) {
            return new ResponseEntity<>(makeResponseEntity("Error", "No Player Authenticated"), HttpStatus.UNAUTHORIZED);
        }

        if (authentication != null && playerCrab != gameCrab.getPlayerCrab()) {
            return new ResponseEntity<>(makeResponseEntity("Error", "This Player isn't authorized to play on this game"), HttpStatus.UNAUTHORIZED);
        }

        if (lastState.getStatus() == "Wins" || lastState.getStatus() == "Looses") {
            return new ResponseEntity<>(makeResponseEntity("Error", "This game already finishes "), HttpStatus.UNAUTHORIZED);
        }


        lastState.getStatus();
        System.out.println(sumsResult);
        if (lastState.getStatus() == "Keeps") {
            //caso 1
            //lenght 0 disparo de 7 u 11 a la primera
            sumsResult.add(diceSum);
            if (diceSum == SEVEN && sumsResult.size() == 1 || diceSum == ELEVEN && sumsResult.size() == 1) {
                state = State.WINS;
                stateGame = "Wins";
                scoreCrabImport.setScoreCrabImport(1.0);


                ScoreCrab scoreCrab = new ScoreCrab(scoreCrabImport.getScoreCrabImport(), playerCrab);
                DiceCrab diceCrabShot = new DiceCrab(shot1, shot2, diceSum, playerCrab, gameCrab);
                StatusCrab statusCrabDone = new StatusCrab(stateGame, playerCrab, gameCrab, diceCrabShot);

                gameCrab.getStatusCrabList().add(statusCrabDone);
                gameCrab.getDiceCrabList().add(diceCrabShot);

                diceCrabRepository.save(diceCrabShot);
                statusCrabRepository.save(statusCrabDone);
                scoreCrabRepository.save(scoreCrab);

                sumsResult = new ArrayList<>();
            }
            //caso 2
            //lenght 0 disparo de 2 ,3,u 12 a la primera
            else if (diceSum == TWELVE && sumsResult.size() == 1 || diceSum == TWICE_1 && sumsResult.size() == 1 || diceSum == THREE && sumsResult.size() == 1) {
                state = State.LOOSES;
                stateGame = "Looses";
                scoreCrabImport.setScoreCrabImport(0.0);

                ScoreCrab scoreCrab = new ScoreCrab(scoreCrabImport.getScoreCrabImport(), playerCrab);
                DiceCrab diceCrabShot = new DiceCrab(shot1, shot2, diceSum, playerCrab, gameCrab);
                StatusCrab statusCrabDone = new StatusCrab(stateGame, playerCrab, gameCrab, diceCrabShot);

                gameCrab.getStatusCrabList().add(statusCrabDone);
                gameCrab.getDiceCrabList().add(diceCrabShot);

                diceCrabRepository.save(diceCrabShot);
                statusCrabRepository.save(statusCrabDone);
                scoreCrabRepository.save(scoreCrab);
                sumsResult = new ArrayList<>();
            }
            //caso 3
            //lenght mayor 0 disparo de 7
            else if (diceSum == SEVEN && sumsResult.size() > 1) {

                state = State.LOOSES;
                stateGame = "Looses";
                scoreCrabImport.setScoreCrabImport(0.0);

                ScoreCrab scoreCrab = new ScoreCrab(scoreCrabImport.getScoreCrabImport(), playerCrab);
                DiceCrab diceCrabShot = new DiceCrab(shot1, shot2, diceSum, playerCrab, gameCrab);
                StatusCrab statusCrabDone = new StatusCrab(stateGame, playerCrab, gameCrab, diceCrabShot);

                gameCrab.getStatusCrabList().add(statusCrabDone);
                gameCrab.getDiceCrabList().add(diceCrabShot);

                diceCrabRepository.save(diceCrabShot);
                statusCrabRepository.save(statusCrabDone);
                scoreCrabRepository.save(scoreCrab);
                sumsResult = new ArrayList<>();
            }
            //caso 4
            //length mayor 0 disparo 4,5,6,78,9,10

            else if (diceSum != SEVEN && sumsResult.size() > 1) {
                if (sumsResult.get(0) == diceSum) {
                    state = State.WINS;
                    stateGame = "Wins";
                    scoreCrabImport.setScoreCrabImport(1.0);


                    ScoreCrab scoreCrab = new ScoreCrab(scoreCrabImport.getScoreCrabImport(), playerCrab);
                    DiceCrab diceCrabShot = new DiceCrab(shot1, shot2, diceSum, playerCrab, gameCrab);
                    StatusCrab statusCrabDone = new StatusCrab(stateGame, playerCrab, gameCrab, diceCrabShot);

                    gameCrab.getStatusCrabList().add(statusCrabDone);
                    gameCrab.getDiceCrabList().add(diceCrabShot);

                    diceCrabRepository.save(diceCrabShot);
                    statusCrabRepository.save(statusCrabDone);
                    scoreCrabRepository.save(scoreCrab);
                    sumsResult = new ArrayList<>();
                } else {
                    stateGame = "Keeps";
                    state = State.KEEPS;
                    DiceCrab diceCrabShot2 = new DiceCrab(shot1, shot2, diceSum, playerCrab, gameCrab);
                    StatusCrab statusCrabDone2 = new StatusCrab(stateGame, playerCrab, gameCrab, diceCrabShot2);

                    gameCrab.getStatusCrabList().add(statusCrabDone2);
                    gameCrab.getDiceCrabList().add(diceCrabShot2);

                    diceCrabRepository.save(diceCrabShot2);
                    statusCrabRepository.save(statusCrabDone2);
                }

            }

            //case 5
            //lenght 0 disparo 4,5,6,8,9,10 mantiene y disparo es win point
            else if (diceSum != TWELVE || diceSum != TWICE_1 || diceSum != THREE || diceSum != SEVEN && sumsResult.size() == 1) {
                winPoint = diceSum;
//                sumsResult.add(diceSum);
                stateGame = "Keeps";

                DiceCrab diceCrabShot2 = new DiceCrab(shot1, shot2, diceSum, playerCrab, gameCrab);
                StatusCrab statusCrabDone2 = new StatusCrab(stateGame, playerCrab, gameCrab, diceCrabShot2);

                gameCrab.getStatusCrabList().add(statusCrabDone2);
                gameCrab.getDiceCrabList().add(diceCrabShot2);

                diceCrabRepository.save(diceCrabShot2);
                statusCrabRepository.save(statusCrabDone2);
            }


            gameCrabRepository.save(gameCrab);
            dto.put("player_name", gameCrab.getPlayerCrab().getuserName());
            dto.put("current_game_id", gameCrab.getId());
            dto.put("score_in_game", scoreCrabImport.getScoreCrabImport());
            dto.put("story_scores_player", playerCrab.getScoreCrabList().stream().map(scoreCrab -> makeScoreDTO(scoreCrab)).collect(Collectors.toList()));

            return new ResponseEntity<>(makeResponseEntity("Success", "Dice thrown"), HttpStatus.CREATED);
        } else


            return new ResponseEntity<>(makeResponseEntity("Error", "This game is Done"), HttpStatus.CONFLICT);
    }


//        if (lastState.getStatus() == "Keeps") {
//            switch (diceSum) {
//                case SEVEN:
//                case ELEVEN:
//                    state = State.WINS;
//                    stateGame = "Wins";
//                    scoreCrabImport.setScoreCrabImport(1.0);
//
//
//                    ScoreCrab scoreCrab = new ScoreCrab(scoreCrabImport.getScoreCrabImport(), playerCrab);
//                    DiceCrab diceCrabShot = new DiceCrab(shot1, shot2, diceSum, playerCrab, gameCrab);
//                    StatusCrab statusCrabDone = new StatusCrab(stateGame, playerCrab, gameCrab, diceCrabShot);
//
//                    gameCrab.getStatusCrabList().add(statusCrabDone);
//                    gameCrab.getDiceCrabList().add(diceCrabShot);
//
//                    diceCrabRepository.save(diceCrabShot);
//                    statusCrabRepository.save(statusCrabDone);
//                    scoreCrabRepository.save(scoreCrab);
//
//
//                    break;
//
//                case TWICE_1:
//                case THREE:
//                case TWELVE:
//                    state = State.LOOSES;
//                    stateGame = "Looses";
//                    scoreCrabImport.setScoreCrabImport(0.0);
//
//                    ScoreCrab scoreCrab1 = new ScoreCrab(scoreCrabImport.getScoreCrabImport(), playerCrab);
//                    DiceCrab diceCrabShot1 = new DiceCrab(shot1, shot2, diceSum, playerCrab, gameCrab);
//                    StatusCrab statusCrabDone1 = new StatusCrab(stateGame, playerCrab, gameCrab, diceCrabShot1);
//
//                    gameCrab.getStatusCrabList().add(statusCrabDone1);
//                    gameCrab.getDiceCrabList().add(diceCrabShot1);
//
//                    diceCrabRepository.save(diceCrabShot1);
//                    statusCrabRepository.save(statusCrabDone1);
//                    scoreCrabRepository.save(scoreCrab1);
//                    break;
//
//                default:
//                    state = State.KEEPS;
//
//                    winPoint = diceSum;
//                    stateGame = "Keeps";
//
//                    DiceCrab diceCrabShot2 = new DiceCrab(shot1, shot2, diceSum, playerCrab, gameCrab);
//                    StatusCrab statusCrabDone2 = new StatusCrab(stateGame, playerCrab, gameCrab, diceCrabShot2);
//
//                    gameCrab.getStatusCrabList().add(statusCrabDone2);
//                    gameCrab.getDiceCrabList().add(diceCrabShot2);
//
//                    diceCrabRepository.save(diceCrabShot2);
//                    statusCrabRepository.save(statusCrabDone2);
//                    break;
//            }
//
//
//            while (state == State.KEEPS) {
//
//                if (diceSum == winPoint) {
//                    state = State.WINS;
//                    scoreCrabImport.setScoreCrabImport(1.0);
//                    ScoreCrab scoreCrab2 = new ScoreCrab(scoreCrabImport.getScoreCrabImport(), playerCrab);
//                    scoreCrabRepository.save(scoreCrab2);
//
//                } else if (diceSum == SEVEN) {
//                    state = State.LOOSES;
//                    scoreCrabImport.setScoreCrabImport(0.0);
//                    ScoreCrab scoreCrab3 = new ScoreCrab(scoreCrabImport.getScoreCrabImport(), playerCrab);
//                    scoreCrabRepository.save(scoreCrab3);
//
//                }
//            }
//            gameCrabRepository.save(gameCrab);
//            dto.put("player_name", gameCrab.getPlayerCrab().getuserName());
//            dto.put("current_game_id", gameCrab.getId());
//            dto.put("score_in_game", scoreCrabImport.getScoreCrabImport());
//            dto.put("story_scores_player", playerCrab.getScoreCrabList().stream().map(scoreCrab -> makeScoreDTO(scoreCrab)).collect(Collectors.toList()));
//
//            return new ResponseEntity<>(makeResponseEntity("Success", "Dice thrown"), HttpStatus.CREATED);
//        } else
//
//
//            return new ResponseEntity<>(makeResponseEntity("Error", "This game is Done"), HttpStatus.CONFLICT);
//
//    }

    //==============================================================================================
    //obteniendo listado de jugadores con puntuacion general
    //===============================================================================================
    @RequestMapping(value = "/crabs/game/scorepoll", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> pollPosition() {
        Map<String, Object> dto = new HashMap<>();

        dto.put("player_details_all", playerCrabRepository.findAll().stream().sorted((gp1, gp2) -> gp1.getId().compareTo(gp2.getId())).map(playerCrab -> makeDtoPollPosition(playerCrab)).collect(Collectors.toList()));

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //================================================================================================
    //obteniendo todos los disparos del jugador
    //===========================================================================================
    @RequestMapping(value = "/crabs/game/allshots", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> allshots(Authentication authentication) {
        Map<String, Object> dto = new HashMap<>();

        PlayerCrab playerCrabAuth = playerLoggedDetails(authentication);
//        if (playerCrabAuth == null) {
//            dto.put("player_shots_detail", playerCrabRepository.findAll().stream()
//                    .map(playerCrab -> playerCrab.getDiceCrabList()).flatMap(diceCrab -> diceCrab.stream().map(diceCrab1 -> makeDiceCrabMiniDto(diceCrab1)))
//                    .collect(Collectors.toList()));

//        }
//        if (!playerCrabAuth.equals( null))  {
        dto.put("player_name", playerCrabAuth.getuserName());
        dto.put("player_auth_shots_detail", playerCrabAuth.getDiceCrabList().stream().map(diceCrab -> makeDiceCrabMiniDto(diceCrab)).collect(Collectors.toList()));
//
//        }
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    //====================================================================================================
    //registrando el usuario
    //====================================================================================================
    @RequestMapping(value = "/crabs/game/register", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getUserRegistered(@RequestBody PlayerCrab playerCrab) {
        if (playerCrab.getuserName().isEmpty() || playerCrab.getUserPassword().isEmpty()) {
            return new ResponseEntity<>(makeResponseEntity("Error", "Fill All Inputs"), HttpStatus.FORBIDDEN);
        }
        if (playerCrabRepository.findByUserName(playerCrab.getuserName()) != null) {
            return new ResponseEntity<>(makeResponseEntity("Conflict", "User With Same Name Already Exists"), HttpStatus.CONFLICT);
        } else {
            playerCrab.setUserPassword(passwordEncoder.encode(playerCrab.getUserPassword()));
            PlayerCrab newPlayerCrab = playerCrabRepository.save(playerCrab);

            return new ResponseEntity<>(makeResponseEntity("Id", newPlayerCrab.getId()), HttpStatus.CREATED);
        }

    }


    ////////////////////////////////////////DTOS/////////////////////////////////////////
    private Map<String, Object> makePlayerDTO(PlayerCrab playerCrab) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player_id", playerCrab.getId());
        dto.put("player_name", playerCrab.getuserName());
        dto.put("player_score", playerCrab.getAddScoreCrabList().stream().map(scoreCrab -> makeScoreDto(scoreCrab)).collect(Collectors.toList()));
        dto.put("player_shots", playerCrab.getDiceCrabList().stream().map(diceCrab -> makeDiceCrabDto(diceCrab)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeDtoPollPosition(PlayerCrab playerCrab) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player_name", playerCrab.getuserName());
        dto.put("details_score_game", playerCrab.getGameCrabSet().stream().sorted((gp1, gp2) -> gp1.getId().compareTo(gp2.getId())).map(gameCrab -> makeDtoShotsScorePerPlayer(gameCrab)).collect(Collectors.toList()));


        return dto;
    }

    private Map<String, Object> makeDtoShotsScorePerPlayer(GameCrab gameCrab) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_id", gameCrab.getId());
        dto.put("game_score", gameCrab.getScoreCrab().getScoreCrabImport());
        dto.put("shots_in_game", gameCrab.getDiceCrabList().stream().sorted((gp1, gp2) -> gp1.getId().compareTo(gp2.getId())).map(diceCrab -> makeDiceCrabDto(diceCrab)).collect(Collectors.toList()));


        return dto;
    }


    private Map<String, Object> makeAllGamesDTO(GameCrab gameCrab) {
        ;
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_id", gameCrab.getId());
        dto.put("game_status", gameCrab.getStatusCrabList().stream().map(statusCrab -> makeStatusDTO(statusCrab)).collect(Collectors.toList()));
        dto.put("game_shot", gameCrab.getDiceCrabList().stream().map(diceCrab -> makeDiceCrabDto(diceCrab)).collect(Collectors.toList()));


        dto.put("game_player", gameCrab.getPlayerCrab().getuserName());
        dto.put("game_score", gameCrab.getScoreCrab().getScoreCrabImport());
        dto.put("player_in_game_details", gameCrab.getPlayerCrab().getDiceCrabList().stream().map(diceCrab -> makeDiceCrabDto(diceCrab)).collect(Collectors.toList()));

        return dto;
    }

    private Map<String, Object> makeStatusDTO(StatusCrab statusCrab) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("status", statusCrab.getStatus());
        return dto;
    }

    private Map<String, Object> makeScoreDTO(ScoreCrab scoreCrab) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("score", scoreCrab.getScoreCrabImport());
        return dto;
    }

    private Map<String, Object> makeScoreDto(ScoreCrab scoreCrab) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("score", scoreCrab.getScoreCrabImport());
        return dto;
    }

    private Map<String, Object> makeDiceCrabDto(DiceCrab diceCrab) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn_shot", diceCrab.getId());
        dto.put("shot", diceCrab.getDicesResult());
        dto.put("shot_dice_1", diceCrab.getDice1Result());
        dto.put("shot_dice_2", diceCrab.getDice2Result());
        return dto;
    }

    private Map<String, Object> makeDiceCrabMiniDto(DiceCrab diceCrab) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("shot", diceCrab.getDicesResult());
//        dto.put("shot_dice_1", diceCrab.getDice1Result());
//        dto.put("shot_dice_2", diceCrab.getDice2Result());
        return dto;
    }


    //////////////////////////////////////////////PRIVATE METHODS EXTRA//////////////////////////////////////
    private PlayerCrab playerLoggedDetails(Authentication authentication) {
        return playerCrabRepository.findByUserName(authentication.getName());
    }

    private Map<String, Object> makeResponseEntity(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

}
