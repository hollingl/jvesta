package org.hollingdale.jvesta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OptionsTests {

  @Test
  void testOptionsWrittenCorrectly() throws IOException {

    var payloadWithOptions = VestaMessagePayloadWithOptions.builder()
        .characters(List.of(List.of(26)))
        .stepIntervalMs(250L)
        .strategy(TransitionStrategy.EDGES_TO_CENTER)
        .stepSize(2)
        .build();

    var serialised = new VestaJackson().serialise(payloadWithOptions);

    var deserialised = new ObjectMapper().readTree(serialised);
    assertEquals("edges-to-center", deserialised.get("strategy").asText());
    assertEquals(250, deserialised.get("step_interval_ms").asLong());
    assertEquals(2, deserialised.get("step_size").asInt());
    assertEquals(26, deserialised.get("characters").get(0).get(0).asInt());
  }
}
