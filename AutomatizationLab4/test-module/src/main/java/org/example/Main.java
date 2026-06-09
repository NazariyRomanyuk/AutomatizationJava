package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = ProductModelProperties.class.getConstructor(long.class, long.class, String.class, String.class, String.class);
        Object[] arguments = {1L, 1L, "Name", null, ""};
        try {
            NotNullOrEmptyValidator.validate(constructor, arguments);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        ProductModelProperties product = (ProductModelProperties) constructor.newInstance(arguments);
        Method setter = ProductModelProperties.class.getMethod("setName", String.class);
        try {
            String name = "";
            NotNullOrEmptyValidator.validate(setter, new Object[] {name});
            setter.invoke(product, name);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        Method extension = ProductModelProperties.class.getMethod("nameExtension", String.class);
        try {
            NotNullOrEmptyValidator.validate(setter, new Object[] {null});
            extension.invoke(product, (Object) null);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }
}
