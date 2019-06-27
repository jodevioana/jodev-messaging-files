package com.jodev.messaging.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class DataService {

  @Autowired
  private GridFsOperations gridOperations;

  public String saveDbFile(File file) throws FileNotFoundException {
    String id = gridOperations.store(new FileInputStream(file), file.getName()).toString();
    return id;
  }
}
