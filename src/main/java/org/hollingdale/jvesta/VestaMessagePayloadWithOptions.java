package org.hollingdale.jvesta;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VestaMessagePayloadWithOptions {
  private List<List<Integer>> characters;
  @Builder.Default
  private TransitionStrategy strategy = TransitionStrategy.getDefault();
  @JsonProperty("step_interval_ms")
  private Long stepIntervalMs;
  @JsonProperty("step_size")
  private Integer stepSize;
}
