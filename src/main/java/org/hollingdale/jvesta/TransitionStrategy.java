package org.hollingdale.jvesta;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransitionStrategy {
  COLUMN,
  REVERSE_COLUMN,
  EDGES_TO_CENTER,
  ROW,
  DIAGONAL,
  RANDOM;

  @JsonValue
  String toJson() {
    return this.toString().toLowerCase(Locale.getDefault()).replace("_", "-");
  }

  static TransitionStrategy getDefault() {
    return COLUMN;
  }

}
