package ru.sbrf.course;

public class Client {

    private long id;

    private String name;

    private ClientCategory category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientCategory getCategory() {
        return category;
    }

    public void setCategory(ClientCategory category) {
        this.category = category;
    }

}
