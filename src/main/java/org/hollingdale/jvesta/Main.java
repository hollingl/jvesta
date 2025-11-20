package org.hollingdale.jvesta;

public class Main {
  public static void main(String[] args) {
    // scores();
    table();
    // airport();
  }

  private static void scores() {
    var payload = new VestaMessagePayload();
    payload.overlay(VestaEncoder.encode("spurs   3-1 arsenal 92"), 0, 0);
    payload.overlay(VestaEncoder.encode("man u   0-0 man c   FT"), 1, 0);
    payload.overlay(VestaEncoder.encode("b'ford  3-1 l'pool  FT"), 2, 0);
    payload.overlay(VestaEncoder.encode("chelsea 2-4 b'ton   89"), 3, 0);
    payload.overlay(VestaEncoder.encode("forest  0-2 wolves  FT"), 4, 0);
    payload.overlay(VestaEncoder.encode("villa   0-0 soton   FT"), 5, 0);
    VestaLocalApi.localhost().setMessage(payload);
  }

  private static void table() {
    var payload = new VestaMessagePayload();
    payload.overlay(VestaEncoder.encode("            PL  GD PTS"), 0, 0);
    payload.overlay(VestaEncoder.encode("1 tottenham 12 +14  36"), 1, 0);
    payload.overlay(VestaEncoder.encode("2 liverpool 12  +8  34"), 2, 0);
    payload.overlay(VestaEncoder.encode("3 brighton  12  +7  34"), 3, 0);
    payload.overlay(VestaEncoder.encode("2026-03-12       18:07"), 5, 0);
    VestaLocalApi.localhost().setMessage(payload);
  }

  private static void airport() {
    var payload = new VestaMessagePayload();
    payload.overlay(VestaEncoder.encode("1220 BA227   JFK   24{R}"), 0, 0);
    payload.overlay(VestaEncoder.encode("1225 EZY2231 CFU   16{Y}"), 1, 0);
    payload.overlay(VestaEncoder.encode("1245 AA112   SFO    8{G}"), 2, 0);
    payload.overlay(VestaEncoder.encode("1245 BA109   IAD   34{G}"), 3, 0);
    payload.overlay(VestaEncoder.encode("1300 LH2222  TOU"), 4, 0);
    payload.overlay(VestaEncoder.encode("1310 CH991   ZRH"), 5, 0);
    VestaLocalApi.localhost().setMessage(payload);
  }
}
