package com.brentcroft.drawbridge;

import com.brentcroft.drawbridge.util.fixtures.GivenShutterState;
import com.brentcroft.drawbridge.util.fixtures.ThenShutterOutcome;
import com.brentcroft.drawbridge.util.fixtures.WhenShutterAction;

import static com.brentcroft.drawbridge.concatenation.SimpleStrings.Suppliers.APPLE;
import static com.brentcroft.drawbridge.concatenation.SimpleStrings.Suppliers.HYDROGEN;


import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

import static com.brentcroft.drawbridge.Status.*;

public class ShutterTest extends ScenarioTest< GivenShutterState, WhenShutterAction, ThenShutterOutcome >
{
    @Test
    public void shutter_starts()
    {
        given()
                .a_simple_shutter();

        when()
                .the_service_starts()
                .recovery_is_complete();

        then()
                .service_status_is_$( UP )
                .all_suppliers_are_available()
                .all_clients_are_open();
    }

    @Test
    public void shutter_partial()
    {
        given()
                .a_simple_shutter();

        when()
                .the_service_starts()
                .the_suppliers_$_go_down( HYDROGEN )
                .client_notifies_supplier_error( HYDROGEN, new Exception("Something went wrong") )
                .investigation_is_complete();

        then()
                .service_status_is_$( PARTIAL )
                .suppliers_are_under_interrogation()
                .all_clients_are_open();
    }


    @Test
    public void shutter_recovery()
    {
        given()
                .a_simple_shutter();

        when()
                .the_service_starts()
                .the_suppliers_$_go_down( APPLE )
                .client_notifies_supplier_error( APPLE, new Exception("Something went wrong") )
                .investigation_is_complete();

        then()
                .service_status_is_$( RECOVERY )
                .suppliers_are_under_interrogation()
                .no_clients_are_open();
    }
}