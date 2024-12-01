package ru.sber.task7;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BeanUtilsTest {

    private ToClass toClass;
    private FromClass fromClass;

    @BeforeEach
    void setUp() {
        toClass = new ToClass();
        fromClass = new FromClass();
        fromClass.setName("John");
        fromClass.setAge(30);
    }

    @Test
    void assignValidObjectsSuccess() {
        BeanUtils.assign(toClass, fromClass);
        Assertions.assertEquals(fromClass.getName(), toClass.getName());
        Assertions.assertEquals(fromClass.getAge(), toClass.getAge());
    }

    @Test
    void assignNullFromThrowsIllegalArgumentException() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> BeanUtils.assign(toClass, null));
        Assertions.assertEquals("Аргументы 'to' и 'from' не должны быть null.", exception.getMessage());
    }

    @Test
    void assignNullToThrowsIllegalArgumentException() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                BeanUtils.assign(null, fromClass)
        );
        Assertions.assertEquals("Аргументы 'to' и 'from' не должны быть null.", exception.getMessage());
    }

    @Test
    void assignIncompatibleTypesNoExceptionButNoCopy() {
        IncompatibleClass incompatible = new IncompatibleClass();
        incompatible.setData("Incompatible data");

        BeanUtils.assign(toClass, incompatible);

        Assertions.assertNull(toClass.getName());
        Assertions.assertEquals(0, toClass.getAge());
    }

    // Пример классов для тестирования
    static class FromClass {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    static class ToClass {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    static class IncompatibleClass {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}
