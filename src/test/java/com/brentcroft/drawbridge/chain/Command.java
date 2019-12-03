package com.brentcroft.drawbridge.chain;

import static java.lang.String.format;

public interface Command<CONTEXT> {

    void action( CONTEXT context );

    default void notifyPlay( Object player, Object action, Object cards )
    {
        System.out.println( format( "[%-8s] %-15s %s", player == null ? "*" : player, action, cards ) );
    }
}
