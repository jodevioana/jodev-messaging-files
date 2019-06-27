package com.jodev.messaging.resourceserver.messaging;

import com.jodev.messaging.resourceserver.util.Utils;
import com.jodev.messaging.resourceserver.service.FileService;
import com.jodev.messaging.resourceserver.model.FileMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Service
public class MessageSender {
  private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);

  @Autowired
  private Source source;

  @Autowired
  private FileService fileService;

  @Autowired
  private MessageSender messageSender;

  @Autowired
  private TokenStore tokenStore;


  public boolean send(FileMessage fileMessage) {
    LOGGER.info("File message sent: {}", Utils.toJson(fileMessage));
    return this.source.output().send(MessageBuilder.withPayload(fileMessage).build());
  }

  @StreamListener(Processor.INPUT)
  public void receive(FileMessage fileMessage) {
    LOGGER.info("File message received: {}", Utils.toJson(fileMessage));

    //validate token
    try {
      OAuth2Authentication authentication = tokenStore.readAuthentication(fileMessage.getToken());
      SecurityContext ctx = SecurityContextHolder.createEmptyContext();
      ctx.setAuthentication(authentication);
      SecurityContextHolder.setContext(ctx);
      try {
        //process file
        File filetoProcess = fileService.getFileFromMessage(fileMessage);
        String response = "File length is " + fileService.getLength(filetoProcess);
        filetoProcess.delete();

        //send the message
        if(response == null || response.isEmpty()) {
          fileMessage.setError("ERROR");
        } else {
          fileMessage.setResponse(response);
        }
        messageSender.send(fileMessage);
      } catch (AccessDeniedException e) {
        LOGGER.error("File message not authorized: {}", e.getMessage());
        fileMessage.setError("UNAUTHORIZED");
        messageSender.send(fileMessage);
      } catch (IOException e) {
        LOGGER.error("Error: {}", e.getMessage());
        fileMessage.setError("INTERNAL_SERVER_ERROR: " + e.getMessage());
        messageSender.send(fileMessage);
      }
    }
    finally {
      SecurityContextHolder.clearContext();
    }
  }
}
