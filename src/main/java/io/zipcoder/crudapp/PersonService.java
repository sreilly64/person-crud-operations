package io.zipcoder.crudapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private PersonRepository repo;

    @Autowired
    public PersonService(PersonRepository repo){
        this.repo = repo;
    }

    Person getPerson(Long id) {
        return repo.findOne(id);
    }

    Person createPerson(Person p) {
        return repo.save(p);
    }

    List<Person> getPersonList() {
        return repo.findAll();
    }

    Person updatePerson(Person p) {
        return repo.save(p);
    }

    Person deletePerson(Long id) {
        Person p = repo.findOne(id);
        repo.delete(id);
        return p;
    }

    Boolean exists(Person person){
        return getPerson(person.getId()) != null;
    }
}
