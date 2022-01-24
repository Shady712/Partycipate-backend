package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.FriendRequestRepository;
import com.sasd.eventor.model.entities.FriendRequest;
import com.sasd.eventor.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;

    public FriendRequest create(FriendRequest friendRequst) {
        return friendRequestRepository.save(friendRequst);
    }

    public Optional<FriendRequest> findById(Long id) {
        return friendRequestRepository.findById(id);
    }

    public List<FriendRequest> findAllOutgoing(User sender) {
        return friendRequestRepository.findAllBySender(sender);
    }

    public List<FriendRequest> findAllIncoming(User receiver) {
        return friendRequestRepository.findAllByReceiver(receiver);
    }
}
