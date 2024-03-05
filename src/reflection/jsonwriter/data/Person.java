package reflection.jsonwriter.data;

public class Person {
    private final String name;
    private final boolean employed;
    private final int age;
    private final float salary;

    private final Address[] addresses;

    private final String[] favoriteMovies;

    public Person(String name, boolean employed, int age, float salary, Address[] addresses, String[] favoriteMovies) {
        this.name = name;
        this.employed = employed;
        this.age = age;
        this.salary = salary;
        this.addresses = addresses;
        this.favoriteMovies = favoriteMovies;
    }
}
