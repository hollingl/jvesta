package org.hollingdale.jvesta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class MessageWriterTests {

    @Test
    void testDefault() {
        runTest(
                new MessageWriter()
                        .template("hello world"),
                "HELLO WORLD           ");
    }

    @Test
    void testLeft() {
        runTest(
                new MessageWriter()
                        .template("hello world")
                        .alignment(HorizontalAlignment.LEFT),
                "HELLO WORLD           ");
    }

    @Test
    void testCentered() {
        runTest(
                new MessageWriter()
                        .template("hello world")
                        .alignment(HorizontalAlignment.CENTER),
                "     HELLO WORLD      ");
    }

    @Test
    void testRight() {
        runTest(
                new MessageWriter()
                        .template("hello world")
                        .alignment(HorizontalAlignment.RIGHT),
                "           HELLO WORLD");
    }

    @Test
    void testTruncated() {
        runTest(
                new MessageWriter()
                        .template("hello world and universe"),
                "HELLO WORLD AND UNIVER");
    }

    @Test
    void testTruncatedLeft() {
        runTest(
                new MessageWriter()
                        .template("hello world and universe")
                        .alignment(HorizontalAlignment.LEFT),
                "HELLO WORLD AND UNIVER");
    }

    @Test
    void testTruncatedCenter() {
        runTest(
                new MessageWriter()
                        .template("hello world and universe")
                        .alignment(HorizontalAlignment.CENTER),
                "ELLO WORLD AND UNIVERS");
    }

    @Test
    void testTruncatedRight() {
        runTest(
                new MessageWriter()
                        .template("hello world and universe")
                        .alignment(HorizontalAlignment.RIGHT),
                "LLO WORLD AND UNIVERSE");
    }

    @Test
    void testMissingVariable() {
        runTest(
                new MessageWriter()
                        .template("hello {{name}}!"),
                "HELLO !               ");
    }

    @Test
    void testVariable() {
        runTest(
                new MessageWriter()
                        .template("hello {{name}}!")
                        .parameter("name", "samantha"),
                "HELLO SAMANTHA!       ");
    }

    @Test
    void testVariableLeft() {
        runTest(
                new MessageWriter()
                        .template("hello {{name,-15}}!")
                        .parameter("name", "samantha"),
                "HELLO SAMANTHA       !");
    }

    @Test
    void testVariableRight() {
        runTest(
                new MessageWriter()
                        .template("hello {{name,15}}!")
                        .parameter("name", "samantha"),
                "HELLO        SAMANTHA!");
    }

    @Test
    void testVariableCenter() {
        runTest(
                new MessageWriter()
                        .template("hello {{name,=15}}!")
                        .parameter("name", "samantha"),
                "HELLO    SAMANTHA    !");
    }

    @Test
    void testVariableTruncatedRight() {
        runTest(
                new MessageWriter()
                        .template("hello {{name,3}}!")
                        .parameter("name", "samantha"),
                "HELLO THA!            ");
    }

    @Test
    void testVariableTruncatedLeft() {
        runTest(
                new MessageWriter()
                        .template("hello {{name,-3}}!")
                        .parameter("name", "samantha"),
                "HELLO SAM!            ");
    }

    @Test
    void writeMultipleVariables() {
        runTest(
                new MessageWriter()
                        .template("{{ht,7}} {{hs}}-{{as}} {{at,-7}} {{t,2}}")
                        .parameters(Map.of(
                                "ht", "spurs",
                                "at", "arsenal",
                                "hs", 3,
                                "as", 1,
                                "t", "8")),
                "  SPURS 3-1 ARSENAL  8");
    }

    @Test
    void testBlocks() {
        var payload = new VestaMessagePayload();
        new MessageWriter()
                .alignment(HorizontalAlignment.CENTER)
                .template("{r}{o}{y}{g}{b}{v}{w}{k}")
                .writeTo(payload);

        assertEquals(
                List.of(0, 0, 0, 0, 0, 0, 0, 63, 64, 65, 66, 67, 68, 69, 70, 0, 0, 0, 0, 0, 0, 0),
                payload.getPayload().get(0));
    }

    @Test
    void testFormatted() {
        runTest(
                new MessageWriter()
                        .template("{{stock,-14}} {{price,%7.2f}}")
                        .parameters(Map.of("stock", "ibm", "price", 297.4)),
                "IBM             297.40");
    }

    @Test
    void testMultipleOverlays() {
        var payload = new VestaMessagePayload();

        new MessageWriter()
                .alignment(HorizontalAlignment.LEFT)
                .template("{{team}}")
                .parameter("team", "tottenham")
                .writeTo(payload);

        new MessageWriter()
                .alignment(HorizontalAlignment.RIGHT)
                .template("{{points}}")
                .parameter("points", 97)
                .writeTo(payload);

        new MessageWriter()
                .alignment(HorizontalAlignment.CENTER)
                .template("Champions")
                .line(1)
                .writeTo(payload);

        new MessageWriter()
                .alignment(HorizontalAlignment.RIGHT)
                .template("{g}")
                .line(1)
                .writeTo(payload);

        assertEquals("TOTTENHAM           97", getPayloadLineAsString(payload, 0));
        assertEquals("CHAMPIONS", getPayloadLineAsString(payload, 1).substring(6, 15));
        assertEquals(VestaCharacter.CGREEN, VestaCharacter.fromCode(payload.getPayload().get(1).get(21)).get());
    }

    private static void runTest(MessageWriter writer, String expected) {
        var payload = new VestaMessagePayload();
        writer.writeTo(payload);
        assertEquals(expected, getPayloadLineAsString(payload, 0));
    }

    private static String getPayloadLineAsString(VestaMessagePayload payload, int index) {
        return payload.getPayload().get(index).stream()
                .map(VestaCharacter::fromCode)
                .map(Optional::get)
                .map(VestaCharacter::getToken)
                .collect(Collectors.joining());
    }
}
