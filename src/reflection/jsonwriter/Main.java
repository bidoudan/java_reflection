package reflection.jsonwriter;

import reflection.jsonwriter.data.Address;
import reflection.jsonwriter.data.Person;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Address address1 = new Address("Main Street", (short) 5);
        Address address2 = new Address("Alt Street", (short) 5);
        Person person = new Person("Ali ISSALI", false, 34, 100.54f, new Address[]{address1, address2}, new String[]{"Lord of Rings", "Source code", "The walking dead"});
        System.out.println(objectToJson(person, 0));
    }
    public static String objectToJson(Object instance, int indentSize) throws IllegalAccessException {
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(indent(indentSize));
        stringBuilder.append("{");
        stringBuilder.append("\n");

        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            if (field.isSynthetic()) {
                continue;
            }
            field.setAccessible(true);

            stringBuilder.append(indent(indentSize + 1));

            stringBuilder.append(formatStringValue(field.getName()));
            stringBuilder.append(":");
            if (field.getType().equals(String.class)) {
                stringBuilder.append(formatStringValue(field.get(instance).toString()));
            } else if (field.getType().isPrimitive()) {
                stringBuilder.append(formatPrimitiveValue(field.get(instance), field.getType()));
            } else if (field.getType().isArray()) {
                stringBuilder.append(arrayToJson(field.get(instance), indentSize + 1));
            } else {
                stringBuilder.append(objectToJson(field.get(instance), indentSize + 1));
            }
            if (i != declaredFields.length - 1) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append(indent(indentSize));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private static String arrayToJson(Object array, int indentSize) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();

         int arrayLength = Array.getLength(array);

         Class<?> componentType = array.getClass().getComponentType();

         stringBuilder.append("[");
         stringBuilder.append("\n");

        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(array, i);
            if (componentType.isPrimitive()) {
                stringBuilder.append(indent(indentSize + 1));
                stringBuilder.append(formatPrimitiveValue(element, componentType));
            } else if (componentType.equals(String.class)) {
                stringBuilder.append(indent(indentSize + 1));
                stringBuilder.append(formatStringValue(element.toString()));
            } else {
                stringBuilder.append(objectToJson(element, indentSize + 1));
            }

            if (i != arrayLength - 1) {
               stringBuilder.append(",");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append(indent(indentSize));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static String formatPrimitiveValue(Object parentInstance, Class<?> type) throws IllegalAccessException {
        if(type.equals(boolean.class)
        || type.equals(int.class)
        || type.equals(short.class)
        || type.equals(long.class)) {
            return parentInstance.toString();
        } else if (type.equals(double.class)
                || type.equals(float.class)) {
            return String.format("%.02f", parentInstance);
        }
        throw new RuntimeException(String.format("Type : %s is unsupported", type));
    }
    public static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }

    public static String indent(int indentSize) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < indentSize; i++) {
            stringBuilder.append("\t");
        }
        return stringBuilder.toString();
    }
}