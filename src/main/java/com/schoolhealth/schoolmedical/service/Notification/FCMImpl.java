package com.schoolhealth.schoolmedical.service.Notification;

import com.google.firebase.messaging.*;
import com.schoolhealth.schoolmedical.entity.UserNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FCMImpl implements FCMService{

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Override
    public String sendNotification(UserNotification notification, String token) {
        Message message = Message.builder()
                .setToken(token)
                .putData("title", notification.getMessage())
                .putAllData(Map.of(
                        "campaignId", String.valueOf(notification.getSourceId()),
                        "campaignType", notification.getTypeNotification().toString(),
                        "parentId", String.valueOf(notification.getUser().getUserId())
                ))
                .build();
        try {
            String response = firebaseMessaging.send(message);
            System.out.println("Successfully sent message: " + response);
            return response;
        } catch (FirebaseMessagingException e) {
            System.err.println("Error sending message: " + e.getMessage());
            return "Error sending message: " + e.getMessage();
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

