package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.NotificationsDTO;
import com.picpaysimplificado.endpoints.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    Endpoint endpoint;
    //String mocLabNotify = "http://o4d9z.mocklab.io/notify";

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotifications(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationsDTO notificationRequest = new NotificationsDTO(email, message);

        //ResponseEntity<String> notificationResponse = restTemplate.postForEntity(endpoint.mocLabNotify, notificationRequest, String.class);

        //if(!(notificationResponse.getStatusCode() == HttpStatus.OK)){
            //System.out.println("Erro ao enviar post request");
            //throw new Exception("Serviço de notificação está fora do ar");}

        System.out.println("deu certo carai");

    }
}
