package com.brentcroft.drawbridge.pizza.fixtures;

import com.brentcroft.drawbridge.Service;
import com.brentcroft.drawbridge.Supplier;
import com.brentcroft.drawbridge.pizza.Pizza;
import com.brentcroft.drawbridge.util.Pauser;
import com.brentcroft.drawbridge.mock.MockClerk;
import com.brentcroft.drawbridge.mock.MockSupplier;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import java.util.stream.Stream;

import static java.lang.String.format;

public class WhenPizzaServiceAction extends Stage< WhenPizzaServiceAction >
{
    @ExpectedScenarioState
    Service service;

    @ProvidedScenarioState
    Exception clientException;

    @ProvidedScenarioState
    Pizza pizza;


    public WhenPizzaServiceAction the_service_starts()
    {
        service.start();

        return self();
    }

    public WhenPizzaServiceAction the_suppliers_$_go_down( MockSupplier< ?, ? >... suppliers )
    {
        Stream
            .of( suppliers )
            .forEach( MockSupplier::stop );

        return self();
    }


    public WhenPizzaServiceAction the_suppliers_$_come_up( Supplier... suppliers )
    {
        Stream
            .of( suppliers )
            .forEach( Supplier::start );

        return self();
    }

    public WhenPizzaServiceAction a_client_notifies_supplier_$_error( Supplier supplier, Exception exception )
    {
        service.getShutter().notifySupplierError(supplier, exception);

        return self();
    }    


    public WhenPizzaServiceAction investigation_is_complete()
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


    public WhenPizzaServiceAction recovery_is_complete()
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



    public WhenPizzaServiceAction client_$_orders_pizza_$( MockClerk< String, Pizza > clerk, String order )
    {
        try
        {
            pizza = clerk.exchange( format( "clerk: %s, %s", clerk, order ) );
        }
        catch( RuntimeException e )
        {
            clientException = e;

            throw e;
        }

        return self();
    }
}
