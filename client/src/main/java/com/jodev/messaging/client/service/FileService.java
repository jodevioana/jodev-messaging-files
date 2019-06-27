package com.jodev.messaging.client.service;

import com.jodev.messaging.client.model.FileMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class FileService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

  @PreAuthorize("hasRole('ADMIN')")
  public void processFileMessage(FileMessage fileMessage) {
    if(fileMessage.getError() == null || fileMessage.getError().isEmpty()) {
      LOGGER.info("File message valid: {}", fileMessage.getResponse());
    } else {
      LOGGER.info("File message invalid: {}", fileMessage.getError());
    }
  }
}
