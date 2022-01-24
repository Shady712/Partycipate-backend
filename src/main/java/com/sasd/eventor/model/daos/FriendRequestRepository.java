package com.sasd.eventor.model.daos;

import com.sasd.eventor.model.entities.FriendRequest;
import com.sasd.eventor.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findAllBySender(User sender);
    List<FriendRequest> findAllByReceiver(User receiver);
}
