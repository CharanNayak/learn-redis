package com.example.learn_redis.basic;

public class User {

    private String username;
    private Address address;

    public User() {
    }

    public User(String username, Address address) {
        this.username = username;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public Address getAddress() {
        return address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", address=" + address +
                '}';
    }
}
