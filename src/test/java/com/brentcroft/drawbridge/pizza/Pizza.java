package com.brentcroft.drawbridge.pizza;

public interface Pizza
{
    enum Base
    {
        WHITE,
        WHOLEMEAL,
        SEEDY
    }

    enum Salad
    {
        CELERY,
        CUCUMBER,
        LETTUCE,
        ONION,
        OLIVES,
        TOMATO,
        ALL
    }

    enum Cheese
    {
        MOZARELLA,
        GORGONZOLA,
        ROCQUFORT,
        CHEDDAR,
        BRIE,
        MANCHEGO,
        ALL
    }

    enum Meat
    {
        PEPERONI,
        BEEF,
        LAMB,
        BACON,
        ALL
    }


    enum Sauce
    {
        BBQ,
        MAYO,
        BROWN,
        TOMATO,
        ALL
    }

    Base getBase();

    Salad getSalad();

    Cheese getCheese();

    Meat getMeat();

    Sauce getSauce();
}