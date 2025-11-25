package com.example.authmicroservice.Controller;


import com.example.authmicroservice.Dao.UserRepository;
import com.example.authmicroservice.Dto.EmailConfirmRequest;
import com.example.authmicroservice.Dto.EmailRequest;
import com.example.authmicroservice.Entity.User;
import com.example.authmicroservice.Jwt.JwtCore;
import com.example.authmicroservice.Jwt.UserDetailsImpl;
import com.example.authmicroservice.Service.EmailService;
import com.example.authmicroservice.Service.RedisService;
import com.example.authmicroservice.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RedisService redisService;
    private final JwtCore jwtCore;


    @PostMapping("/login")
    public ResponseEntity<?> loginWithEmail(@RequestBody EmailRequest emailTo) {
        try {
            String subject = "Для того чтобы зайти на наш сайт введите данные 4х значный код";
            int ramndom = (int)(Math.random() * 9000) + 1000;
            String randomintvalue = String.valueOf(ramndom);
            redisService.setOtp(emailTo.getEmail(), randomintvalue);
            emailService.sendEmail(emailTo.getEmail(),subject,randomintvalue);
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

        return ResponseEntity.ok("Код с подтверждением был отправлен на "+ emailTo.getEmail());
    }

    //to do: ошибки поправить вывод нормальный
    @PostMapping("/confirm") //контроллер для подтверждения одноразового пароля и создания юзера
    public ResponseEntity<?> confirmEmail(@RequestBody EmailConfirmRequest emailConfirmRequest, HttpServletResponse response) {
        UserDetailsImpl userDetails;
        if(emailConfirmRequest.getOtp()!=null){
            String otp = redisService.getOtp(emailConfirmRequest.getEmail(),emailConfirmRequest.getOtp());
            if(emailConfirmRequest.getOtp().equals(otp)){
                try {
                    User user = userService.createUser(emailConfirmRequest.getEmail());
                    userDetails = UserDetailsImpl.build(user);
                    jwtCore.putRefreshinHttpCockieOnly(jwtCore.generateRefreshToken(userDetails),response);
                    return ResponseEntity.ok("Пароль Подтвержден, вы вошли в свой аккаунт ваш access токен: "+jwtCore
                            .generateAccessToken(UserDetailsImpl.build(user)));
                }
                catch (Exception e) {
                    userDetails = UserDetailsImpl
                            .build(userRepository
                                    .findByEmail(emailConfirmRequest.getEmail()).orElseThrow());
                    jwtCore.putRefreshinHttpCockieOnly(jwtCore.generateRefreshToken(userDetails),response);
                    String accessToken = jwtCore.generateAccessToken(UserDetailsImpl
                            .build(userRepository
                                    .findByEmail(emailConfirmRequest.getEmail()).orElseThrow()));
                    return ResponseEntity.ok("Пароль Подтвержден, вы вошли в свой аккаунт ваш access токен: "+accessToken);
                }
            }
            else{
                return ResponseEntity.status(403).body("Срок вашего кода либо истек либо код неверен");
            }
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        try {
            return ResponseEntity.ok(jwtCore.refreshacessToken(refreshToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }



}
