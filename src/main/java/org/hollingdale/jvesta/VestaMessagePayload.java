package org.hollingdale.jvesta;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class VestaMessagePayload {

  @Getter
  private List<List<Integer>> payload = new ArrayList<>();

  public VestaMessagePayload() {
    for (var i = 0; i < 6; i++) {
      var row = new ArrayList<Integer>();
      for (var j = 0; j < 22; j++) {
        row.add(0);
      }
      payload.add(row);
    }
  }

  public void overlay(List<Integer> codes, int row, int column) {
    if (row >= 0 && row < 6) {
      for (var i = 0; i < codes.size(); i++) {
        var col = column + i;
        if (col >= 0 && col < 22) {
          payload.get(row).set(col, codes.get(i));
        }
      }
    }
  }
}
