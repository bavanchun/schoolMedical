package com.schoolhealth.schoolmedical.service.Notification;

import com.google.firebase.messaging.*;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.UserNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FCMImpl implements FCMService{

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Override
    public void sendNotification(Map<String,List<Pupil>> pupilsMap, Long sourceId, String type, String title) {
        if (pupilsMap == null || pupilsMap.isEmpty()) {
            System.out.println("Không có học sinh nào để gửi thông báo");
        }
        List<Message> messages = new ArrayList<>();
        for(Map.Entry<String, List<Pupil>> entry : pupilsMap.entrySet()) {
            String token = entry.getKey();
            List<Pupil> pupils = entry.getValue();

            if (token == null || token.isEmpty()) {
                System.out.println("Token không hợp lệ");
                continue;
            }

            for (Pupil pupil : pupils) {
                Message message = Message.builder()
                        .setToken(token)
                        .putData("title", title)
                        .putAllData(Map.of(
                                "campaignId", String.valueOf(sourceId),
                                "campaignType", type,
                                "pupilId", String.valueOf(pupil.getPupilId()),
                                "pupilName", pupil.getFirstName(),
                                "pupilLastName", pupil.getLastName()
                        ))
                        .build();
                messages.add(message);
            }
        }
        try {
            BatchResponse response = firebaseMessaging.sendEach(messages);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    @Override
    public void sendMulticastNotification(List<String> tokens, UserNotification usernotification) {
        if (tokens == null || tokens.isEmpty()) {
            System.out.println("Không có thiết bị nào để gửi");
            return;
        }

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .putAllData(Map.of(
                        "title", usernotification.getMessage(),
                        "campaignId", String.valueOf(usernotification.getSourceId()),
                        "campaignType", usernotification.getTypeNotification().toString()
                ))
                .build();

        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
            System.out.println("Gửi thành công đến " + response.getSuccessCount() + " thiết bị");
        } catch (FirebaseMessagingException e) {
            e.getStackTrace();
        }
    }
}

