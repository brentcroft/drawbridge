package com.brentcroft.drawbridge.pizza.fixtures;

import com.brentcroft.drawbridge.Service;
import com.brentcroft.drawbridge.pizza.PizzaParlour;
import com.brentcroft.drawbridge.pizza.PizzaService;
import com.brentcroft.drawbridge.mock.*;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;


public class GivenPizzaServiceState extends Stage< GivenPizzaServiceState > implements PizzaParlour
{
    @ProvidedScenarioState
    Service service;

    @ProvidedScenarioState
    MockInvestigator investigator;

    public GivenPizzaServiceState a_pizza_service()
    {
        service = new PizzaService();
        investigator = new MockInvestigator();

        service.getShutter().setInvestigator( investigator );

        return self();
    }
}
