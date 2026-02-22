package org.hollingdale.jvesta;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class VestaJackson {
  private ObjectMapper mapper = new ObjectMapper();

  String serialise(Object object) throws IOException {
    return mapper.writeValueAsString(object);
  }
}
