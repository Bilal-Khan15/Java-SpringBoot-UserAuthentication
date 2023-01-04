package com.callsign.userauthentication.service;

import com.callsign.userauthentication.models.Person;
import com.callsign.userauthentication.models.PersonDetails;
import com.callsign.userauthentication.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonDetailsServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonDetailsService personDetailsService;

    @Test
    void loadUserByUsername() {
        Person person = new Person(1, "foo", "pass");
        when(personRepository.findByUsername(anyString())).thenReturn(Optional.of(person));

        UserDetails userDetails = personDetailsService.loadUserByUsername("foo");

        assertEquals("foo", userDetails.getUsername());
        assertEquals("pass", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void create() {

        Person userMocked = new Person(1, "foo", "pass");
        PersonDetails personDetails = new PersonDetails("foo", "pass");

        when(personRepository.save(any(Person.class))).thenReturn(userMocked);

        personDetailsService.create(personDetails);

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void findById() {

        Person userMocked = new Person(1, "foo", "pass");

        when(personRepository.findById(anyInt())).thenReturn(Optional.of(userMocked));

        Person person = personDetailsService.findById(1);

        assertNotNull(person);
        assertEquals(userMocked.getId(), person.getId());
        assertEquals(userMocked.getUsername(), person.getUsername());
        assertEquals(userMocked.getPassword(), person.getPassword());
    }
}