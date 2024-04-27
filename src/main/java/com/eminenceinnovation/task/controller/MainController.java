package com.eminenceinnovation.task.controller;

import com.eminenceinnovation.task.MainService;
import com.eminenceinnovation.task.config.CustomUserDetailsService;
import com.eminenceinnovation.task.config.JwtUtil;
import com.eminenceinnovation.task.model.AuthenticationRequest;
import com.eminenceinnovation.task.model.AuthenticationResponse;
import com.eminenceinnovation.task.model.FootballDTO;
import com.eminenceinnovation.task.model.MatchInfo;
import com.eminenceinnovation.task.model.Payload;
import com.eminenceinnovation.task.model.PayloadDTO;
import com.eminenceinnovation.task.model.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;

@RestController
public class MainController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder bcryptEncoder;
    private final MainService mainService;

    public MainController(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil, PasswordEncoder bcryptEncoder, MainService mainService) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.bcryptEncoder = bcryptEncoder;
        this.mainService = mainService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("User is disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials", e);
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        String password = userDTO.getPassword();
        userDTO.setPassword(bcryptEncoder.encode(password));
        return ResponseEntity.ok(customUserDetailsService.save(userDTO));
    }

    @GetMapping("/decode-jwt")
    public ResponseEntity<?> decodeJWT(@RequestHeader("Authorization") String token) {
        String bearerToken = token.substring(7);

        String userName = jwtUtil.getUserName(bearerToken);

        String[] chunks = bearerToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        ObjectMapper mapper = new ObjectMapper();
        try {
            Payload payload1 = mapper.readValue(payload, Payload.class);

            PayloadDTO payloadDTO = getPayloadDTO(payload1, userName);
            return ResponseEntity.ok(payloadDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/draw-matches")
    public ResponseEntity<?> footBallMatches(@RequestParam("year") Integer year) {
        List<MatchInfo> drawMatches = mainService.getDrawMatches(year);
        return ResponseEntity.ok(drawMatches);
    }

    private static PayloadDTO getPayloadDTO(Payload payload1, String userName) {
        LocalDateTime formattedIssuedAtDate = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(payload1.getIssuedAt()),
                ZoneId.systemDefault()
        );

        LocalDateTime formattedExpirationDate = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(payload1.getExpirationTime()),
                ZoneId.systemDefault()
        );


        PayloadDTO payloadDTO = new PayloadDTO(userName, formattedIssuedAtDate.toString(), formattedExpirationDate.toString());
        return payloadDTO;
    }


}