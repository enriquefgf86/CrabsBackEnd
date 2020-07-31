package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;


@SpringBootApplication
public class CrabApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrabApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public CommandLineRunner initData(PlayerCrabRepository playerCrabRepository, GameCrabRepository gameCrabRepository, DiceCrabRepository diceCrabRepository,
//                                      ScoreCrabRepository scoreCrabRepository, StatusCrabRepository statusCrabRepository   ) {
//        return (args) -> {
//            //Jugadores
//            PlayerCrab playerCrab1 = new PlayerCrab("Romi", passwordEncoder().encode("123456"));
//            PlayerCrab playerCrab2 = new PlayerCrab("Reagan", passwordEncoder().encode("123456"));
//            PlayerCrab playerCrab3 = new PlayerCrab("Obama", passwordEncoder().encode("123456"));
//            PlayerCrab playerCrab4 = new PlayerCrab("Diaz", passwordEncoder().encode("123456"));
//            PlayerCrab playerCrab5 = new PlayerCrab("Stalin", passwordEncoder().encode("123456"));
//
//            playerCrabRepository.save(playerCrab1);
//            playerCrabRepository.save(playerCrab2);
//            playerCrabRepository.save(playerCrab3);
//            playerCrabRepository.save(playerCrab4);
//            playerCrabRepository.save(playerCrab5);
//
////            StateCase stateCase1=new StateCase("Keeps");
////            StateCase stateCase3=new StateCase("Wins");
////            StateCase stateCase2=new StateCase("Looses");
////
////            stateCaseRepository.save(stateCase1);
////            stateCaseRepository.save(stateCase2);
////            stateCaseRepository.save(stateCase3);
//
//
//            ScoreCrab scoreCrab1 = new ScoreCrab(0.0, playerCrab1);
//            ScoreCrab scoreCrab2 = new ScoreCrab(-1.0, playerCrab1);
//            ScoreCrab scoreCrab3 = new ScoreCrab(1.0, playerCrab2);
//            ScoreCrab scoreCrab4 = new ScoreCrab(1.0, playerCrab2);
//            ScoreCrab scoreCrab5 = new ScoreCrab(-1.0, playerCrab3);
//            ScoreCrab scoreCrab6 = new ScoreCrab(1.0, playerCrab3);
//            ScoreCrab scoreCrab7 = new ScoreCrab(1.0, playerCrab4);
//            ScoreCrab scoreCrab8 = new ScoreCrab(-1.0, playerCrab4);
//            ScoreCrab scoreCrab9 = new ScoreCrab(1.0, playerCrab5);
//            ScoreCrab scoreCrab10 = new ScoreCrab(-1.0, playerCrab5);
//            ScoreCrab scoreCrab11 = new ScoreCrab(1.0, playerCrab1);
//            ScoreCrab scoreCrab12 = new ScoreCrab(0.0, playerCrab3);
//
//            scoreCrabRepository.save(scoreCrab1);
//            scoreCrabRepository.save(scoreCrab2);
//            scoreCrabRepository.save(scoreCrab3);
//            scoreCrabRepository.save(scoreCrab4);
//            scoreCrabRepository.save(scoreCrab5);
//            scoreCrabRepository.save(scoreCrab6);
//            scoreCrabRepository.save(scoreCrab7);
//            scoreCrabRepository.save(scoreCrab8);
//            scoreCrabRepository.save(scoreCrab9);
//            scoreCrabRepository.save(scoreCrab10);
//            scoreCrabRepository.save(scoreCrab11);
//            scoreCrabRepository.save(scoreCrab12);
//
//
//
//            //Juegos
//            GameCrab gameCrab1 = new GameCrab(new Date(), playerCrab1,scoreCrab1);
//            GameCrab gameCrab2 = new GameCrab(new Date(), playerCrab1,scoreCrab2);
//            GameCrab gameCrab3 = new GameCrab(new Date(), playerCrab2,scoreCrab3);
//            GameCrab gameCrab4 = new GameCrab(new Date(), playerCrab2,scoreCrab4);
//            GameCrab gameCrab5 = new GameCrab(new Date(), playerCrab3,scoreCrab5);
//            GameCrab gameCrab6 = new GameCrab(new Date(), playerCrab3,scoreCrab6);
//            GameCrab gameCrab7 = new GameCrab(new Date(), playerCrab4,scoreCrab7);
//            GameCrab gameCrab8 = new GameCrab(new Date(), playerCrab4,scoreCrab8);
//            GameCrab gameCrab9 = new GameCrab(new Date(), playerCrab5,scoreCrab9);
//            GameCrab gameCrab10 = new GameCrab(new Date(), playerCrab5,scoreCrab10);
//            GameCrab gameCrab11 = new GameCrab(new Date(), playerCrab1,scoreCrab11);
//            GameCrab gameCrab12 = new GameCrab(new Date(), playerCrab3,scoreCrab12);
//
//            gameCrabRepository.save(gameCrab1);
//            gameCrabRepository.save(gameCrab2);
//            gameCrabRepository.save(gameCrab3);
//            gameCrabRepository.save(gameCrab4);
//            gameCrabRepository.save(gameCrab5);
//            gameCrabRepository.save(gameCrab6);
//            gameCrabRepository.save(gameCrab7);
//            gameCrabRepository.save(gameCrab8);
//            gameCrabRepository.save(gameCrab9);
//            gameCrabRepository.save(gameCrab10);
//            gameCrabRepository.save(gameCrab11);
//            gameCrabRepository.save(gameCrab12);
//
//
//
//
//
//            //DadosResults
//            //juego 1 jugador 1
//            DiceCrab diceCrab1 = new DiceCrab(1, 5, 6, playerCrab1, gameCrab1);
//            DiceCrab diceCrab2 = new DiceCrab(4, 1, 5, playerCrab1, gameCrab1);
//            DiceCrab diceCrab3 = new DiceCrab(6, 2, 8, playerCrab1, gameCrab1);
//            //juego 2 jugador 1
//            DiceCrab diceCrab4 = new DiceCrab(4, 1, 5, playerCrab1, gameCrab2);
//            DiceCrab diceCrab5 = new DiceCrab(1, 1, 2, playerCrab1, gameCrab2);
//            //juego 3jugador 2
//            DiceCrab diceCrab6 = new DiceCrab(6, 1, 7, playerCrab2, gameCrab3);
//            //juego 4 jugador 2
//            DiceCrab diceCrab7 = new DiceCrab(5, 1, 6, playerCrab2, gameCrab4);
//            DiceCrab diceCrab8 = new DiceCrab(2, 1, 3, playerCrab2, gameCrab4);
//            DiceCrab diceCrab9 = new DiceCrab(1, 2, 3, playerCrab2, gameCrab4);
//            //juego 5 jugador 3
//            DiceCrab diceCrab10 = new DiceCrab(1, 2, 3, playerCrab3, gameCrab5);
//            //juego 6 jugador 3
//            DiceCrab diceCrab11 = new DiceCrab(2, 2, 4, playerCrab3, gameCrab6);
//            DiceCrab diceCrab12 = new DiceCrab(1, 3, 4, playerCrab3, gameCrab6);
//            //juego 7 jugador 4
//            DiceCrab diceCrab13 = new DiceCrab(1, 6, 7, playerCrab4, gameCrab7);
//            //juegoc 8 jugador 4
//            DiceCrab diceCrab14 = new DiceCrab(1, 4, 5, playerCrab4, gameCrab8);
//            DiceCrab diceCrab15 = new DiceCrab(1, 1, 2, playerCrab4, gameCrab8);
//
//            //juego 9 jugador 5
//            DiceCrab diceCrab16 = new DiceCrab(2, 2, 4, playerCrab5, gameCrab9);
//            DiceCrab diceCrab17 = new DiceCrab(1, 3, 4, playerCrab5, gameCrab9);
//            //juego 10 jugador 5
//            DiceCrab diceCrab18 = new DiceCrab(2, 2, 4, playerCrab5, gameCrab10);
//            DiceCrab diceCrab19 = new DiceCrab(1, 3, 4, playerCrab5, gameCrab10);
//            // juego 11 jugador 1
//            DiceCrab diceCrab20 = new DiceCrab(2, 6, 8, playerCrab1, gameCrab11);
//            DiceCrab diceCrab21 = new DiceCrab(2, 3, 5, playerCrab1, gameCrab11);
//            DiceCrab diceCrab22 = new DiceCrab(4, 4, 8, playerCrab1, gameCrab11);
//
//            DiceCrab diceCrab23 = new DiceCrab(1, 3, 4, playerCrab3, gameCrab12);
//            DiceCrab diceCrab24 = new DiceCrab(2, 3, 5, playerCrab3, gameCrab12);
////
//            diceCrabRepository.save(diceCrab1);
//            diceCrabRepository.save(diceCrab2);
//            diceCrabRepository.save(diceCrab3);
//            diceCrabRepository.save(diceCrab4);
//            diceCrabRepository.save(diceCrab5);
//            diceCrabRepository.save(diceCrab6);
//            diceCrabRepository.save(diceCrab7);
//            diceCrabRepository.save(diceCrab8);
//            diceCrabRepository.save(diceCrab9);
//            diceCrabRepository.save(diceCrab10);
//            diceCrabRepository.save(diceCrab11);
//            diceCrabRepository.save(diceCrab12);
//            diceCrabRepository.save(diceCrab13);
//            diceCrabRepository.save(diceCrab14);
//            diceCrabRepository.save(diceCrab15);
//            diceCrabRepository.save(diceCrab16);
//            diceCrabRepository.save(diceCrab17);
//            diceCrabRepository.save(diceCrab18);
//            diceCrabRepository.save(diceCrab19);
//            diceCrabRepository.save(diceCrab20);
//            diceCrabRepository.save(diceCrab21);
//            diceCrabRepository.save(diceCrab22);
//            diceCrabRepository.save(diceCrab23);
//            diceCrabRepository.save(diceCrab24);
//
//            //Scores
//
//            StatusCrab statusCrab1 = new StatusCrab("Keeps", playerCrab1, gameCrab1,diceCrab1);
//            StatusCrab statusCrab2 = new StatusCrab("Keeps", playerCrab1, gameCrab1,diceCrab2);
//            StatusCrab statusCrab3 = new StatusCrab("Keeps", playerCrab1, gameCrab1,diceCrab3);
//
//            StatusCrab statusCrab4 = new StatusCrab("Keeps", playerCrab1, gameCrab2,diceCrab4);
//            StatusCrab statusCrab5 = new StatusCrab("Looses", playerCrab1, gameCrab2,diceCrab5);
//
//            StatusCrab statusCrab6 = new StatusCrab("Wins", playerCrab2, gameCrab3,diceCrab6);
//
//            StatusCrab statusCrab7 = new StatusCrab("Keeps", playerCrab2, gameCrab4,diceCrab7);
//            StatusCrab statusCrab8 = new StatusCrab("Keeps", playerCrab2, gameCrab4,diceCrab8);
//            StatusCrab statusCrab9 = new StatusCrab("Wins", playerCrab2, gameCrab4,diceCrab9);
//
//            StatusCrab statusCrab10 = new StatusCrab("Looses", playerCrab3, gameCrab5,diceCrab10);
//
//            StatusCrab statusCrab11 = new StatusCrab("Keeps", playerCrab3, gameCrab6,diceCrab11);
//            StatusCrab statusCrab12 = new StatusCrab("Wins", playerCrab3, gameCrab6,diceCrab12);
//
//            StatusCrab statusCrab13 = new StatusCrab("Wins", playerCrab4, gameCrab7,diceCrab13);
//
//            StatusCrab statusCrab14 = new StatusCrab("Keeps", playerCrab4, gameCrab8,diceCrab14);
//            StatusCrab statusCrab15 = new StatusCrab("Looses", playerCrab4, gameCrab8,diceCrab16);
//
//            StatusCrab statusCrab16 = new StatusCrab("Keeps", playerCrab5, gameCrab9,diceCrab16);
//            StatusCrab statusCrab17 = new StatusCrab("Wins", playerCrab5, gameCrab9,diceCrab17);
//
//            StatusCrab statusCrab18 = new StatusCrab("Keeps", playerCrab5, gameCrab10,diceCrab18);
//            StatusCrab statusCrab19 = new StatusCrab("Looses", playerCrab5, gameCrab10,diceCrab19);
//
//            StatusCrab statusCrab20 = new StatusCrab("Keeps", playerCrab1, gameCrab11,diceCrab20);
//            StatusCrab statusCrab21 = new StatusCrab("Keeps", playerCrab1, gameCrab11,diceCrab21);
//            StatusCrab statusCrab22= new StatusCrab("Wins", playerCrab1, gameCrab11,diceCrab22);
//
//            StatusCrab statusCrab23= new StatusCrab("Keeps", playerCrab3, gameCrab12,diceCrab23);
//            StatusCrab statusCrab24 = new StatusCrab("Keeps", playerCrab3, gameCrab12,diceCrab24);
//
//            statusCrabRepository.save(statusCrab1);
//            statusCrabRepository.save(statusCrab2);
//            statusCrabRepository.save(statusCrab3);
//            statusCrabRepository.save(statusCrab4);
//            statusCrabRepository.save(statusCrab5);
//            statusCrabRepository.save(statusCrab6);
//            statusCrabRepository.save(statusCrab7);
//            statusCrabRepository.save(statusCrab8);
//            statusCrabRepository.save(statusCrab9);
//            statusCrabRepository.save(statusCrab10);
//            statusCrabRepository.save(statusCrab11);
//            statusCrabRepository.save(statusCrab12);
//            statusCrabRepository.save(statusCrab13);
//            statusCrabRepository.save(statusCrab14);
//            statusCrabRepository.save(statusCrab15);
//            statusCrabRepository.save(statusCrab16);
//            statusCrabRepository.save(statusCrab17);
//            statusCrabRepository.save(statusCrab18);
//            statusCrabRepository.save(statusCrab19);
//            statusCrabRepository.save(statusCrab20);
//            statusCrabRepository.save(statusCrab21);
//            statusCrabRepository.save(statusCrab22);
//            statusCrabRepository.save(statusCrab23);
//            statusCrabRepository.save(statusCrab24);
//
//
//        };
//    }

}

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    PlayerCrabRepository playerCrabRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {
            PlayerCrab playerCrab = playerCrabRepository.findByUserName(inputName);
            if (playerCrab != null) {
                return new User(playerCrab.getuserName(), playerCrab.getUserPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();///de heroku tambien
        http.authorizeRequests()
                .antMatchers("/crabs/game/all").permitAll()
                .antMatchers("/crabs/game/register").permitAll()
                .antMatchers("/crabs/game/{gameCrabId}").permitAll()
                .antMatchers("crabs/game/play/{gameDiceId}").permitAll()
                .antMatchers("/crabs/game/scorepoll").permitAll()
                .antMatchers("/crabs/game/create").permitAll()
                .antMatchers("/crabs/game/allshots").permitAll()

                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/rest/**").hasAuthority("ADMIN")

                .antMatchers("/**").hasAuthority("USER")
                .anyRequest().fullyAuthenticated();
        /////Autorizaciones y permisos para los distintos niveles de seguridad que tendria el usuario segun su casificacion
        http.formLogin()
                .usernameParameter("userName")
                .passwordParameter("userPassword")
                .loginPage("/crabs/game/login");

        http.logout().logoutUrl("/crabs/game/logout");

        http.csrf().disable();

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
//        http.headers().frameOptions().disable();
        http.headers().frameOptions().sameOrigin();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    @Bean////importando Heroku a la base de datos
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // will fail with 403 Invalid CORS request
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

