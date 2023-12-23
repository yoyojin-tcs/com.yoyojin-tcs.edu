package com.yoyojintcs.edu.reflection.services;

import com.yoyojintcs.edu.reflection.di.annotations.Autowired;
import com.yoyojintcs.edu.reflection.di.annotations.Component;
import com.yoyojintcs.edu.reflection.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

@Component
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    // This field will be injected through the setter
    @Autowired
    private PersonService personService;

    // This field will remain untouched
    private String senderEmail = "my.tinkoff.всё@tinkoff.ru";

    @Override
    public void sendNotificationEmailByName(String personName) {
        Person person = personService.findPersonByName(personName).orElseThrow(() -> new RuntimeException(
                format("Email could not be sent. Person '%s' has not been found", personName))
        );
        String email = person.getEmail();
        logger.debug("Sending notification from {} to {}", senderEmail, email);
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

}
