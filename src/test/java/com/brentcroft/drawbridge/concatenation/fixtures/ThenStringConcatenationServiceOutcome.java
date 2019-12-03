package com.brentcroft.drawbridge.concatenation.fixtures;

import com.brentcroft.drawbridge.Service;
import com.brentcroft.drawbridge.Status;
import com.brentcroft.drawbridge.mock.MockInvestigator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import static org.junit.Assert.assertEquals;

public class ThenStringConcatenationServiceOutcome extends Stage< ThenStringConcatenationServiceOutcome >
{
    @ExpectedScenarioState
    Service service;

    @ExpectedScenarioState
    MockInvestigator investigator;

    public ThenStringConcatenationServiceOutcome all_suppliers_are_available()
    {
        boolean expected = false;
        boolean actual = service.getShutter().getInvestigator().hasFailedSuppliers();

        assertEquals( "Not all SUPPLIERS are available", expected, actual );

        return self();
    }

    public ThenStringConcatenationServiceOutcome no_clients_are_open()
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

    public ThenStringConcatenationServiceOutcome some_clients_are_open()
    {
        boolean expected = true;
        boolean actual = service
                .getShutter()
                .getClerks()
                .stream()
                .anyMatch( c -> c.isOpen() );

        assertEquals( "No CLIENTS are open", expected, actual );

        return self();
    }


    public ThenStringConcatenationServiceOutcome all_clients_are_open()
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

    public ThenStringConcatenationServiceOutcome service_status_is_$( Status status )
    {
        Status expected = status;
        Status actual = service.getShutter().getStatus();

        assertEquals( "Unexpected service status", expected, actual );

        return self();
    }

    public ThenStringConcatenationServiceOutcome suppliers_are_under_interrogation()
    {
        boolean expected = true;
        boolean actual = investigator.hasSuppliersToInvestigate();

        assertEquals( "No SUPPLIERS are under investigation", expected, actual );

        return self();
    }
}
