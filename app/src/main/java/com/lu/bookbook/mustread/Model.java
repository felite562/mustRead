package com.lu.bookbook.mustread;

/**
 * Created by Luba on 23.04.2018.
 */



public class Model {
    private int id;
    private String name;
    private String author;
    private byte [] image;

    public Model(int id, String name, String author, byte[] image) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public byte[] getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
