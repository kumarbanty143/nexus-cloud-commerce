package service;

import com.twilio.rest.chat.v1.service.channel.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {
    @Value("${twilio.from-number}")
    private String fromNumber;

    public void sendSms(String to , String message){
        Message.creator(to, fromNumber, message).create();
    }
}
