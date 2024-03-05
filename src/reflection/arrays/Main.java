package reflection.arrays;

public class Main {

    public static void main(String[] args) {
        int[] array = { 1, 3 };
        inspectArrayType(array);
    }

    public static void inspectArrayType(Object obj) {
        Class<?> clazz = obj.getClass();

        System.out.printf("Type: %s\n", clazz.isArray());

        Class<?> arrayComponentType = clazz.getComponentType();

        System.out.printf("This is an array of type: %s", arrayComponentType.getTypeName());
    }
}
