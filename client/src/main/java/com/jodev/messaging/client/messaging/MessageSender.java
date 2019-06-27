package com.jodev.messaging.client.messaging;

import com.jodev.messaging.client.service.FileService;
import com.jodev.messaging.client.model.FileMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import static com.jodev.messaging.client.utils.Utils.toJson;

@Service
public class MessageSender {
  private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);

  @Autowired
  private Source source;

  @Autowired
  private TokenStore tokenStore;

  @Autowired
  private FileService fileService;

  public boolean send(FileMessage fileMessage) {
    LOGGER.info("File message sent: {}", toJson(fileMessage));
    return this.source.output().send(MessageBuilder.withPayload(fileMessage).build());
  }

  @StreamListener(Processor.INPUT)
  public void receive(FileMessage fileMessage) {
    LOGGER.info("File message received: {}", toJson(fileMessage));
    //authenticate token
    try {
      OAuth2Authentication authentication = tokenStore.readAuthentication(fileMessage.getToken());
      SecurityContext ctx = SecurityContextHolder.createEmptyContext();
      ctx.setAuthentication(authentication);
      SecurityContextHolder.setContext(ctx);
      try {
        //process incoming file
        fileService.processFileMessage(fileMessage);
      } catch (AccessDeniedException e) {
        LOGGER.error("File message not authorized: {}", e.getMessage());
      }
    } finally {
      SecurityContextHolder.clearContext();
    }
  }
}
