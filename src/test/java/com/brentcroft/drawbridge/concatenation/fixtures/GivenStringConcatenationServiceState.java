package com.brentcroft.drawbridge.concatenation.fixtures;

import com.brentcroft.drawbridge.Service;
import com.brentcroft.drawbridge.concatenation.ConcatenationService;
import com.brentcroft.drawbridge.concatenation.StringConcatenationService;
import com.brentcroft.drawbridge.mock.MockInvestigator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import java.util.stream.Collectors;

import static java.lang.String.format;

public class GivenStringConcatenationServiceState extends Stage< GivenStringConcatenationServiceState >
{
    @ProvidedScenarioState
    Service service;

    @ProvidedScenarioState
    MockInvestigator investigator;

    public GivenStringConcatenationServiceState a_simple_service()
    {
        service = new StringConcatenationService();

        investigator = new MockInvestigator();

        service.getShutter().setInvestigator( investigator );

        ( ( ConcatenationService< String, String > ) service ).setResponseConcatenator(
                l -> l.stream().collect( Collectors.joining( "\n" ) )
        );

        ( ( ConcatenationService< String, String > ) service ).setErrorHandler(
                ( request, error ) -> format( "An error occurred processing request: %s; %s", request, error )
        );

        return self();
    }
}
