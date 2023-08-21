package com.zenika.poc;

import org.graalvm.polyglot.HostAccess;

import java.util.List;

public class Person {
    @HostAccess.Export
    public String name;
    @HostAccess.Export
    public int age;
    @HostAccess.Export
    public String[] hobbies;

    public Person(String name, int age, String[] hobbies) {
        this.name = name;
        this.age = age;
        this.hobbies = hobbies;
    }
    @HostAccess.Export
    public String getName() {
        return name;
    }
    @HostAccess.Export
    public int getAge() {
        return age;
    }

    @HostAccess.Export
    public String[] getHobbies() {
        return hobbies;
    }
}
