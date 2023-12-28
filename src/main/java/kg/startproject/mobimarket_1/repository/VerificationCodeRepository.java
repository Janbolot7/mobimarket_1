package kg.startproject.mobimarket_1.repository;


import kg.startproject.mobimarket_1.model.User;
import kg.startproject.mobimarket_1.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    VerificationCode findByPhoneNumber(String phoneNumber);
    VerificationCode findByPhoneNumberAndUser(String phoneNumber, User user);
    void deleteByPhoneNumber(String phoneNumber);

}
