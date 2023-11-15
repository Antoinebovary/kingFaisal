package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.Repository.AuthorityRepository;
import com.rra.meetingRoomMgt.Repository.UserAuthorityRepository;
import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.UserAuthorityService;
import com.rra.meetingRoomMgt.modal.Authority;
import com.rra.meetingRoomMgt.modal.User_Authority;
import com.rra.meetingRoomMgt.modal.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthorityImpl implements UserAuthorityService {

    private final UserAuthorityRepository userAuthorityRepo;
    private final UserRepository usersRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public Object mapRoles(User_Authority userAuthority) {
        // Retrieve the Authority entity using Authority_no
        Authority authority = authorityRepository.findById(userAuthority.getAuthority().getAuthorityNo())
                .orElse(null);

        // Retrieve the Users entity using user_no
        Users user = usersRepository.findById(userAuthority.getUser().getUserNo())
                .orElse(null);

        User_Authority auth = new User_Authority();
        auth.setAuthority(authority);
        auth.setUser(user);
        auth.setStatus(1);

        LocalDateTime currentTimestamp = LocalDateTime.now();
        auth.setCreatedAt(currentTimestamp);
        auth.setUpdatedAt(currentTimestamp);

        return userAuthorityRepo.save(auth);
    }

    @Override
    public List<User_Authority> retrieveMappedRoles() {
        return userAuthorityRepo.findAll();
    }

    @Override
    public Object updateUserRoles(User_Authority userAuthority) {

        User_Authority existingUserAuthority = userAuthorityRepo.findById(userAuthority.getId()).orElse(null);

        if (existingUserAuthority == null) {
            return null;
        }

        int status = existingUserAuthority.getStatus();
        LocalDateTime createdAt = existingUserAuthority.getCreatedAt();

        existingUserAuthority.setAuthority(userAuthority.getAuthority());
        existingUserAuthority.setUser(userAuthority.getUser());

        LocalDateTime updatedAt = LocalDateTime.now();
        existingUserAuthority.setUpdatedAt(updatedAt);

        existingUserAuthority.setStatus(status);
        existingUserAuthority.setCreatedAt(createdAt);

        return userAuthorityRepo.save(existingUserAuthority);
    }

    @Override
    public Object deleteUserRoles(int id, int newStatus) {
        return userAuthorityRepo.updateAuthorityByStatus(id, newStatus);
    }
}
