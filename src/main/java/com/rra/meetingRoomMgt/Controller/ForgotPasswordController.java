package com.rra.meetingRoomMgt.Controller;
import com.rra.meetingRoomMgt.Repository.ForgetPasswordRepo;
import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.MailSenderService;
import com.rra.meetingRoomMgt.dto.MailBody;
import com.rra.meetingRoomMgt.dto.request.ChangePassword;
import com.rra.meetingRoomMgt.modal.ForgotPassword;
import com.rra.meetingRoomMgt.modal.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/home")
public class ForgotPasswordController {
    @Autowired
    private final UserRepository userRepository;
    private final MailSenderService mailSenderService;
    @Autowired
    private ForgetPasswordRepo forgetPasswordRepo;


    @Autowired
    private PasswordEncoder encoder;


    public ForgotPasswordController(UserRepository userRepository , MailSenderService mailSenderService) {
        this.userRepository=userRepository;
        this.mailSenderService = mailSenderService;

    }

    @RequestMapping("/VerifyEmail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {


        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required.");
        }

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Your Email is not found"));

        Optional<ForgotPassword> existingEntry = forgetPasswordRepo.findByUsersEmail(email);
        System.out.println("Ndahagera 000");
        if (existingEntry.isPresent()) {
            // If an existing entry is found, update the OTP
            ForgotPassword existingForgotPassword = existingEntry.get();
            int otp = GenerateOTP();
            existingForgotPassword.setOTP(otp);
            existingForgotPassword.setExpiredDate(new Date(System.currentTimeMillis() + 4 * 60 * 10000));
            forgetPasswordRepo.save(existingForgotPassword);
            System.out.println("Ndahagera");
            MailBody mailBody = MailBody.builder()
                    .to(email)
                    .text("Here Is Your Updated OTP: " + otp)
                    .subject("Updated OTP for Forget Password")
                    .build();
            System.out.println("Ndahagera 1");
            mailSenderService.EmailSender(mailBody);
            System.out.println("Ndahagera 2");
            return ResponseEntity.ok("Email already sent for verification with updated OTP");
        } else {
            // If no existing entry is found, save a new OTP
            int otp = GenerateOTP();
            MailBody mailBody = MailBody.builder()
                    .to(email)
                    .text("Here Is Your OTP: " + otp)
                    .subject("OTP for Forget Password")
                    .build();

            ForgotPassword forgotPassword = ForgotPassword.builder()
                    .OTP(otp)
                    .users(user)
                    .ExpiredDate(new Date(System.currentTimeMillis() + 4 * 60 * 10000))
                    .build();

            mailSenderService.EmailSender(mailBody);
            forgetPasswordRepo.save(forgotPassword);

            return ResponseEntity.ok("Email sent for verification");
        }
    }




    @RequestMapping("/VerifyOTP/{otp}/{email}")
public ResponseEntity<String> VerifyOtp(@PathVariable Integer otp,@PathVariable String email){
    Users user =  userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Your Email is not  found"));
    ForgotPassword forgotPassword = forgetPasswordRepo.findOtpAndEmail(otp,user).orElseThrow(()->new RuntimeException("Invalid OTP for Email" + email));

    if(forgotPassword.getExpiredDate().before(Date.from(Instant.now()))){
        forgetPasswordRepo.deleteById(forgotPassword.getID());
        return new ResponseEntity<>("OTP Failed", HttpStatus.EXPECTATION_FAILED);
    }
    return new  ResponseEntity<>("OTP Verified", HttpStatus.OK);
}

@RequestMapping("/ChangePassword/{email}")
public ResponseEntity<String> ChangePassword(@PathVariable String email, @RequestBody ChangePassword changePassword){

    if (!Objects.equals(changePassword.password(),changePassword.confirmPassw())){
        return new ResponseEntity<>("Password are not matching ",HttpStatus.BAD_REQUEST);
    }

    String encodePassword = encoder.encode(changePassword.password());
      userRepository.UpdatePassword(email,encodePassword);


    return new ResponseEntity<>("Password has changed Successfully",HttpStatus.OK);
}

 private int GenerateOTP(){
    Random random = new Random();
    return  random.nextInt(100000, 999999);
}
}
