package com.brentcroft.drawbridge.pizza.fixtures;

import com.brentcroft.drawbridge.Service;
import com.brentcroft.drawbridge.Status;
import com.brentcroft.drawbridge.pizza.ErrorPizza;
import com.brentcroft.drawbridge.pizza.Pizza;
import com.brentcroft.drawbridge.mock.MockInvestigator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import lombok.extern.log4j.Log4j;

import java.util.Objects;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

@Log4j
public class ThenPizzaServiceOutcome extends Stage< ThenPizzaServiceOutcome >
{
    @ExpectedScenarioState
    Service service;

    @ExpectedScenarioState
    MockInvestigator investigator;


    @ExpectedScenarioState
    Pizza pizza;


    public ThenPizzaServiceOutcome all_suppliers_are_available()
    {
        boolean expected = false;
        boolean actual = service.getShutter().getInvestigator().hasFailedSuppliers();

        assertEquals( "Not all SUPPLIERS are available", expected, actual );

        return self();
    }

    public ThenPizzaServiceOutcome no_clerks_are_open()
    {
        boolean expected = false;
        boolean actual = service
                .getShutter()
                .getClerks()
                .stream()
                .anyMatch( c -> c.isOpen() );

        assertEquals( "Some CLIENTS are open", expected, actual );

        return self();
    }


    public ThenPizzaServiceOutcome all_clerks_are_open()
    {
        boolean expected = false;
        boolean actual = service
                .getShutter()
                .getClerks()
                .stream()
                .anyMatch( s -> ! s.isOpen() );

        assertEquals( "Not all CLIENTS are open", expected, actual );

        return self();
    }

    public ThenPizzaServiceOutcome service_status_is_$( Status status )
    {
        Status expected = status;
        Status actual = service.getShutter().getStatus();

        assertEquals( "Unexpected service status", expected, actual );

        return self();
    }

    public ThenPizzaServiceOutcome suppliers_are_under_interrogation()
    {
        boolean expected = true;
        boolean actual = investigator.hasSuppliersToInvestigate();

        assertEquals( "No SUPPLIERS are under investigation", expected, actual );

        return self();
    }

    public ThenPizzaServiceOutcome no_pizza_delivered()
    {
        boolean expected = true;
        boolean actual = Objects.nonNull( pizza ) && pizza instanceof ErrorPizza;

        assertEquals( "Pizza was delivered", expected, actual );

        log.info( format("Response: %s", pizza ) );

        return self();
    }


    public ThenPizzaServiceOutcome pizza_delivered()
    {
        boolean expected = true;
        boolean actual = Objects.nonNull( pizza ) && ! ( pizza instanceof ErrorPizza );

        assertEquals( "Pizza is an error", expected, actual );

        log.info( format("Response: %s", pizza ) );

        return self();
    }
}
