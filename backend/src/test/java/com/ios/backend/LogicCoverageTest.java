package com.ios.backend;

import com.ios.backend.controllers.AuthController;
import com.ios.backend.entities.Role;
import com.ios.backend.entities.RoleName;
import com.ios.backend.message.request.SignUpForm;
import com.ios.backend.repositories.RoleRepository;
import com.ios.backend.repositories.UserRepository;
import com.ios.backend.security.jwt.JwtProvider;
import com.ios.backend.services.MailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LogicCoverageTest {

    @Mock
    MailService s;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder encoder;
    @Mock
    JwtProvider jwtProvider;
    @InjectMocks
    private AuthController authController;

    SignUpForm signUpForm = new SignUpForm();

    @Before
    public void setUp(){
        signUpForm.setName("Name");
        signUpForm.setEmail("email@test.com");
        signUpForm.setPassword("test");
        signUpForm.setUsername("User");
    }

    @Test
    public void registerUserTest1() {

        ResponseEntity<?> expected = new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

        when(userRepository.existsByUsername(signUpForm.getUsername())).thenReturn(true);

        assertEquals(expected,authController.registerUser(signUpForm));
    }

    @Test
    public void registerUserTest2() {

        ResponseEntity<?> expected = new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);

        when(userRepository.existsByUsername(signUpForm.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpForm.getEmail())).thenReturn(true);

        assertEquals(expected,authController.registerUser(signUpForm));
    }

    @Test
    public void registerUserTest3() {

        ResponseEntity<?> expected = new ResponseEntity<>(true, HttpStatus.OK);
        this.signUpForm.setUser("U");
        Role role = new Role();

        when(userRepository.existsByUsername(signUpForm.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpForm.getEmail())).thenReturn(false);
        when(encoder.encode(signUpForm.getPassword())).thenReturn("22227hd");
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));

        assertEquals(expected,authController.registerUser(signUpForm));
    }

    @Test
    public void registerUserTest4() {

        ResponseEntity<?> expected = new ResponseEntity<>(true, HttpStatus.OK);
        this.signUpForm.setUser("A");
        Role role = new Role();

        when(userRepository.existsByUsername(signUpForm.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpForm.getEmail())).thenReturn(false);
        when(encoder.encode(signUpForm.getPassword())).thenReturn("22227hd");
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(role));

        assertEquals(expected,authController.registerUser(signUpForm));
    }

    @Test
    public void registerUserTest5() {

        ResponseEntity<?> expected = new ResponseEntity<>(true, HttpStatus.OK);
        this.signUpForm.setUser("C");//not U and not A
        Role role = new Role();

        when(userRepository.existsByUsername(signUpForm.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpForm.getEmail())).thenReturn(false);
        when(encoder.encode(signUpForm.getPassword())).thenReturn("22227hd");

        assertEquals(expected,authController.registerUser(signUpForm));
    }

}
