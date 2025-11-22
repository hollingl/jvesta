package org.hollingdale.jvesta;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
public class MessageWriter {

  private static final String PADDING = " ";

  private int line = 0;
  private HorizontalAlignment alignment = HorizontalAlignment.LEFT;
  private String template = "";
  private Map<String, Object> parameters;

  public void writeTo(VestaMessagePayload payload) {
    // Render template
    var rendered = new StringBuilder();
    var index = 0;
    while (true) {
      var next = template.indexOf("{{", index);
      if (next == -1) {
        break;
      }

      // Write string before variable
      rendered.append(template.substring(index, next));

      var end = template.indexOf("}}", next);
      var token = template.substring(next + 2, end);
      var tokens = token.split(",", 2);
      var formatter = (String) null;

      if (tokens.length > 1) {
        token = tokens[0];
        formatter = tokens[1];
      }

      var variable = parameters == null || !parameters.containsKey(token) ? "" : parameters.get(token);
      var formatted = format(variable, formatter);

      // Render variable value
      rendered.append(formatted);
      index = end + 2;
    }

    // Write content after last variable
    rendered.append(template.substring(index));

    // Encode
    var encoded = VestaEncoder.encode(rendered.toString());

    // Write to payload
    var column = 0;
    if (alignment == HorizontalAlignment.RIGHT) {
      column = VestaMessagePayload.COLUMNS - encoded.size();
    } else if (alignment == HorizontalAlignment.CENTER) {
      column = (VestaMessagePayload.COLUMNS - encoded.size()) / 2;
    }
    payload.overlay(encoded, line, column);
  }

  public MessageWriter parameter(String name, Object value) {
    if (parameters == null) {
      parameters = new HashMap<>();
    }
    parameters.put(name, value);
    return this;
  }

  private static String format(Object value, String format) {

    if (format == null) {
      return String.valueOf(value);
    } else if (format.startsWith("%")) {
      return String.format(format, value);
    } else {
      var alignment = HorizontalAlignment.LEFT;
      var width = 0;

      if (format.startsWith("=")) {
        alignment = HorizontalAlignment.CENTER;
        width = Integer.parseInt(format.substring(1));
      } else {
        width = Integer.parseInt(format);
        if (width > 0) {
          alignment = HorizontalAlignment.RIGHT;
        } else {
          width = -width;
        }
      }

      // Do we need to truncate?
      var toFormat = String.valueOf(value);
      if (toFormat.length() > width) {
        toFormat = truncate(toFormat, width, alignment);
      }

      var padding = width - toFormat.length();
      var padLeft = 0;
      var padRight = 0;
      switch (alignment) {
        case LEFT:
          padRight = padding;
          break;
        case RIGHT:
          padLeft = padding;
          break;
        case CENTER:
          padLeft = padding / 2;
          padRight = padding - padLeft;
          break;
      }

      return PADDING.repeat(padLeft) + toFormat + PADDING.repeat(padRight);
    }
  }

  private static String truncate(String s, int width, HorizontalAlignment alignment) {
    var offset = 0;
    if (alignment == HorizontalAlignment.RIGHT) {
      offset = s.length() - width;
    } else if (alignment == HorizontalAlignment.CENTER) {
      offset = (s.length() - width) / 2;
    }

    return s.substring(offset, offset + width);
  }
}
