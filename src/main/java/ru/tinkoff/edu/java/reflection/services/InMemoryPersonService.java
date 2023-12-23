package ru.tinkoff.edu.java.reflection.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.edu.java.reflection.di.annotations.Component;
import ru.tinkoff.edu.java.reflection.model.Person;

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
