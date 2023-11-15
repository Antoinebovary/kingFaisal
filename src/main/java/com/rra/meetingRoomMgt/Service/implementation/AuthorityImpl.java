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
        // Step 1: Fetch the existing entity by ID
        Authority existingAuthority = authorityRepo.findById(authority.getAuthorityNo()).orElse(null);

        if (existingAuthority == null) {
            // Handle the case where the entity with the given ID doesn't exist
            return null;
        }

        // Step 2: Update the properties of the existing entity
        existingAuthority.setAuthorityName(authority.getAuthorityName());

        Authority auth = new Authority();
        auth.setStatus(1);

        // Preserve the existing createdAt value
        LocalDateTime createdAt = existingAuthority.getCreatedAt();

        // Set updatedAt with the current timestamp
        LocalDateTime updatedAt = LocalDateTime.now();
        authority.setUpdatedAt(updatedAt);

        // Set the preserved createdAt value
        authority.setCreatedAt(createdAt);

        return authorityRepo.save(authority);
    }

    @Override
    public void deleteRoles(int id, int newStatus) {




        authorityRepo.updateAuthorityByStatus(id, newStatus);
    }
}
