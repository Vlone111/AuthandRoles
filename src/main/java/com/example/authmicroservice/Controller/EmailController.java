package com.example.authmicroservice.Controller;


import com.example.authmicroservice.Dto.EmailDto;
import com.example.authmicroservice.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> loginWithEmail(@RequestBody EmailDto emailTo) {
        try {
            String subject = "Для того чтобы зайти на наш сайт введите данные 4х значный код";
            int ramndom = (int)(Math.random() * 9000) + 1000;
            String randomintvalue = String.valueOf(ramndom);
            emailService.sendEmail(emailTo.getEmail(),subject,randomintvalue);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Код с подтверждением был отправлен на "+ emailTo);
    }


}
