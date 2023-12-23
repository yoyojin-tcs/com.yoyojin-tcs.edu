package ru.tinkoff.edu.java.reflection.di;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class DiRunner {
    private final BeanFactory beanFactory = new BeanFactory();

    public void start(Class<?> clazz) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, URISyntaxException {
        beanFactory.instantiate(clazz.getPackageName());
        beanFactory.initialize();
    }

    public <T> T getBean(Class<T> clazz) {
        return (T) beanFactory.getBean(clazz);
    }

}
