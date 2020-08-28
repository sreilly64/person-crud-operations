package io.zipcoder.crudapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService){
        this.personService = personService;
    }

    @GetMapping(value = "/people/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id){
        Person person = null;
        try{
            person = personService.getPerson(id);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
        if(person == null){
            return ResponseEntity.notFound().build();
        }else{
            return new ResponseEntity<Person>(person, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/people")
    public @ResponseBody List<Person> getPersonList(){
        List<Person> people = null;
        try{
            people = personService.getPersonList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return people;
    }

    @PostMapping(value = "/people")
    public ResponseEntity<Person> createPerson(@RequestBody Person p){
        Person person = null;
        try{
            person = personService.createPerson(p);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<Person>(person, HttpStatus.CREATED);
    }

    @PutMapping(value = "/people/{id}")
    public ResponseEntity<Person> updatePerson(@RequestBody Person p){
        Person person = null;
        try{
            person = personService.updatePerson(p);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
        if(getPersonById(p.getId()).getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return new ResponseEntity<Person>(person, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<Person>(person, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "/people/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable Long id){
        Person person = null;
        try{
            person = personService.deletePerson(id);
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<Person>(person, HttpStatus.NO_CONTENT);
    }

}
