package kg.startproject.mobimarket_1.service;

import kg.startproject.mobimarket_1.dto.request.SmsRequest;

public interface SmsSender {
    void sendSms(SmsRequest smsRequest);

}
