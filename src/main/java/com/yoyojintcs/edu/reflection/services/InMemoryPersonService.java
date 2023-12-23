package com.yoyojintcs.edu.reflection.services;

import com.yoyojintcs.edu.reflection.di.annotations.Component;
import com.yoyojintcs.edu.reflection.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryPersonService implements PersonService {
    private final Logger logger = LoggerFactory.getLogger(InMemoryPersonService.class);

    private final Map<String, Person> store = new HashMap<>();

    @Override
    public Optional<Person> findPersonByName(String name) {
        logger.trace("Looking for {} in the memory store...", name);
        return Optional.ofNullable(store.get(name));
    }

    @Override
    public void save(Person p) {
        store.put(p.getName(), p);
    }
}
