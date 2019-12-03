package com.brentcroft.drawbridge.mock;

import com.brentcroft.drawbridge.Status;
import com.brentcroft.drawbridge.Supplier;
import com.brentcroft.drawbridge.util.Pauser;
import com.brentcroft.drawbridge.util.Portal;
import com.brentcroft.drawbridge.util.SupplierException;
import com.brentcroft.drawbridge.util.Transport;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.util.Objects;
import java.util.function.Function;

import static com.brentcroft.drawbridge.Status.DOWN;
import static com.brentcroft.drawbridge.Status.UP;
import static java.lang.String.format;


@Getter
@Log4j
public class MockSupplier< I, O > implements Supplier, Comparable<Supplier>
{
    private final Transport< I, O > transport = new Transport<>();
    private Function< I, O > handler;

    private final String name;

    public MockSupplier( String name, Function< I, O > handler )
    {
        this.name = name;
        setHandler( handler );
    }

    public String getId()
    {
        return name;
    }


    public String toString()
    {
        return name;
    }

    @Override
    public void start()
    {
        transport.setHandler( handler );
        transport.open();

        log.debug( format( "Started supplier: %s [%s]", name, getTransport().getState() ) );
    }

    @Override
    public boolean isStarted()
    {
        return transport.isOpen();
    }

    @Override
    public Status getAvailability(  Visitor visitor )
    {
        if (Objects.nonNull( visitor ) && visitor.assumeUp( this ) )
        {
            return Status.UP;
        }

        Pauser.pauseMillis( 50 );

        Status status = Portal.State.OPEN.equals( transport.getState() ) ?
                        UP :
                        DOWN;

        if ( Objects.nonNull(visitor) )
        {
            visitor.visit( this, status );
        }

        return status;
    }


    public void stop()
    {
        transport.close();
        transport.setHandler( null );

        log.debug( format( "Stopped supplier: %s [%s]", name, getTransport().getState() ) );
    }

    public void setHandler( Function< I, O > handler )
    {
        this.handler = handler;

        transport.setHandler( handler );
    }


    public O exchange( I request )
    {
        try
        {
            return transport.exchange( request );
        } catch( Exception e )
        {
            throw new SupplierException( this, e );
        }
    }

    @Override
    public int compareTo( Supplier o )
    {
        return getId().compareTo( o.getId() );
    }
}
