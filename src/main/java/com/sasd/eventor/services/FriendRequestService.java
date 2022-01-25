package com.sasd.eventor.services;

import com.sasd.eventor.model.daos.FriendRequestRepository;
import com.sasd.eventor.model.entities.FriendRequest;
import com.sasd.eventor.model.entities.User;
import com.sasd.eventor.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.sasd.eventor.model.enums.RequestStatus.ACCEPTED;
import static com.sasd.eventor.model.enums.RequestStatus.REJECTED;

@Service
@AllArgsConstructor
public class FriendRequestService {
    private final UserService userService;
    private final FriendRequestRepository friendRequestRepository;

    public FriendRequest create(FriendRequest request) {
        return friendRequestRepository.save(request);
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

    public FriendRequest acceptRequest(FriendRequest request) {
        request.setStatus(ACCEPTED);
        userService.befriend(request.getSender(), request.getReceiver());
        return friendRequestRepository.save(request);
    }

    public FriendRequest rejectRequest(FriendRequest request) {
        request.setStatus(REJECTED);
        return friendRequestRepository.save(request);
    }

    public void deleteRequest(FriendRequest request) {
        friendRequestRepository.delete(request);
    }
}
