package com.brentcroft.drawbridge;

import com.brentcroft.drawbridge.pizza.fixtures.GivenPizzaServiceState;
import com.brentcroft.drawbridge.pizza.fixtures.ThenPizzaServiceOutcome;
import com.brentcroft.drawbridge.pizza.fixtures.WhenPizzaServiceAction;
import com.brentcroft.drawbridge.pizza.PizzaParlour;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

import java.net.SocketException;

import static com.brentcroft.drawbridge.Status.*;

/**
 * Note that a client has to notify a supplier error to the Shutter
 * in order for a Supplier to be investigated.
 */
public class PizzaServiceTest extends ScenarioTest< GivenPizzaServiceState, WhenPizzaServiceAction, ThenPizzaServiceOutcome > implements PizzaParlour
{
    @Test
    public void service_up_recovery_up()
    {
        String order = "base: white; salad: lettuce; cheese: gorgonzola; meat: lamb; sauce: mayo";

        given()
                .a_pizza_service();

        when()
                .the_service_starts()
                .investigation_is_complete()
                .the_suppliers_$_go_down(
                        PizzaBaseSupplier.WHITE,
                        PizzaBaseSupplier.WHOLEMEAL,
                        PizzaBaseSupplier.SEEDY
                )
                .client_$_orders_pizza_$( Clerks.BLUE, order )
                .investigation_is_complete();

        then()
                .no_pizza_delivered()
                .service_status_is_$( RECOVERY )
                .suppliers_are_under_interrogation()
                .no_clerks_are_open();

        when()
                .the_suppliers_$_come_up(
                        PizzaBaseSupplier.WHITE,
                        PizzaBaseSupplier.WHOLEMEAL,
                        PizzaBaseSupplier.SEEDY
                )
                .recovery_is_complete()
                .client_$_orders_pizza_$( Clerks.BLUE, order );

        then()
                .service_status_is_$( UP )
                .all_suppliers_are_available()
                .all_clerks_are_open()
                .pizza_delivered();
    }

    @Test
    public void service_up_partial_up()
    {
        String order = "base: white; salad: lettuce; cheese: mozarella; meat: lamb; sauce: mayo";

        given()
                .a_pizza_service();
        when()
                .the_service_starts()
                .investigation_is_complete()
                .the_suppliers_$_go_down( CheeseSupplier.MOZARELLA_01 )
                .a_client_notifies_supplier_$_error( CheeseSupplier.MOZARELLA_01, new SocketException( "Trouble at mill" ) )
                .investigation_is_complete();

        then()
                .service_status_is_$( PARTIAL )
                .suppliers_are_under_interrogation()
                .all_clerks_are_open();

        when()
                .the_suppliers_$_come_up( CheeseSupplier.MOZARELLA_01 )
                .recovery_is_complete()
                .client_$_orders_pizza_$( Clerks.RED, order );

        then()
                .service_status_is_$( UP )
                .all_suppliers_are_available()
                .all_clerks_are_open()
                .pizza_delivered();
    }
}