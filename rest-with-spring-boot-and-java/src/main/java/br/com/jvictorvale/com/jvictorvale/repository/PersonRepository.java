package br.com.jvictorvale.com.jvictorvale.repository;

import br.com.jvictorvale.com.jvictorvale.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> { }
