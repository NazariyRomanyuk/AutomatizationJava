package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class NotNullOrEmptyValidator {
    public static void validate(Method method, Object[] args) {
        validateExecutable(method, args);
    }
    public static void validate(Constructor<?> constructor, Object[] args) {
        validateExecutable(constructor, args);
    }
    public static void validateExecutable(Executable executable, Object[] args) {
        Parameter[] parameters = executable.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.isAnnotationPresent(NotNullOrEmpty.class)) {
                Object value = args[i];

                if (value == null)
                    throw new IllegalArgumentException("Parameter \"" + parameter.getName() + "\" must not be null.");

                if (value instanceof String && ((String) value).isBlank())
                    throw new IllegalArgumentException("Parameter \"" + parameter.getName() + "\" must not be empty.");
            }
        }

    }
}
