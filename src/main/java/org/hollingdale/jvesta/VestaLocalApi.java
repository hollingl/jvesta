package org.hollingdale.jvesta;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VestaLocalApi {

  private static final String DEFAULT_LOCALHOST = "localhost:7000";
  private static final String DEFAULT_TOKEN = "dummy";

  private final String hostAndPort;
  private final String token;
  private final VestaJackson jackson = new VestaJackson();

  public static VestaLocalApi localhost() {
    return new VestaLocalApi(DEFAULT_LOCALHOST, DEFAULT_TOKEN);
  }

  public boolean setMessage(VestaMessagePayload payload) {
    return send(payload.getPayload());
  }

  public boolean setMessageWithOptions(VestaMessagePayloadWithOptions messageWithOptions) {
    return send(messageWithOptions);
  }

  private boolean send(Object payload) {
    try {
      var payloadString = jackson.serialise(payload);

      var uri = new URI("http://%s/local-api/message".formatted(hostAndPort));
      var request = HttpRequest.newBuilder()
          .uri(uri)
          .header("X-Vestaboard-Local-Api-Key", token)
          .header("content-type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(payloadString))
          .build();

      var response = HttpClient.newBuilder()
          .build()
          .send(request, BodyHandlers.discarding());

      return response.statusCode() == 200;

    } catch (URISyntaxException | IOException | InterruptedException x) {
      x.printStackTrace(System.err);
    }
    return false;
  }

}
