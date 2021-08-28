package com.ios.backend;

import com.ios.backend.dto.NewProgramDTO;
import com.ios.backend.entities.Passcode;
import com.ios.backend.entities.Program;
import com.ios.backend.entities.User;
import com.ios.backend.repositories.InvitesRepository;
import com.ios.backend.repositories.PasscodeRepository;
import com.ios.backend.repositories.ProgramRepository;
import com.ios.backend.repositories.UserRepository;
import com.ios.backend.services.MailService;
import com.ios.backend.services.ProgramService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ProgramServiceMockitoTest {

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
    private ProgramService programService;


    @Test
    public void addUserTest() {
        //given
        long uid = 1L;
        String code = "code123";
        User user = new User("NameTest", "LastName", "Test", "kwdcdi");
        Program program = new Program();
        List<Long> invitedPid = new ArrayList<>();
        invitedPid.add(1L);
        invitedPid.add(2L);
        List<User> users = new ArrayList<>();
        users.add(user);

        //when
        when(userRepository.findById(uid)).thenReturn(java.util.Optional.of(user));
        when(passcodeRepository.getPidByCode(code)).thenReturn(Optional.of(1L));
        when(invitesRepository.existsByUid(uid)).thenReturn(true);
        when(invitesRepository.findByUid(uid)).thenReturn(invitedPid);
        when(programRepository.findById(1L)).thenReturn(java.util.Optional.of(program));
        program.setUsers(users);
        when(programRepository.save(program)).thenReturn(program);

        //then
        assertTrue(programService.addUser(uid, code));

    }

    @Test
    public void createProgramTest() {
        // given
        long admin = 1L;
        User adminUser = new User();
        NewProgramDTO programDTO = new NewProgramDTO();
        programDTO.setId(1L);
        programDTO.setAdmin(1L);
        programDTO.setName("New Program");
        programDTO.setDescription("Description");
        long[] users = {};
        programDTO.setUsers(users);
        Program newProgram = new Program();
        newProgram.setId(programDTO.getId());
        newProgram.setName(programDTO.getName());
        newProgram.setDescription(programDTO.getDescription());
        newProgram.setAdmin(admin);
        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(adminUser);
        newProgram.setUsers(usersList);
        long expected = newProgram.getId();


        //when
        when(userRepository.findById(admin)).thenReturn(Optional.of(adminUser));
        when(programRepository.save(any(Program.class))).thenReturn(newProgram);
        when(mailService.generatePasscodeForProgram(newProgram.getId())).thenReturn(new Passcode());


        //then
        assertEquals(expected, programService.createProgram(programDTO, admin));
    }

}
