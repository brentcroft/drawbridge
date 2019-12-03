package com.brentcroft.drawbridge.pizza;

import com.brentcroft.drawbridge.Service;
import com.brentcroft.drawbridge.chain.Chain;
import com.brentcroft.drawbridge.mock.MockClerk;
import com.brentcroft.drawbridge.mock.MockInvestigator;
import com.brentcroft.drawbridge.mock.MockShutter;
import com.brentcroft.drawbridge.util.SupplierException;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class PizzaService implements Service, PizzaParlour
{
    private final MockShutter shutter;

    public PizzaService()
    {
        shutter = new MockShutter();

        shutter.setClerks( getClerks() );
        shutter.setSuppliers( getSuppliers() );

        shutter.setInvestigator( new MockInvestigator() );
    }


    public void start()
    {
        getShutter()
                .getClerks()
                .forEach( c -> ( ( MockClerk< String, Pizza > ) c ).setHandler( this::businessLogic ) );

        getShutter().start();
    }


    private Pizza businessLogic( String order )
    {
        try
        {
            Map< String, String > parts = Stream
                    .of( order.split( "\\s*;\\s*" ) )
                    .map( line -> line.split( "\\s*[=\\:]\\s*" ) )
                    .filter( p -> p.length > 1 )
                    .collect( Collectors.toMap( p -> p[ 0 ], p -> p[ 1 ] ) );


            return Chain
                    .of( BasicPizza.class )
                    .firstDo( p -> p.setBase(
                            PizzaBaseSupplier
                                    .suppliers
                                    .getOrDefault( parts.get( "base" ), PizzaBaseSupplier.WHOLEMEAL )
                                    .exchange( "" )
                    ) )
                    .andThen( p -> p.setSalad(
                            SaladSupplier
                                    .suppliers
                                    .getOrDefault( parts.get( "salad" ), SaladSupplier.LETTUCE )
                                    .exchange( "" )
                    ) )
                    .andThen( p -> p.setCheese(
                            CheeseSupplier
                                    .suppliers
                                    .getOrDefault( parts.get( "cheese" ), CheeseSupplier.MANCHEGO )
                                    .exchange( "" )
                    ) )
                    .andThen( p -> p.setMeat(
                            MeatSupplier
                                    .suppliers
                                    .getOrDefault( parts.get( "meat" ), MeatSupplier.PEPERONI )
                                    .exchange( "" )
                    ) )
                    .andThen( p -> p.setSauce(
                            SauceSupplier
                                    .suppliers
                                    .getOrDefault( parts.get( "sauce" ), SauceSupplier.MAYO )
                                    .exchange( "" )
                    ) )
                    .build()
                    .executeUsing( new BasicPizza() );

        } catch( SupplierException e )
        {
            getShutter()
                    .notifySupplierError(
                            e.getSupplier(),
                            e.getCause()
                    );

            return new ErrorPizza( e.getCause() );
        }
    }
}
