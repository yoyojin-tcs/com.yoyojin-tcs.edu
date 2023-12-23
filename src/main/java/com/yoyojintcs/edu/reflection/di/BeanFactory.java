package com.yoyojintcs.edu.reflection.di;

import com.yoyojintcs.edu.reflection.di.annotations.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yoyojintcs.edu.reflection.di.annotations.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private final Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    private final Map<String, Object> singletonBeans = new ConcurrentHashMap<>();

    public void instantiate(String basePackage) throws IOException, URISyntaxException {
        logger.trace("============ Instantiation started ============");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        // В Java нет механизма скана пакетов из коробки, поэтому пишем свой;
        scanPackageAndInitiateBeans(basePackage, classLoader);
        logger.trace("Instantiated BEANS={}", singletonBeans);
        logger.trace("============ Instantiation finished ============");
    }

    private void scanPackageAndInitiateBeans(String basePackage, ClassLoader classLoader) throws IOException, URISyntaxException {
        // "ru.tinkoff.edu.java.reflection" -> "ru/tinkoff/edu/java/reflection"
        logger.debug("Scanning package '{}'", basePackage);
        String path = packageToPath(basePackage);
        Enumeration<URL> resources = classLoader.getResources(path);
        Iterator<URL> resourcesIterator = resources.asIterator();
        while (resourcesIterator.hasNext()) {
            var resource = resourcesIterator.next();
            File file = new File(resource.toURI());
            if (file.isDirectory()) {
                scanDirectoryForComponents(basePackage, file);
            } else {
                loadBean(basePackage, file);
            }
        }
    }

    public void initialize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        logger.trace("============ Initialization started ============");
        for (Map.Entry<String, Object> e : singletonBeans.entrySet()) {
            var component = e.getValue();
            Class<?> clazz = component.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    logger.trace("{} - Found autowired field: {}. Looking for suitable beans...", clazz.getSimpleName(), field.getName());
                    Class<?> fieldClass = field.getType();
                    for (Map.Entry<String, Object> beanEntry : singletonBeans.entrySet()) {
                        Object bean = beanEntry.getValue();
                        if (fieldClass.isAssignableFrom(bean.getClass())) {
                            logger.trace("Bean '{}' is suitable to inject.", beanEntry.getKey());
                            String fieldName = field.getName();
                            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                            Method method = clazz.getMethod(setterName, field.getType());
                            logger.trace("Invoke setter method: {}", setterName);
                            method.invoke(component, bean);
                        }
                    }
                }
            }
        }
        logger.trace("============ Initialization finished ============");
    }

    private void scanDirectoryForComponents(String basePackage, File parentDirectory) {
        File[] files = parentDirectory.listFiles();
        if (files == null) return;
        for (File classFile : files) {
            if (classFile.isDirectory()) {
                scanDirectoryForComponents(basePackage, classFile);
            } else {
                loadBean(basePackage, classFile);
            }
        }
    }

    private void loadBean(String basePackage, File classFile) {
        try {
            String fileName = classFile.getName();
            if (fileName.endsWith(".class")) {
                String absolutePath = classFile.getPath();
                String packageStyledPath = absolutePath.replaceAll(FileSystems.getDefault().getSeparator(), ".");
                String withoutExtension = packageStyledPath.substring(0, packageStyledPath.lastIndexOf(".class"));
                int packageNameStartIndex = withoutExtension.indexOf(basePackage);
                String className = withoutExtension.substring(packageNameStartIndex); // Cut off absolute path part
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Component.class)) {
                    createBean(className, clazz);
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            logger.error("Error occurred during loading been!");
            logger.error(e.getMessage(), e);
        }
    }

    private void createBean(String beanName, Class<?> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        logger.debug("Creating bean: " + beanName);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        singletonBeans.put(beanName, instance);
    }

    private static String packageToPath(String packageName) {
        return packageName.replaceAll("\\.", FileSystems.getDefault().getSeparator());
    }

    public <T> T getBean(Class<T> clazz) {
        for (Map.Entry<String, Object> e : singletonBeans.entrySet()) {
            if (clazz.isAssignableFrom(e.getValue().getClass())) {
                return (T) e.getValue();
            }
        }
        return null;
    }
}
