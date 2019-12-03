package com.brentcroft.drawbridge.mock;

import com.brentcroft.drawbridge.Status;
import com.brentcroft.drawbridge.Supplier;
import com.brentcroft.drawbridge.util.SupplierException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.brentcroft.drawbridge.Status.*;
import static com.brentcroft.drawbridge.mock.MockSupplierGroup.GroupLogic.ALL;
import static com.brentcroft.drawbridge.mock.MockSupplierGroup.GroupLogic.ANY;
import static java.lang.String.format;

@Getter
@Setter
public class MockSupplierGroup< I, O > extends MockSupplier< I, O >
{
    private Collection< MockSupplier< I, O > > suppliers;
    private GroupLogic groupLogic;
    private Function< List< O >, O > responseConcatenator;


    public enum GroupLogic
    {
        ALL,
        ANY
    }


    public MockSupplierGroup( String name, GroupLogic groupLogic, Function< List< O >, O > responseConcatenator, Collection< MockSupplier< I, O > > suppliers )
    {
        super( name, null );
        this.groupLogic = groupLogic;
        this.suppliers = suppliers;
        this.responseConcatenator = responseConcatenator;
    }

    public MockSupplierGroup( String name, MockSupplier< I, O >... suppliers )
    {
        super( name, null );
        this.groupLogic = GroupLogic.ANY;
        this.suppliers = Arrays.asList( suppliers );
        this.responseConcatenator = null;
    }

    public MockSupplierGroup( String name, Function< List< O >, O > responseConcatenator, MockSupplier< I, O >... suppliers )
    {
        super( name, null );
        this.groupLogic = ALL;
        this.suppliers = Arrays.asList( suppliers );
        this.responseConcatenator = responseConcatenator;
    }


    public String toString()
    {
        return format( "Group[%s]:%s", groupLogic, getName() );
    }

    public GroupLogic getGroupLogic()
    {
        return groupLogic;
    }


    public void start()
    {
        getSuppliers()
                .parallelStream()
                .forEach( Supplier::start );
    }

    public boolean isStarted()
    {
        return getSuppliers()
                .stream()
                .allMatch( Supplier::isStarted );
    }

    public Status getAvailability( Visitor visitor )
    {
        Set< Status > availability = getSuppliers()
                .stream()
                .map( s -> s.getAvailability( visitor ) )
                .collect( Collectors.toSet() );

        boolean hasUp = availability.contains( UP );
        boolean hasDown = availability.contains( DOWN );
        boolean hasPartial = availability.contains( PARTIAL );

        GroupLogic gl = getGroupLogic();


        Status status = ( hasUp && ! hasPartial && ! hasDown )
                        ? UP
                        : ( hasUp || hasPartial ) && ( ANY.equals( gl ) || ! hasDown )
                          ? PARTIAL
                          : ( hasDown )
                            ? DOWN
                            : UNKNOWN;


        if( Objects.nonNull( visitor ) )
        {
            visitor.visit( this, status );
        }


        return status;
    }


    public void stop()
    {
        getSuppliers()
                .parallelStream()
                .forEach( MockSupplier< I, O >::stop );
    }

    public O exchange( I request )
    {
        if( ANY.equals( groupLogic ) )
        {
            return getSuppliers()
                    .stream()
                    .findAny()
                    .orElseThrow( () -> new SupplierException( this, "No suppliers" ) )
                    .exchange( request );
        }
        else
        {
            List< O > responses = getSuppliers()
                    .stream()
                    .map( s -> s.exchange( request ) )
                    .collect( Collectors.toList() );

            return responseConcatenator.apply( responses );
        }
    }
}
