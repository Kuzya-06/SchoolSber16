package ru.sber.task7;

public class Main {
    public static void main(String[] args) {

        FromClass fromClass = new FromClass();
        fromClass.setName("John");
        fromClass.setAge(30);

        ToClass toClass = new ToClass();
        BeanUtils.assign(toClass, fromClass);

        System.out.println(toClass);
        System.out.println(fromClass);
    }
}

class FromClass {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "FromClass{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

class ToClass {
    private String name;
    private int age;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "ToClass{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

}
