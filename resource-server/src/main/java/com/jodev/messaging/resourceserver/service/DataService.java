package com.jodev.messaging.resourceserver.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class DataService {

  @Autowired
  private GridFsOperations gridOperations;

  public String saveDbFile(File file) throws FileNotFoundException {
    String id = gridOperations.store(new FileInputStream(file), file.getName()).toString();
    return id;
  }

  public InputStream getFileFromDB(String id) throws IOException {
    GridFSFile file = gridOperations.findOne(new Query(Criteria.where("_id").is(id)));
    GridFsResource resource = gridOperations.getResource(file);
    return resource.getInputStream();
  }
}
