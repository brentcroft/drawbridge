package com.brentcroft.drawbridge.concatenation.fixtures;

import com.brentcroft.drawbridge.Service;
import com.brentcroft.drawbridge.util.Pauser;
import com.brentcroft.drawbridge.mock.MockClerk;
import com.brentcroft.drawbridge.Supplier;
import com.brentcroft.drawbridge.mock.MockSupplier;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import static java.lang.String.format;

public class WhenStringConcatenationServiceAction extends Stage< WhenStringConcatenationServiceAction >
{
    @ExpectedScenarioState
    Service service;

    @ProvidedScenarioState
    Exception clientException;

    @ProvidedScenarioState
    String supplierResponse;

    public WhenStringConcatenationServiceAction the_service_starts()
    {
        service.start();

        return self();
    }

    public WhenStringConcatenationServiceAction the_supplier_$_goes_down( MockSupplier< ?, ? > supplier )
    {
        supplier.stop();

        return self();
    }

    public WhenStringConcatenationServiceAction the_supplier_$_comes_up( MockSupplier< ?, ? > supplier )
    {
        supplier.start();

        return self();
    }


    public WhenStringConcatenationServiceAction a_client_notifies_supplier_$_error( Supplier supplier, Exception exception )
    {
        service.getShutter().notifySupplierError(supplier, exception);

        return self();
    }


    public WhenStringConcatenationServiceAction investigation_is_complete()
    {
        final long maxWaitMillis = 10 * 1000;

        final long startMillis = System.currentTimeMillis();

        while( service.getShutter().isInvestigating() )
        {
            if( System.currentTimeMillis() > startMillis + maxWaitMillis )
            {
                throw new IllegalStateException( format( "Timed out waiting for investigation to complete: " ) );
            }

            Pauser.pauseMillis( 100 );
        }

        return self();
    }



    public WhenStringConcatenationServiceAction recovery_is_complete()
    {
        final long maxWaitMillis = 10 * 1000;

        final long startMillis = System.currentTimeMillis();

        while( service.getShutter().isRecovering() )
        {
            if( System.currentTimeMillis() > startMillis + maxWaitMillis )
            {
                throw new IllegalStateException( format( "Timed out waiting for recovery to complete: " ) );
            }

            Pauser.pauseMillis( 100 );
        }

        return self();
    }



    public WhenStringConcatenationServiceAction the_client_$_requests_$( MockClerk< String, String > clerk, String request )
    {
        try
        {
            supplierResponse = clerk.exchange( format( "clerk: %s, %s", clerk, request ) );
        }
        catch( RuntimeException e )
        {
            clientException = e;

            throw e;
        }

        return self();
    }
}
