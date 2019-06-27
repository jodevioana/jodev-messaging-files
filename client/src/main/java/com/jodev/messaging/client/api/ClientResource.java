package com.jodev.messaging.client.api;

import com.jodev.messaging.client.service.AuthorizationService;
import com.jodev.messaging.client.service.DataService;
import com.jodev.messaging.client.messaging.MessageSender;
import com.jodev.messaging.client.model.FileMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

import static com.jodev.messaging.client.utils.Utils.toJson;

@RestController
@EnableCircuitBreaker
public class ClientResource {

  @Autowired
  private AuthorizationService authorizationService;

  @Autowired
  private DataService dataService;

  @Autowired
  private MessageSender messageSender;

  @GetMapping("/resource")
  private ResponseEntity getResource(@RequestParam("filename") String filename) {
    try {
      //get token to add to message
      String token = authorizationService.getToken("admin", "admin");

      //file to send
      File file = ResourceUtils.getFile("classpath:" + filename);

      //file saved into database
      String id = dataService.saveDbFile(file);

      //send message to RabbitMQ
      FileMessage message = new FileMessage();
      message.setFileId(id);
      message.setFileName(file.getName());
      message.setToken(token);
      messageSender.send(message);

      //return the sent message just as an example
      return ResponseEntity.ok().body(toJson(message));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
