package com.partycipate.services;

import com.partycipate.model.daos.FriendRequestRepository;
import com.partycipate.model.entities.FriendRequest;
import com.partycipate.model.entities.User;
import com.partycipate.services.utils.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.partycipate.model.enums.RequestStatus.ACCEPTED;
import static com.partycipate.model.enums.RequestStatus.REJECTED;

@Service
@AllArgsConstructor
public class FriendRequestService {
    private final UserService userService;
    private final MailService mailService;
    private final FriendRequestRepository friendRequestRepository;
    private final Logger logger = LoggerFactory.getLogger(FriendRequestService.class);

    public FriendRequest create(FriendRequest request) {
        logger.info("Creating friend request from '{}' to '{}'",
                request.getSender().getLogin(), request.getReceiver().getLogin()
        );
        mailService.sendEmail(
                request.getReceiver().getEmail(),
                "New friend request!",
                "You received a friend request from '" + request.getSender().getName() + "'! Check it out!"
        );
        return friendRequestRepository.save(request);
    }

    public Optional<FriendRequest> findById(Long id) {
        logger.debug("Received request for finding friend request with id '{}'", id);
        return friendRequestRepository.findById(id);
    }

    public List<FriendRequest> findAllOutgoing(User sender) {
        logger.debug("Received request for finding all outgoing friend request for sender '{}'", sender.getLogin());
        return friendRequestRepository.findAllBySender(sender);
    }

    public List<FriendRequest> findAllIncoming(User receiver) {
        logger.debug("Received request for finding all incoming friend request for receiver '{}", receiver.getLogin());
        return friendRequestRepository.findAllByReceiver(receiver);
    }

    public FriendRequest acceptRequest(FriendRequest request) {
        logger.info("Accepting friend request with id '{}', sender is '{}', receiver is '{}'",
                request.getId(), request.getSender().getLogin(), request.getReceiver().getLogin()
        );
        request.setStatus(ACCEPTED);
        userService.befriend(request.getSender(), request.getReceiver());
        return friendRequestRepository.save(request);
    }

    public FriendRequest rejectRequest(FriendRequest request) {
        logger.info("Rejecting friend request with id '{}', sender is '{}', receiver is '{}'",
                request.getId(), request.getSender().getLogin(), request.getReceiver().getLogin()
        );
        request.setStatus(REJECTED);
        return friendRequestRepository.save(request);
    }

    public void deleteRequest(FriendRequest request) {
        logger.info("Deleting friend request, id is '{}', sender is '{}', receiver is '{}'",
                request.getId(), request.getSender().getLogin(), request.getReceiver().getLogin()
        );
        friendRequestRepository.delete(request);
    }
}
