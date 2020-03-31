package com.controller;

import java.util.List;

import com.domain.Person;
import com.service.PersonService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @RequestMapping("/addPerson")
    public int create(@RequestBody Person person) {
        int result = personService.create(person);
        return result;
    }

    @RequestMapping("/getPersonList")
    public List<Person> selectList() {
        List<Person> result = personService.selectList();
        return result;
    }

    @RequestMapping("/getPerson")
    public Person selectOne(int id) {
        Person person = personService.selectOne(id);
        return person;
    }

    @RequestMapping("/editPerson")
    public int update(@RequestBody Person person) {
        int result = personService.update(person);
        return result;
    }

    @RequestMapping("/deletePerson")
    public int delete(int id) {
        int result = personService.delete(id);
        return result;
    }
}
