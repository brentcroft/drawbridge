package com.brentcroft.drawbridge.chain;

public interface Guard<CONTEXT> {

    boolean isSatisfied( CONTEXT context );

}
