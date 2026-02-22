package org.hollingdale.jvesta.samples;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hollingdale.jvesta.HorizontalAlignment;
import org.hollingdale.jvesta.MessageWriter;
import org.hollingdale.jvesta.TransitionStrategy;
import org.hollingdale.jvesta.VestaLocalApi;
import org.hollingdale.jvesta.VestaMessagePayload;
import org.hollingdale.jvesta.VestaMessagePayloadWithOptions;

public class OptionsSample {
  public static void main(String[] args) {
    var payload = new VestaMessagePayload();

    new MessageWriter()
        .alignment(HorizontalAlignment.CENTER)
        .template("{r}{o}{y}{g}{b}{v}{w}{k}")
        .writeTo(payload);

    new MessageWriter()
        .alignment(HorizontalAlignment.CENTER)
        .template("welcome to")
        .line(2)
        .writeTo(payload);

    new MessageWriter()
        .alignment(HorizontalAlignment.CENTER)
        .template("vestaboard")
        .line(3)
        .writeTo(payload);

    new MessageWriter()
        .line(5)
        .template("{{date,-14}}{{time,-8}}")
        .parameter("date", LocalDate.now())
        .parameter("time", LocalTime.now())
        .writeTo(payload);

    var payloadWithOptions = VestaMessagePayloadWithOptions.builder()
        .characters(payload.getPayload())
        .strategy(TransitionStrategy.DIAGONAL)
        .stepIntervalMs(100L)
        .stepSize(5)
        .build();

    VestaLocalApi.localhost().setMessageWithOptions(payloadWithOptions);
  }
}
