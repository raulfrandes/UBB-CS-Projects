package com.example.lab_8.repository;

import com.example.lab_8.domain.Friendship;
import com.example.lab_8.domain.Tuple;
import com.example.lab_8.domain.User;
import com.example.lab_8.repository.paging.Page;
import com.example.lab_8.repository.paging.PagingRepository;
import com.example.lab_8.repository.paging.Pegeable;

public interface PagingFriendshipRepository extends PagingRepository<Tuple<Long, Long>, Friendship> {
    Page<Friendship> findAll(Pegeable pegeable, User user);
}
