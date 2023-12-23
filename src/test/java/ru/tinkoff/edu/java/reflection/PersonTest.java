package ru.tinkoff.edu.java.reflection;

import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.reflection.model.Person;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class PersonTest {

    @Test
    void reflectionTest(){
        try {
            Class<?> clazz = Class.forName("ru.tinkoff.edu.java.reflection.model.Person");
            Person p = new Person("Alex", "ru.tinkoff.edu.java.reflection.Alex@tinkoff.ru");
            Constructor<?> constructor = p.getClass().getConstructor(String.class);
            var person = constructor.newInstance("Alex");
            System.out.println("We initialized new instance for class: " + person.getClass().getName());
            List<Method> getMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(m->{
                System.out.println(" " + m.getName());
                return m.getName().startsWith("get") && m.getParameterCount() == 0;
            }).toList();
            System.out.println("Get methods invocation result:");
            for (Method method : getMethods) {
                System.out.println(" " + method.invoke(person));
            }
            Field nameField = Person.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(p, "Another Name");
            System.out.println("Printing new value:");
            System.out.println(" " + p.getName());
            nameField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}