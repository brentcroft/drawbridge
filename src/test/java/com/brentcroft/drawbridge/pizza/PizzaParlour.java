package com.brentcroft.drawbridge.pizza;

import com.brentcroft.drawbridge.Clerk;
import com.brentcroft.drawbridge.Supplier;
import com.brentcroft.drawbridge.mock.MockClerk;
import com.brentcroft.drawbridge.mock.MockSupplier;
import com.brentcroft.drawbridge.mock.MockSupplierGroup;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public interface PizzaParlour
{
    interface Clerks
    {
        MockClerk<String, Pizza> RED = new MockClerk<>( "red" );
        MockClerk<String, Pizza> GREEN = new MockClerk<>( "green" );
        MockClerk<String, Pizza> BLUE = new MockClerk<>( "blue" );
    }

    interface PizzaBaseSupplier
    {
        MockSupplier< String, Pizza.Base > WHITE = new MockSupplier<>( "white", r -> Pizza.Base.WHITE );
        MockSupplier< String, Pizza.Base > WHOLEMEAL = new MockSupplier<>( "wholemeal", r -> Pizza.Base.WHOLEMEAL );
        MockSupplier< String, Pizza.Base > SEEDY = new MockSupplier<>( "seedy", r -> Pizza.Base.SEEDY );

        MockSupplier< String, Pizza.Base > ANY = new MockSupplierGroup<>( "any base", WHITE, WHOLEMEAL, SEEDY );
        MockSupplier< String, Pizza.Base > ALL = new MockSupplierGroup<>( "all bases", l -> null, WHITE, WHOLEMEAL, SEEDY );

        Map< String, MockSupplier< String, Pizza.Base > > suppliers = Stream.of( WHITE, WHOLEMEAL, SEEDY )
                .collect( Collectors.toMap( MockSupplier::getName, Function.identity() ) );
    }


    interface SaladSupplier
    {
        MockSupplier< String, Pizza.Salad > CELERY = new MockSupplier<>( "celery", r -> Pizza.Salad.CELERY );
        MockSupplier< String, Pizza.Salad > CUCUMBER = new MockSupplier<>( "cucumber", r -> Pizza.Salad.CUCUMBER );
        MockSupplier< String, Pizza.Salad > LETTUCE = new MockSupplier<>( "lettuce", r -> Pizza.Salad.LETTUCE );
        MockSupplier< String, Pizza.Salad > ONION = new MockSupplier<>( "onion", r -> Pizza.Salad.ONION );
        MockSupplier< String, Pizza.Salad > OLIVES = new MockSupplier<>( "olives", r -> Pizza.Salad.OLIVES );
        MockSupplier< String, Pizza.Salad > TOMATO = new MockSupplier<>( "tomato", r -> Pizza.Salad.TOMATO );

        MockSupplier< String, Pizza.Salad > ANY = new MockSupplierGroup<>( "any one salad", CELERY, CUCUMBER, LETTUCE, ONION, OLIVES, TOMATO );
        MockSupplier< String, Pizza.Salad > ALL = new MockSupplierGroup<>( "all salads", l -> Pizza.Salad.ALL, CELERY, CUCUMBER, LETTUCE, ONION, OLIVES, TOMATO );


        Map< String, MockSupplier< String, Pizza.Salad > > suppliers = Stream.of( CELERY, CUCUMBER, LETTUCE, ONION, OLIVES, TOMATO )
                .collect( Collectors.toMap( MockSupplier::getName, Function.identity() ) );
    }

    interface CheeseSupplier
    {
        MockSupplier< String, Pizza.Cheese > MOZARELLA_01 = new MockSupplier<>( "mozarella_01", r -> Pizza.Cheese.MOZARELLA );
        MockSupplier< String, Pizza.Cheese > MOZARELLA_02 = new MockSupplier<>( "mozarella_02", r -> Pizza.Cheese.MOZARELLA );
        MockSupplier< String, Pizza.Cheese > MOZARELLA_03 = new MockSupplier<>( "mozarella_03", r -> Pizza.Cheese.MOZARELLA );
        MockSupplier< String, Pizza.Cheese > MOZARELLA = new MockSupplierGroup<>( "mozarella", MOZARELLA_01, MOZARELLA_02, MOZARELLA_03 );

        MockSupplier< String, Pizza.Cheese > GORGONZOLA = new MockSupplier<>( "gorgonzola", r -> Pizza.Cheese.GORGONZOLA );
        MockSupplier< String, Pizza.Cheese > ROCQUFORT = new MockSupplier<>( "roquefort", r -> Pizza.Cheese.ROCQUFORT );
        MockSupplier< String, Pizza.Cheese > CHEDDAR = new MockSupplier<>( "cheddar", r -> Pizza.Cheese.CHEDDAR );
        MockSupplier< String, Pizza.Cheese > BRIE = new MockSupplier<>( "brie", r -> Pizza.Cheese.BRIE );
        MockSupplier< String, Pizza.Cheese > MANCHEGO = new MockSupplier<>( "manchego", r -> Pizza.Cheese.MANCHEGO );

        MockSupplier< String, Pizza.Cheese > ANY = new MockSupplierGroup<>( "any one cheese", MOZARELLA, GORGONZOLA, ROCQUFORT, CHEDDAR, BRIE, MANCHEGO );
        MockSupplier< String, Pizza.Cheese > ALL = new MockSupplierGroup<>( "all cheeses", l -> null, MOZARELLA, GORGONZOLA, ROCQUFORT, CHEDDAR, BRIE, MANCHEGO );

        Map< String, MockSupplier< String, Pizza.Cheese > > suppliers = Stream.of( MOZARELLA, GORGONZOLA, ROCQUFORT, CHEDDAR, BRIE, MANCHEGO )
                .collect( Collectors.toMap( MockSupplier::getName, Function.identity() ) );
    }

    interface MeatSupplier
    {
        MockSupplier< String, Pizza.Meat > PEPERONI = new MockSupplier<>( "peperoni", r -> Pizza.Meat.PEPERONI );
        MockSupplier< String, Pizza.Meat > BEEF = new MockSupplier<>( "beef", r -> Pizza.Meat.BEEF );
        MockSupplier< String, Pizza.Meat > LAMB = new MockSupplier<>( "lamb", r -> Pizza.Meat.LAMB );
        MockSupplier< String, Pizza.Meat > BACON = new MockSupplier<>( "bacon", r -> Pizza.Meat.BACON );

        MockSupplierGroup< String, Pizza.Meat > ANY = new MockSupplierGroup<>( "any one meat", PEPERONI, BEEF, LAMB, BACON );
        MockSupplierGroup< String, Pizza.Meat > ALL = new MockSupplierGroup<>( "all meats", l -> Pizza.Meat.ALL, PEPERONI, BEEF, LAMB, BACON );

        Map< String, MockSupplier< String, Pizza.Meat > > suppliers = ANY
                .getSuppliers()
                .stream()
                .collect( Collectors.toMap( MockSupplier::getName, Function.identity() ) );
    }


    interface SauceSupplier
    {
        MockSupplier< String, Pizza.Sauce > BBQ = new MockSupplier<>( "bbq", r -> Pizza.Sauce.BBQ );
        MockSupplier< String, Pizza.Sauce > BROWN = new MockSupplier<>( "brown", r -> Pizza.Sauce.BROWN );
        MockSupplier< String, Pizza.Sauce > MAYO = new MockSupplier<>( "mayo", r -> Pizza.Sauce.MAYO );
        MockSupplier< String, Pizza.Sauce > TOMATO = new MockSupplier<>( "tomato", r -> Pizza.Sauce.TOMATO );

        MockSupplierGroup< String, Pizza.Sauce > ANY = new MockSupplierGroup<>( "any sauce", BBQ, BROWN, MAYO, TOMATO );
        MockSupplierGroup< String, Pizza.Sauce > ALL = new MockSupplierGroup<>( "all sauces", l -> Pizza.Sauce.ALL, BBQ, BROWN, MAYO, TOMATO );

        Map< String, MockSupplier< String, Pizza.Sauce > > suppliers = ANY
                .getSuppliers()
                .stream()
                .collect( Collectors.toMap( MockSupplier::getName, Function.identity() ) );
    }


    default Collection< Clerk > getClerks()
    {
        return Arrays.asList(
                Clerks.RED,
                Clerks.GREEN,
                Clerks.BLUE
        );
    }

    default Collection< Supplier > getSuppliers()
    {
        return Arrays.asList(
                PizzaBaseSupplier.ALL,
                SaladSupplier.ALL,
                CheeseSupplier.ALL,
                MeatSupplier.ALL,
                SauceSupplier.ALL
        );
    }
}
