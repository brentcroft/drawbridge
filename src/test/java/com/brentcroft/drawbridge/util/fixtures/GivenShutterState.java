package com.brentcroft.drawbridge.util.fixtures;

import com.brentcroft.drawbridge.Shutter;
import com.brentcroft.drawbridge.concatenation.SimpleStrings;
import com.brentcroft.drawbridge.mock.MockInvestigator;
import com.brentcroft.drawbridge.mock.MockShutter;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class GivenShutterState extends Stage< GivenShutterState >
{
    @ProvidedScenarioState
    Shutter shutter;

    @ProvidedScenarioState
    MockInvestigator investigator;


    public GivenShutterState a_simple_shutter()
    {
        shutter = new MockShutter();
        investigator = new MockInvestigator();

        shutter.setInvestigator( investigator );


        SimpleStrings model = new SimpleStrings()
        {
        };

        ( ( MockShutter ) shutter ).setClerks( model.getClerks() );
        ( ( MockShutter ) shutter ).setSuppliers( model.getSuppliers() );

        return self();
    }
}
