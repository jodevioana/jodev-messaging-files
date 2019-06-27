package com.jodev.messaging.resourceserver.util;

import com.google.gson.Gson;

public class Utils {

  private static Gson GSON = new Gson();

  public static String toJson(Object object) {
    return GSON.toJson(object);
  }
}
