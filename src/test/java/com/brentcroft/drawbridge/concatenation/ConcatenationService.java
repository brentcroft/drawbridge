package com.brentcroft.drawbridge.concatenation;

import com.brentcroft.drawbridge.Service;
import com.brentcroft.drawbridge.mock.MockClerk;
import com.brentcroft.drawbridge.mock.MockShutter;
import com.brentcroft.drawbridge.mock.MockSupplier;
import com.brentcroft.drawbridge.util.SupplierException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


@Getter
@Setter
public class ConcatenationService< I, O > implements Service
{
    private final MockShutter shutter = new MockShutter();

    private BiFunction< I, Throwable, O > errorHandler;
    private Function< List< O >, O > responseConcatenator;

    public void start()
    {
        getShutter()
                .getClerks()
                .forEach( c -> ( ( MockClerk< I, O > ) c ).setHandler( this::businessLogic ) );


        getShutter().start();
    }


    public O businessLogic( I request )
    {
        try
        {
            return responseConcatenator.apply(
                    getShutter()
                            .getSuppliers()
                            .stream()
                            .map( s -> ( ( MockSupplier< I, O > ) s ).exchange( request ) )
                            .collect( Collectors.toList() )
            );

        } catch( SupplierException e )
        {
            getShutter().notifySupplierError( e.getSupplier(), e.getCause() );

            return errorHandler.apply( request, e.getCause() );
        }
    }
}
