package kg.startproject.mobimarket_1.service;

import kg.startproject.mobimarket_1.dto.request.SmsRequest;
import kg.startproject.mobimarket_1.model.User;
import kg.startproject.mobimarket_1.model.VerificationCode;
import kg.startproject.mobimarket_1.repository.UserRepository;
import kg.startproject.mobimarket_1.repository.VerificationCodeRepository;
import kg.startproject.mobimarket_1.twillio.TwilioSmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class SmsService {
    private final SmsSender smsSender;
    private final VerificationCodeRepository verificationCodeRepository;

    private final UserRepository userRepository;

    @Autowired
    public SmsService(@Qualifier("twilio") TwilioSmsSender smsSender, VerificationCodeRepository verificationCodeRepository, UserRepository userRepository) {
        this.smsSender = smsSender;
        this.verificationCodeRepository = verificationCodeRepository;
        this.userRepository = userRepository;
    }

    public void sendVerificationCode(String phoneNumber) {
        String verificationCode = generateVerificationCode();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = currentTime.plusMinutes(5);


        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user != null) {
            VerificationCode Verificationcode = new VerificationCode();
            Verificationcode.setCode(verificationCode);
            Verificationcode.setPhoneNumber(phoneNumber);
            Verificationcode.setUser(user);
            Verificationcode.setExpirationTime(expirationTime);
            verificationCodeRepository.save(Verificationcode);
        }


        String message = "Your verification code is: " + verificationCode;
        SmsRequest smsRequest = new SmsRequest(phoneNumber, message);
        smsSender.sendSms(smsRequest);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000; // Generates a random 4-digit code
        return String.valueOf(code);
    }
}
