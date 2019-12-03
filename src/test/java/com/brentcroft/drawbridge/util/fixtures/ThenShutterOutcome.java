package com.brentcroft.drawbridge.util.fixtures;

import com.brentcroft.drawbridge.Shutter;
import com.brentcroft.drawbridge.Status;
import com.brentcroft.drawbridge.mock.MockInvestigator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import static org.junit.Assert.assertEquals;

public class ThenShutterOutcome extends Stage< ThenShutterOutcome >
{
    @ExpectedScenarioState
    Shutter shutter;

    @ExpectedScenarioState
    MockInvestigator investigator;


    public ThenShutterOutcome all_suppliers_are_available()
    {
        boolean expected = false;
        boolean actual = shutter.getInvestigator().hasFailedSuppliers();

        assertEquals( "Not all SUPPLIERS are available", expected, actual );

        return self();
    }

    public ThenShutterOutcome no_clients_are_open()
    {
        boolean expected = false;
        boolean actual = shutter
                .getClerks()
                .stream()
                .anyMatch( c -> c.isOpen() );

        assertEquals( "Some CLIENTS are open", expected, actual );

        return self();
    }


    public ThenShutterOutcome all_clients_are_open()
    {
        boolean expected = false;
        boolean actual = shutter
                .getClerks()
                .stream()
                .anyMatch( s -> ! s.isOpen() );

        assertEquals( "Not all CLIENTS are open", expected, actual );

        return self();
    }

    public ThenShutterOutcome service_status_is_$( Status status )
    {
        Status expected = status;
        Status actual = shutter.getStatus();

        assertEquals( "Unexpected service status", expected, actual );

        return self();
    }

    public ThenShutterOutcome suppliers_are_under_interrogation()
    {
        boolean expected = true;
        boolean actual = investigator.hasSuppliersToInvestigate();

        assertEquals( "No SUPPLIERS are under investigation", expected, actual );

        return self();
    }
}
