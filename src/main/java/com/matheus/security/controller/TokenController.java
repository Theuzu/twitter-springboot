package com.matheus.security.controller;

import com.matheus.security.controller.dto.LoginRequest;
import com.matheus.security.controller.dto.LoginResponse;
import com.matheus.security.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder){
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login") //Endpoint de login
        public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {; //Retorna o Login

            var user = userRepository.findByUsername(loginRequest.username());//validando no DB

            if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
                throw new BadCredentialsException("user or password is invalid!");
            }

            var now = Instant.now();
            var expiresIn = 300L;

            //atributos do JSON
            var claims = JwtClaimsSet.builder()
                    .issuer("myBackend")//quem gerou o token
                    .subject(user.get().getUserID().toString())//User como dono do token
                    .issuedAt(now)//emissao do token
                    .expiresAt(now.plusSeconds(expiresIn)) //tempo de expiracao
                    .build();

            var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(); //pega jwt dos claims e criptografa

            return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));

    }
}