package com.brentcroft.drawbridge.pizza;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static java.lang.String.format;

@Getter
@Setter
@AllArgsConstructor
public class ErrorPizza extends BasicPizza
{
    private Throwable error;

    public String toString()
    {
        return format("ErrorPizza: unfortunately your pizza could not be made right now; %s", error );
    }
}