package com.brentcroft.drawbridge.util;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
@Setter
public class Transport< I, O > implements Portal< I, O >
{
    private Function< I, O > handler = null;
    private State state = State.SHUT;
}
