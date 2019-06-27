package com.jodev.messaging.client.service;

import com.jodev.messaging.client.model.TokenResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class AuthorizationService {

  @Autowired
  private RestTemplate restTemplate;

  public String getToken(String username, String password) {
    String credentials = "web_app:secret";
    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.add("Authorization", "Basic " + encodedCredentials);

    HttpEntity<String> request = new HttpEntity<String>(headers);


    String url = "http://authorization-server/oauth/token?grant_type=password&username=" + username + "&password=" + password;
    ResponseEntity<TokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, TokenResponse.class);
    return response.getBody().getAccess_token();
  }
}
