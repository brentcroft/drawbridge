package com.brentcroft.drawbridge.concatenation;

import com.brentcroft.drawbridge.mock.MockInvestigator;
import com.brentcroft.drawbridge.mock.MockShutter;
import lombok.Getter;

@Getter
public class StringConcatenationService extends ConcatenationService< String, String > implements SimpleStrings
{
    private final MockShutter shutter;

    public StringConcatenationService()
    {
        shutter = new MockShutter();

        shutter.setClerks( getClerks() );
        shutter.setSuppliers( getSuppliers() );

        shutter.setInvestigator( new MockInvestigator() );
    }


}
