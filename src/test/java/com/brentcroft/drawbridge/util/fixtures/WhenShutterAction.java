package com.brentcroft.drawbridge.util.fixtures;

import com.brentcroft.drawbridge.Shutter;
import com.brentcroft.drawbridge.Supplier;
import com.brentcroft.drawbridge.util.Pauser;
import com.brentcroft.drawbridge.mock.MockClerk;
import com.brentcroft.drawbridge.mock.MockSupplier;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import java.util.stream.Stream;

import static java.lang.String.format;

public class WhenShutterAction extends Stage< WhenShutterAction >
{
    @ExpectedScenarioState
    Shutter shutter;

    @ProvidedScenarioState
    Exception clientException;

    @ProvidedScenarioState
    String response;


    public WhenShutterAction the_service_starts()
    {
        shutter.start();

        return self();
    }

    public WhenShutterAction the_suppliers_$_go_down( MockSupplier< ?, ? >... suppliers )
    {
        Stream
                .of( suppliers )
                .forEach( MockSupplier::stop );

        return self();
    }


    public WhenShutterAction the_suppliers_$_come_up( Supplier... suppliers )
    {
        Stream
                .of( suppliers )
                .forEach( Supplier::start );

        return self();
    }

    public WhenShutterAction client_notifies_supplier_error( Supplier supplier, Exception exception )
    {
        shutter.notifySupplierError( supplier, exception );

        return self();
    }

    public WhenShutterAction client_$_requests_$( MockClerk< String, String > clerk, String order )
    {
        try
        {
            response = clerk.exchange( format( "clerk: %s, %s", clerk, order ) );
        } catch( RuntimeException e )
        {
            clientException = e;

            throw e;
        }

        return self();
    }


    public WhenShutterAction investigation_is_complete()
    {
        final long maxWaitMillis = 10 * 1000;

        final long startMillis = System.currentTimeMillis();

        while( shutter.isInvestigating() )
        {
            if( System.currentTimeMillis() > startMillis + maxWaitMillis )
            {
                throw new IllegalStateException( format( "Timed out waiting for investigation to complete: " ) );
            }

            Pauser.pauseMillis( 100 );
        }

        return self();
    }


    public WhenShutterAction recovery_is_complete()
    {
        final long maxWaitMillis = 10 * 1000;

        final long startMillis = System.currentTimeMillis();

        while( shutter.isRecovering() )
        {
            if( System.currentTimeMillis() > startMillis + maxWaitMillis )
            {
                throw new IllegalStateException( format( "Timed out waiting for recovery to complete: " ) );
            }

            Pauser.pauseMillis( 100 );
        }

        return self();
    }
}
