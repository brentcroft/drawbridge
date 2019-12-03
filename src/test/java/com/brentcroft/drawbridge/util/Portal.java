package com.brentcroft.drawbridge.util;

import java.util.Objects;
import java.util.function.Function;


public interface Portal<I,O>
{
    void setHandler( Function<I,O> handler );

    Function<I,O> getHandler( );

    default O exchange( I request )
    {
        if ( Objects.isNull(getHandler()))
        {
            throw new IllegalStateException( "No transport" );
        }
        else if ( State.SHUT.equals( getState() ))
        {
            throw new IllegalStateException( "Portal is shut" );
        }
        else
        {
            return getHandler().apply( request );
        }
    }


    enum State
    {
        SHUT,
        OPEN
    }

    State getState();

    void setState(State state);

    default boolean isOpen()
    {
        return State.OPEN.equals( getState());
    }

    default void open()
    {
        setState(State.OPEN);
    }

    default void close()
    {
        setState( State.SHUT );
    }
}
