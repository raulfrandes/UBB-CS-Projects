package com.example.lab_8.repository.paging;

import com.example.lab_8.domain.Entity;
import com.example.lab_8.domain.FriendDTO;
import com.example.lab_8.repository.Repository;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {

    Page<E> findAll(Pegeable pegeable);
}
