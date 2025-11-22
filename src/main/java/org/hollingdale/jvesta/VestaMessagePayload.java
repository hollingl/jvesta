package org.hollingdale.jvesta;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class VestaMessagePayload {

  static final int ROWS = 6;
  static final int COLUMNS = 22;

  @Getter
  private List<List<Integer>> payload = new ArrayList<>();

  public VestaMessagePayload() {
    for (var i = 0; i < ROWS; i++) {
      var row = new ArrayList<Integer>();
      for (var j = 0; j < COLUMNS; j++) {
        row.add(0);
      }
      payload.add(row);
    }
  }

  void overlay(List<Integer> codes, int row, int column) {
    if (row >= 0 && row < ROWS) {
      for (var i = 0; i < codes.size(); i++) {
        var col = column + i;
        if (col >= 0 && col < COLUMNS) {
          payload.get(row).set(col, codes.get(i));
        }
      }
    }
  }
}
