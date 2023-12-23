package ru.tinkoff.edu.java.reflection.services;

import ru.tinkoff.edu.java.reflection.model.Person;

import java.util.Optional;

public interface PersonService {

    Optional<Person> findPersonByName(String name);

    void save(Person p);

}
