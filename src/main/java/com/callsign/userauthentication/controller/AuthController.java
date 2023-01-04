package com.callsign.userauthentication.controller;

import com.callsign.userauthentication.models.AuthenticationRequest;
import com.callsign.userauthentication.models.AuthenticationResponse;
import com.callsign.userauthentication.models.Person;
import com.callsign.userauthentication.models.PersonDetails;
import com.callsign.userauthentication.service.PersonDetailsService;
import com.callsign.userauthentication.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private PersonDetailsService personDetailsService;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    @RequestMapping({"/"})
    public String home() {
        return "Hello World";
    }

    @RequestMapping({"/hello"})
    public String hello(@RequestParam(name = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s", name);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody @Validated AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password" + e);
        }

        final UserDetails userDetails = personDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody @Validated PersonDetails personDetails, UriComponentsBuilder uriComponentsBuilder) {
        personDetails.setPassword(passwordEncoder.encode(personDetails.getPassword()));

        try {
            personDetailsService.create(personDetails);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid username or password" + e);
        }

        URI location = uriComponentsBuilder.path("/signup").buildAndExpand(personDetails.getUsername()).toUri();
        return ResponseEntity.created(location).body("User Created");
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable Integer id) {
        return personDetailsService.findById(id);
    }
}
