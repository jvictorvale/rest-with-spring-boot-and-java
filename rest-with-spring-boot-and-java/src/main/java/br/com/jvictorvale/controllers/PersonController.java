package br.com.jvictorvale.controllers;

import br.com.jvictorvale.data.dto.PersonDTO;
import br.com.jvictorvale.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

    @Autowired
    private PersonServices service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO findById(@PathVariable("id") Long id) {
        var person = service.findById(id);

        person.setBirthDay(new Date());
//        person.setPhoneNumber("+55 (85) 98846-3741");
        person.setPhoneNumber("");
        person.setLastName(null);
        person.setSensitiveData("Foo Bar");
        return person;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO create(@RequestBody PersonDTO person) {
        return service.create(person);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO update(@RequestBody PersonDTO person) {
        return service.update(person);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
