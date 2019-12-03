package com.brentcroft.drawbridge.mock;

import com.brentcroft.drawbridge.Clerk;
import com.brentcroft.drawbridge.util.Transport;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.util.function.Function;

import static java.lang.String.format;

@Getter
@Log4j
public class MockClerk< I, O > implements Clerk
{
    private final Transport< I, O > transport = new Transport<>();
    private final String name;


    public MockClerk( String name )
    {
        this.name = name;
    }

    public String toString()
    {
        return name;
    }

    @Override
    public boolean isOpen()
    {
        return transport.isOpen();
    }

    @Override
    public void open()
    {
        transport.open();

        log.info( format( "Opened clerk: %s [%s]", name, getTransport().getState() ) );
    }

    @Override
    public void close()
    {
        transport.close();

        log.info( format( "Closed clerk: %s [%s]", name, getTransport().getState() ) );
    }

    public void setHandler( Function< I, O > handler )
    {
        transport.setHandler( handler );
    }

    public O exchange( I request )
    {
        if( ! isOpen() )
        {
            throw new RuntimeException( "Not listening" );
        }

        return transport.exchange( request );
    }
}
