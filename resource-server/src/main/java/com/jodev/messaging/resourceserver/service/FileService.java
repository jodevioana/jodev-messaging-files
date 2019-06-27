package com.jodev.messaging.resourceserver.service;

import com.jodev.messaging.resourceserver.model.FileMessage;
import com.jodev.messaging.resourceserver.util.Utils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {

  @Autowired
  private DataService dataService;

  @PreAuthorize("hasRole('ADMIN')")
  public long getLength(File inputFile) {
    return inputFile.length();
  }

  @PreAuthorize("hasRole('ADMIN')")
  public File getFileFromMessage(FileMessage fileMessage) throws IOException {
    InputStream inputStream = dataService.getFileFromDB(fileMessage.getFileId());
    File newFile = new File(fileMessage.getFileName());
    FileUtils.copyInputStreamToFile(inputStream, newFile);
    //delete file from the database
    return newFile;
  }
}
