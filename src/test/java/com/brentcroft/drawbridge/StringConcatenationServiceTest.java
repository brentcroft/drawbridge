package com.brentcroft.drawbridge;

import com.brentcroft.drawbridge.concatenation.fixtures.GivenStringConcatenationServiceState;
import com.brentcroft.drawbridge.concatenation.fixtures.ThenStringConcatenationServiceOutcome;
import com.brentcroft.drawbridge.concatenation.fixtures.WhenStringConcatenationServiceAction;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

import static com.brentcroft.drawbridge.concatenation.SimpleStrings.Suppliers.APPLE;
import static com.brentcroft.drawbridge.concatenation.SimpleStrings.Suppliers.HYDROGEN;
import static com.brentcroft.drawbridge.Status.PARTIAL;
import static com.brentcroft.drawbridge.Status.RECOVERY;
import static com.brentcroft.drawbridge.Status.UP;


public class StringConcatenationServiceTest extends ScenarioTest< GivenStringConcatenationServiceState, WhenStringConcatenationServiceAction, ThenStringConcatenationServiceOutcome >
{

    @Test
    public void service_starts()
    {
        given()
                .a_simple_service();

        when()
                .the_service_starts()
                .investigation_is_complete();

        then()
                .service_status_is_$( UP )
                .all_suppliers_are_available()
                .all_clients_are_open();
    }


    @Test
    public void service_recovery()
    {
        given()
                .a_simple_service();

        when()
                .the_service_starts()
                .investigation_is_complete()
                .the_supplier_$_goes_down( APPLE )
                .a_client_notifies_supplier_$_error( APPLE, new Exception("Something went wrong") )
                .investigation_is_complete();

        then()
                .service_status_is_$( RECOVERY )
                .suppliers_are_under_interrogation()
                .no_clients_are_open();

        when()
                .the_supplier_$_comes_up( APPLE )
                .recovery_is_complete();

        then()
                .service_status_is_$( UP )
                .all_suppliers_are_available()
                .all_clients_are_open();
    }


    @Test
    public void service_partial()
    {
        given()
                .a_simple_service();

        when()
                .the_service_starts()
                .investigation_is_complete()
                .the_supplier_$_goes_down( HYDROGEN )
                .a_client_notifies_supplier_$_error( HYDROGEN, new Exception("Something went wrong") )
                .investigation_is_complete();

        then()
                .service_status_is_$( PARTIAL )
                .suppliers_are_under_interrogation()
                .some_clients_are_open();

        when()
                .the_supplier_$_comes_up( HYDROGEN )
                .recovery_is_complete();

        then()
                .service_status_is_$( UP )
                .all_suppliers_are_available()
                .all_clients_are_open();
    }    
}