package reflection.parser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Scanner;

public class Parser {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        Config config = createConfigObject(Config.class, Path.of("resources/config.cfg"));
        System.out.println(config);
    }

    public static <T> T createConfigObject(Class<T> clazz, Path filePath) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        // Reading file using Scanner line by line
        Scanner scanner = new Scanner(filePath);
        // GETTING declared constructor
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        // Setting its accessibility
        constructor.setAccessible(true);
        // instantiation using default constructor
        T instance = constructor.newInstance();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] pairNameValue = line.split("=");
            if(pairNameValue.length != 2) {
                continue;
            }
            String propertyName = pairNameValue[0];
            String propertyValue = pairNameValue[1];

            Field field;
            try {
                field = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                System.out.printf("Unsupported property name %s", propertyName);
                continue;
            }
            field.setAccessible(true);
            Object parsedValue;
            if (field.getType().isArray()) {
                parsedValue = parseArray(field.getType().getComponentType(), propertyValue);
            } else {
                parsedValue = parseValue(field.getType(), propertyValue);
            }
            field.set(instance, parsedValue);
        }
        return instance;
    }

    private static Object parseArray(Class<?> arrayElementsType, String value) {
        String[] values = value.split(",");
        Object arrayInstance = Array.newInstance(arrayElementsType, values.length);

        for (int i = 0; i < values.length; i++) {
            Array.set(arrayInstance, i, parseValue(arrayElementsType, values[i]));
        }

        return  arrayInstance;
    }

    private static Object parseValue(Class<?> type, String propertyValue) {
        if(type.equals(boolean.class)) {
            return Boolean.parseBoolean(propertyValue);
        } else if (type.equals(short.class)) {
            return Short.parseShort(propertyValue);
        } else if (type.equals(int.class)) {
            return Integer.parseInt(propertyValue);
        } else if (type.equals(long.class)) {
            return Long.parseLong(propertyValue);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(propertyValue);
        } else if (type.equals(double.class)) {
            return Double.parseDouble(propertyValue);
        } else if (type.equals(String.class)) {
            return propertyValue;
        }
        throw new RuntimeException(String.format("Unsupported type %s", type.getTypeName()));
    }
}
