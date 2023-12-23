package ru.tinkoff.edu.reflection.services;

import ru.tinkoff.edu.reflection.model.Person;

import java.util.Optional;

public interface PersonService {

    Optional<Person> findPersonByName(String name);

    void save(Person p);

}
