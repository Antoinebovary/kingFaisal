package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.Repository.AuthorityRepository;
import com.rra.meetingRoomMgt.Service.AuthorityService;
import com.rra.meetingRoomMgt.modal.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorityImpl implements AuthorityService {

    private final AuthorityRepository authorityRepo;


    @Override
    public Object saveRoles(Authority authority) {
        Authority auth = new Authority();
        auth.setAuthorityName(authority.getAuthorityName());
        auth.setStatus(1);

        // Set created_at and updated_at with the current timestamp
        LocalDateTime currentTimestamp = LocalDateTime.now();
        auth.setCreatedAt(currentTimestamp);
        auth.setUpdatedAt(currentTimestamp);

        return authorityRepo.save(auth);
    }

    @Override
    public List<Authority> retrieveRoles() {
        return authorityRepo.findAll();
    }

    @Override
    public Object updateRoles(Authority authority) {
        // Fetch the existing entity by ID
        Authority existingAuthority = authorityRepo.findById(authority.getAuthorityNo()).orElse(null);

        if (existingAuthority == null) {
            return null;
        }

        // Preserve the existing status value
        int status = existingAuthority.getStatus();

        // Preserve the existing createdAt value
        LocalDateTime createdAt = existingAuthority.getCreatedAt();

        // Step 2: Update the properties of the existing entity
        existingAuthority.setAuthorityName(authority.getAuthorityName());

        // Set updatedAt with the current timestamp
        LocalDateTime updatedAt = LocalDateTime.now();
        existingAuthority.setUpdatedAt(updatedAt);

        // Set the preserved status and createdAt values
        existingAuthority.setStatus(status);
        existingAuthority.setCreatedAt(createdAt);

        return authorityRepo.save(existingAuthority);
    }

    @Override
    public Object deleteRoles(int id, int newStatus) {
        return authorityRepo.updateRoomNamesByStatus(id, newStatus);
    }
}
