package org.hollingdale.jvesta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class VestaEncoder {

  static List<Integer> encode(String string) {
    var encoded = new ArrayList<Integer>();

    if (string != null) {
      var upper = string.toUpperCase();
      var offset = 0;
      while (true) {
        var optToken = nextToken(upper, offset);
        if (optToken.isEmpty()) {
          break;
        } else {
          var token = optToken.get();
          encoded.add(
              VestaCharacter.fromToken(token).orElse(VestaCharacter.BLANK)
                  .getCode());
          offset += token.length();
        }
      }
    }

    return encoded;
  }

  private static Optional<String> nextToken(String s, int offset) {
    if (s != null && s.length() > 0 && offset < s.length()) {
      if (s.charAt(offset) == '{') {
        var end = s.indexOf('}', offset);
        if (end != -1) {
          return Optional.of(s.substring(offset, end + 1));
        }
      } else {
        return Optional.of(s.substring(offset, offset + 1));
      }
    }
    return Optional.empty();
  }
}
