package com.yoyojintcs.edu.reflection.services;

import com.yoyojintcs.edu.reflection.model.Person;

import java.util.Optional;

public interface PersonService {

    Optional<Person> findPersonByName(String name);

    void save(Person p);

}
