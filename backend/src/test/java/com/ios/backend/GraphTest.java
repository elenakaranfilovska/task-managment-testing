package com.ios.backend;

import com.ios.backend.entities.Passcode;
import com.ios.backend.entities.Program;
import com.ios.backend.entities.User;
import com.ios.backend.repositories.InvitesRepository;
import com.ios.backend.repositories.PasscodeRepository;
import com.ios.backend.repositories.ProgramRepository;
import com.ios.backend.repositories.UserRepository;
import com.ios.backend.services.MailService;
import com.ios.backend.services.ProgramService;
import com.ios.backend.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProgramRepository programRepository;
    @Mock
    private PasscodeRepository passcodeRepository;
    @Mock
    private InvitesRepository invitesRepository;
    @Mock
    private MailService mailService;
    @InjectMocks
    private ProgramService service;


    @Test
    public void test1() {
        User user = new User();
        user.setId(1);
        String code = "nonexistingcode";

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(passcodeRepository.getPidByCode(code)).thenReturn(Optional.empty());
        assertFalse(service.addUser(1L, code));
    }

    @Test
    public void test2() {
        User user = new User();
        user.setId(1);
        String code = "code";
        Passcode passcode = new Passcode();
        passcode.setCode("code");
        List<Long> invitedPid = new ArrayList<>();
        invitedPid.add(2L);
        invitedPid.add(1L);
        Program program = new Program();
        program.setUsers(new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(passcodeRepository.getPidByCode(code)).thenReturn(Optional.of(1L));
        when(invitesRepository.existsByUid(1)).thenReturn(true);
        when(invitesRepository.findByUid(1L)).thenReturn(invitedPid);
        when(programRepository.findById(1L)).thenReturn(Optional.of(program));
        when(programRepository.save(any(Program.class))).thenReturn(program);
        assertTrue(service.addUser(1L, code));
    }

    @Test
    public void test3() {
        User user = new User();
        user.setId(1);
        String code = "code";
        Passcode passcode = new Passcode();
        passcode.setCode("code");
        List<Long> invitedPid = new ArrayList<>();
        invitedPid.add(2L);
        Program program = new Program();
        program.setUsers(new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(passcodeRepository.getPidByCode(code)).thenReturn(Optional.of(1L));
        when(invitesRepository.existsByUid(1)).thenReturn(true);
        when(invitesRepository.findByUid(1L)).thenReturn(invitedPid);
        assertFalse(service.addUser(1L, code));
    }
}
