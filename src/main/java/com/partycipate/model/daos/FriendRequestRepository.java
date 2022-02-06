package com.partycipate.model.daos;

import com.partycipate.model.entities.FriendRequest;
import com.partycipate.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findAllBySender(User sender);
    List<FriendRequest> findAllByReceiver(User receiver);
}
