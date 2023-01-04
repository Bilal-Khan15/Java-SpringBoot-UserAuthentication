package com.callsign.userauthentication.service;

import com.callsign.userauthentication.repository.PersonRepository;
import com.callsign.userauthentication.models.PersonDetails;
import com.callsign.userauthentication.models.Person;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> user = personRepository.findByUsername(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        return user.map(PersonDetails::new).get();
    }

    public void create(PersonDetails personDetails) {
        Person person = Person.builder()
                .username(personDetails.getUsername())
                .password(personDetails.getPassword())
                .build();

        personRepository.save(person);
    }

    public Person findById(Integer id) {
        var user = personRepository.findById(id);

        return user.get();
    }
}
