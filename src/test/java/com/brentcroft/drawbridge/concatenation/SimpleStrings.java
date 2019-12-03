package com.brentcroft.drawbridge.concatenation;

import com.brentcroft.drawbridge.Clerk;
import com.brentcroft.drawbridge.Supplier;
import com.brentcroft.drawbridge.mock.MockClerk;
import com.brentcroft.drawbridge.mock.MockSupplier;
import com.brentcroft.drawbridge.mock.MockSupplierGroup;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.brentcroft.drawbridge.concatenation.SimpleStrings.Clerks.*;
import static com.brentcroft.drawbridge.concatenation.SimpleStrings.Suppliers.*;

public interface SimpleStrings
{
    interface Clerks
    {
        MockClerk<String, String> RED = new MockClerk<>( "Red" );
        MockClerk<String, String> GREEN = new MockClerk<>( "Green" );
        MockClerk<String, String> BLUE = new MockClerk<>( "Blue" );
    }

    interface Suppliers
    {
        MockSupplier< String, String > APPLE = new MockSupplier<>( "Apple", r -> "apple" );
        MockSupplier< String, String > BEECH = new MockSupplier<>( "Beech", r -> "beech" );
        MockSupplier< String, String > CONDA = new MockSupplier<>( "Conda", r -> "conda" );

        MockSupplier< String, String > PETROL = new MockSupplier<>( "Petrol", r -> "petrol" );
        MockSupplier< String, String > DIESEL = new MockSupplier<>( "Diesel", r -> "diesel" );
        MockSupplier< String, String > HYDROGEN = new MockSupplier<>( "Hydrogen", r -> "hydrogen" );

        MockSupplier< String, String > WOOD = new MockSupplierGroup<>(
                "wood",
                l -> l.stream().collect( Collectors.joining( "\n" ) ),
                APPLE, BEECH, CONDA 
        );

        MockSupplier< String, String > FUEL = new MockSupplierGroup< >( "fuel", PETROL, DIESEL, HYDROGEN );
    }

    default Collection< Clerk > getClerks()
    {
        return Arrays.asList( RED, GREEN, BLUE );
    }

    default Collection< Supplier > getSuppliers()
    {
        return Arrays.asList( WOOD, FUEL );
    }
}
