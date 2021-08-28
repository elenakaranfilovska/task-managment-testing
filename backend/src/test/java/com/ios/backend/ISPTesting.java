package com.ios.backend;

import com.ios.backend.entities.User;
import com.ios.backend.services.ProgramService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ISPTesting {

    private List<User> users;
    private List<User> programMembers;
    private ProgramService service;

    @Before
    public void setUp() {
        users = new ArrayList<>();
        programMembers = new ArrayList<>();
        this.service = new ProgramService();
    }

    /*
           C1,C2:барем 1 елемент, барем 1 елемент
     */
    @Test
    public void test1() {
        User user = new User("name1", "username1", "lastname1", "psw1");
        User user2 = new User("name2", "username2", "lastname2", "psw2");
        List<User> expected = new ArrayList<>();
        users.add(user);
        users.add(user2);
        programMembers.add(user);
        expected.add(user);
        assertEquals(expected, service.inviteOnlySameProgramMembers(users, programMembers));
    }

    /*
         барем 1 елемент, null-очекуваме NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void test2() {
        User user = new User("name1", "username1", "lastname1", "psw1");
        users.add(user);
        programMembers = null;
        service.inviteOnlySameProgramMembers(users, programMembers);
    }

    /*
         null, барем 1 елемент -очекуваме NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void test3() {
        User user = new User("name1", "username1", "lastname1", "psw1");
        users = null;
        programMembers.add(user);
        service.inviteOnlySameProgramMembers(users, programMembers);
    }

    /*
          барем 1 елемент, {} - очекуваме да врати празна листа
     */
    @Test()
    public void test4() {
        User user = new User("name1", "username1", "lastname1", "psw1");
        users.add(user);
        List<User> expected = new ArrayList<>();
        assertEquals(expected, service.inviteOnlySameProgramMembers(users, programMembers));
    }

    /*
        {}, барем 1 елемент  - очекуваме  да врати празна листа
     */
    @Test()
    public void test5() {
        User user = new User("name1", "username1", "lastname1", "psw1");
        programMembers.add(user);
        List<User> expected = new ArrayList<>();
        assertEquals(expected, service.inviteOnlySameProgramMembers(users, programMembers));
    }


    /*
           C1: Немаат ниту еден заеднички елемент. falsе
           C2: users е подлиста на programMembers: true
     */
    @Test()
    public void test6() {
        User user = new User("name1", "username1", "lastname1", "psw1");
        User user2 = new User("name2", "username2", "lastname2", "psw2");
        List<User> expected = new ArrayList<>();
        users.add(user);
        programMembers.add(user);
        programMembers.add(user2);
        expected.add(user);
        assertEquals(expected, service.inviteOnlySameProgramMembers(users, programMembers));
    }

    /*
           C1: Немаат ниту еден заеднички елемент. falsе
           C2: users е подлиста на programMembers:  falsе
    */
    @Test()
    public void test7() {
        User user = new User("name1", "username1", "lastname1", "psw1");
        User user2 = new User("name2", "username2", "lastname2", "psw2");
        List<User> expected = new ArrayList<>();
        users.add(user);
        users.add(user2);
        programMembers.add(user);
        expected.add(user);
        assertEquals(expected, service.inviteOnlySameProgramMembers(users, programMembers));
    }

    /*
           C1: Немаат ниту еден заеднички елемент. true
           C2: users е подлиста на programMembers:  true
           -- koga i dvete listi se prazni--
     */
    @Test()
    public void test8() {
        List<User> expected = new ArrayList<>();
        assertEquals(expected, service.inviteOnlySameProgramMembers(users, programMembers));
    }
}