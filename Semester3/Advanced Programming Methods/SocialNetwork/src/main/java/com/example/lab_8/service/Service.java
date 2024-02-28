package com.example.lab_8.service;

import com.example.lab_8.domain.Friendship;
import com.example.lab_8.domain.User;

import java.util.List;
import java.util.Optional;

public interface Service {
    Optional<User> addUser(String username, String password, String firstName, String lastName);
    Optional<User> deleteUser(Long id);
    Optional<Friendship> addFriendship(Long id1, Long id2);
    Optional<Friendship> deleteFriendship(Long id1, Long id2);
    Integer getNumberOfCommunities();
    List<List<User>> getTheMostSociableCommunity();
}
