package com.example.lab_8.domain;

import java.time.LocalDateTime;

public class FriendDTO extends Entity<Long>{
    private String username;
    private String firstName;
    private String lastName;
    private FriendRequest status;
    private LocalDateTime friendsFrom;

    public FriendDTO(Long id, String username, String firstName, String lastName, FriendRequest status, LocalDateTime friendsFrom){
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.friendsFrom = friendsFrom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public FriendRequest getStatus() {
        return status;
    }

    public void setStatus(FriendRequest status) {
        this.status = status;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    @Override
    public String toString() {
        return "" + firstName + " | " + lastName + " | " + friendsFrom;
    }
}
