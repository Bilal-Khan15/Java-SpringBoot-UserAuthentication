package com.callsign.userauthentication.controller;

import com.callsign.userauthentication.models.AuthenticationRequest;
import com.callsign.userauthentication.models.AuthenticationResponse;
import com.callsign.userauthentication.models.Person;
import com.callsign.userauthentication.models.PersonDetails;
import com.callsign.userauthentication.repository.PersonRepository;
import com.callsign.userauthentication.service.PersonDetailsService;
import com.callsign.userauthentication.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static com.callsign.userauthentication.Utils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import javax.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDetailsService personDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private static PersonDetails dummy;

    @Test
    void home() {
        AuthController controller = new AuthController(authenticationManager, personDetailsService, jwtUtil, passwordEncoder);
        String response = controller.home();
        assertEquals("Hello World", response);
    }

    @Test
    void hello() {
        AuthController controller = new AuthController(authenticationManager, personDetailsService, jwtUtil, passwordEncoder);
        String response = controller.hello("Bilal");
        assertEquals("Hello, Bilal", response);
    }

    @Test
    void login() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("Bilal", "password");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("anyToken");

        String jsonRequest = asJsonString(authenticationRequest);
        String jsonResponse = asJsonString(authenticationResponse);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/login")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);

        when(jwtUtil.generateToken(dummy)).thenReturn("anyToken");
        when(personDetailsService.loadUserByUsername(eq("Bilal"))).thenReturn(dummy);

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(jsonResponse, true))
                .andExpect(jsonPath("$.jwt").value(is(notNullValue())))
                .andExpect(jsonPath("$.jwt").value("anyToken"))
                .andReturn();
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n    \"username\": \"Bilal\",\n    \"password\": \"password\"\n}"))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/signup"));
    }

    @Test
    void findById() {
        AuthController controller = new AuthController(authenticationManager, personDetailsService, jwtUtil, passwordEncoder);
        Person userMocked = new Person(1, "foo", "pass");

        when(personDetailsService.findById(anyInt())).thenReturn(userMocked);

        Person person = controller.findById(1);

        assertNotNull(person);
        assertEquals(userMocked.getId(), person.getId());
        assertEquals(userMocked.getUsername(), person.getUsername());
        assertEquals(userMocked.getPassword(), person.getPassword());
    }
}