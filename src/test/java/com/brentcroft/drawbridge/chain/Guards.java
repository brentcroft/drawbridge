package com.brentcroft.drawbridge.chain;

import java.util.function.Predicate;

public final class Guards {

    private Guards() {
        // private constructor to prevent external instantiation
    }

    public static <CONTEXT> Guard<CONTEXT> onlyIf(Predicate<CONTEXT> predicate) {
        return predicate::test;
    }

}
