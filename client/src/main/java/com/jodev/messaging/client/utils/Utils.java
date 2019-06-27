package com.jodev.messaging.client.utils;

import com.google.gson.Gson;

public class Utils {

  private static Gson GSON = new Gson();

  public static String toJson(Object object) {
    return GSON.toJson(object);
  }
}
