package ru.tinkoff.edu.java.reflection;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ru.tinkoff.edu.java.reflection.di.DiRunner;
import ru.tinkoff.edu.java.reflection.model.Person;
import ru.tinkoff.edu.java.reflection.services.EmailService;
import ru.tinkoff.edu.java.reflection.services.PersonService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException, URISyntaxException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        logger.info("Start the App...");
        DiRunner runner = new DiRunner();
        runner.start(Application.class);
        logger.info("============ Executing business logic ============");
        PersonService personService = runner.getBean(PersonService.class);
        String personName = "Alex";
        Person p = new Person(personName, "Alex.example@tinkoff.ru");
        personService.save(p);
        EmailService emailService = runner.getBean(EmailService.class);
        emailService.sendNotificationEmailByName(personName);
    }

}
