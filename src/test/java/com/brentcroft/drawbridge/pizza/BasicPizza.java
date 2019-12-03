package com.brentcroft.drawbridge.pizza;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static java.lang.String.format;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicPizza implements Pizza
{
    Base base;
    Salad salad;
    Cheese cheese;
    Meat meat;
    Sauce sauce;

    public String toString()
    {
        return format( "BasicPizza: base: %s, salad: %s, cheese: %s, meat: %s, sauce: %s",
                base, salad, cheese, meat, sauce );
    }
}