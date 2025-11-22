package org.hollingdale.jvesta;

import java.util.Arrays;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum VestaCharacter {

    BLANK(0, " "),
    A(1, "A"),
    B(2, "B"),
    C(3, "C"),
    D(4, "D"),
    E(5, "E"),
    F(6, "F"),
    G(7, "G"),
    H(8, "H"),
    I(9, "I"),
    J(10, "J"),
    K(11, "K"),
    L(12, "L"),
    M(13, "M"),
    N(14, "N"),
    O(15, "O"),
    P(16, "P"),
    Q(17, "Q"),
    R(18, "R"),
    S(19, "S"),
    T(20, "T"),
    U(21, "U"),
    V(22, "V"),
    W(23, "W"),
    X(24, "X"),
    Y(25, "Y"),
    Z(26, "Z"),
    D1(27, "1"),
    D2(28, "2"),
    D3(29, "3"),
    D4(30, "4"),
    D5(31, "5"),
    D6(32, "6"),
    D7(33, "7"),
    D8(34, "8"),
    D9(35, "9"),
    D0(36, "0"),
    BANG(37, "!"),
    AT(38, "@"),
    HASH(39, "#"),
    DOLLAR(40, "$"),
    LEFT(41, "("),
    RIGHT(42, ")"),
    HYPHEN(44, "-"),
    PLUS(46, "+"),
    AMP(47, "&"),
    EQUALS(48, "="),
    SEMI(49, ";"),
    COLON(50, ":"),
    SINGLE(52, "'"),
    DOUBLE(53, "\""),
    PERCENT(54, "%"),
    COMMA(55, ","),
    DOT(56, "."),
    SLASH(59, "/"),
    QUESTION(60, "?"),
    DEGREE(62, "°"),
    CRED(63, "{R}"),
    CORANGE(64, "{O}"),
    CYELLOW(65, "{Y}"),
    CGREEN(66, "{G}"),
    CBLUE(67, "{B}"),
    CVIOLET(68, "{V}"),
    CWHITE(69, "{W}"),
    CBLACK(70, "{K}");

    @Getter(AccessLevel.PACKAGE)
    private final int code;
    @Getter(AccessLevel.PACKAGE)
    private final String token;

    static Optional<VestaCharacter> fromCode(int code) {
        return Arrays.stream(values())
                .filter(c -> code == c.code)
                .findFirst();
    }

    static Optional<VestaCharacter> fromToken(String token) {
        return Arrays.stream(values())
                .filter(c -> c.token.equals(token))
                .findFirst();
    }
}
