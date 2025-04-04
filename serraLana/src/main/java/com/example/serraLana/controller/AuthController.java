package com.example.serraLana.controller;

import com.example.serraLana.auth.TokenService;
import com.example.serraLana.model.Utente;
import com.example.serraLana.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {}) 
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UtenteRepository userRepository;


    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body, HttpServletResponse response) {
        
    	String username = body.get("username");
        String password = body.get("password");

        Map<String, String> result = new HashMap<>();

        if (username == null || password == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("message", "Credenziali non valide");
            return result;
        }

        Optional<Utente> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent() || !optionalUser.get().getPassword().equals(password)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("message", "Credenziali non valide");
            return result;
        }

        Utente user = optionalUser.get();
        String role = user.getRuolo();

        String token = tokenService.generateToken(username, role);

        result.put("message", "Login effettuato con successo");
        result.put("role", role);
        result.put("token", token);
        return result;
    }
    


    @PostMapping("/logout")
    public Map<String, String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            token = authHeader;
        }
        tokenService.removeToken(token);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Logout effettuato con successo");
        return result;
    }
}

