package com.rra.meetingRoomMgt.Controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.JwtService;
import com.rra.meetingRoomMgt.Service.UnitsService;
import com.rra.meetingRoomMgt.dto.request.RefreshTokenRequest;
import com.rra.meetingRoomMgt.dto.response.JwtAuthenticationResponse;
import com.rra.meetingRoomMgt.modal.Departments;
import com.rra.meetingRoomMgt.modal.Units;
import com.rra.meetingRoomMgt.modal.Users;
import com.rra.meetingRoomMgt.Service.UserAuthenticationService;
import com.rra.meetingRoomMgt.dto.request.SignUpRequest;
import com.rra.meetingRoomMgt.dto.request.SigninRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/rra/v1/admin/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserAuthenticationService userAuthenticationService;
    private final UserRepository userRepository;

    @Autowired
    private UnitsService unitsService;

    private final JwtService jwtService;


    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpRequest request) {

        if (request.getFullnames() == null || request.getEmpNo() == null ||
                request.getPassword() == null || request.getMobileNo() == null ||
                request.getEmail() == null || request.getPosition() == null) {
            return ResponseEntity.badRequest().body(Map.of("msg", "All fields must be provided"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("msg", "Email already exists"));
        }

        if (userRepository.existsByEmpNo(request.getEmpNo())) {
            return ResponseEntity.badRequest().body(Map.of("msg", "Employee number already exists"));
        }

        if (!request.getMobileNo().matches("\\d+")) {
            return ResponseEntity.badRequest().body(Map.of("msg", "Phone number must contain only numbers"));
        }

        if (request.getUnitID() == null) {
            return ResponseEntity.badRequest().body(Map.of("msg", "Unit information is missing in the request."));
        }

        Optional<Units> optionalUnit = unitsService.findUnitsById(request.getUnitID());
        if (optionalUnit.isPresent()) {
            Units unit = optionalUnit.get();
            request.setUnits(unit);

            Departments department = unit.getDepartment();
            if (department != null) {
                request.setDepartments(department);
            } else {
                return ResponseEntity.badRequest().body(Map.of("msg", "Department not found for unitID: " + unit.getUnitID()));
            }

            Object savedUser = userAuthenticationService.signup(request);
            return ResponseEntity.ok(Map.of("msg", "account created successfully", "user", savedUser));
        } else {
            return ResponseEntity.badRequest().body(Map.of("msg", "Unit not found for unitID: " + request.getUnits().getUnitID()));
        }




    }

    @GetMapping(path = "/listall")
    public ResponseEntity<List<Users>>retrieveUsers() {
        List<Users> users =  userAuthenticationService.retrieveUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody Users updateUsers) {

        Optional<Units> optionalUnit = (Optional<Units>) unitsService.findUnitsById(updateUsers.getUnits().getUnitID());
        if (optionalUnit.isPresent()) {
            Units unit = optionalUnit.get();
            updateUsers.setUnits(unit);

            // Fetch the department associated with the unit
            Departments department = unit.getDepartment();
            if (department != null) {
                // Set the department in the user object
                updateUsers.setDepartments(department);
            } else {
                return ResponseEntity.badRequest().body(Map.of("msg", "Department not found for unitID: " + unit.getUnitID()));
            }

            Object updatedUser = userAuthenticationService.updateUsers(updateUsers);
            return ResponseEntity.ok(Map.of("msg", "User updated successfully", "user", updatedUser));
        } else {
            return ResponseEntity.badRequest().body(Map.of("msg", "Unit not found for unitID: " + updateUsers.getUnits().getUnitID()));
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestParam("staffID") Users deleteUser) {
        int id = deleteUser.getStaffID();
        String newStatus = deleteUser.getUserStatus();

        userAuthenticationService.deleteUsers(id, newStatus);
        return ResponseEntity.ok(Map.of("msg", "Users Deleted successfuly", "id", id));
    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(userAuthenticationService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/profile")
    public UserDetails getUserProfile(Authentication authentication) {
        // Retrieve user details from the authentication object
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                return (UserDetails) principal;
            }
        }
        return null;
    }





}
