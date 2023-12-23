package ru.tinkoff.edu.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.edu.reflection.model.Person;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class PersonTest {
    private final Logger logger = LoggerFactory.getLogger(PersonTest.class);

    @Test
    void reflectionTest(){
        try {
            Class<?> clazz = Class.forName("ru.tinkoff.edu.reflection.model.Person");
            Person p = new Person("Alex", "ru.tinkoff.edu.java.reflection.Alex@tinkoff.ru");
            Constructor<?> constructor = p.getClass().getConstructor(String.class, String.class);
            var person = constructor.newInstance("Alex", "alex.example@tinkoff.ru");
            logger.info("We initialized new instance for class: " + person.getClass().getName());
            List<Method> getMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(m->{
                System.out.println(" " + m.getName());
                return m.getName().startsWith("get") && m.getParameterCount() == 0;
            }).toList();
            logger.info("Get methods invocation result:");
            for (Method method : getMethods) {
                logger.info(" {}", method.invoke(person));
            }
            Field nameField = Person.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(p, "Another Name");
            logger.info("Printing new name after changing private field:");
            logger.info(" {}", p.getName());
            nameField.setAccessible(false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}