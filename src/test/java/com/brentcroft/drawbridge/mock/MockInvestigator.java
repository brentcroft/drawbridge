package com.brentcroft.drawbridge.mock;

import com.brentcroft.drawbridge.Investigator;
import com.brentcroft.drawbridge.Status;
import com.brentcroft.drawbridge.Supplier;
import lombok.extern.log4j.Log4j;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Log4j
public class MockInvestigator implements Investigator
{
    private final Set< Supplier > suppliersNotifiedErrors = new ConcurrentSkipListSet<>();
    private final Set< Supplier > suppliersPartialAvailability = new ConcurrentSkipListSet<>();
    private final Set< Supplier > suppliersFailedAvailability = new ConcurrentSkipListSet<>();


    @Override
    public boolean isSupplierErrorInvestigable( Supplier supplier, Throwable error )
    {
        if( ! Exception.class.isInstance( error ) )
        {
            return false;
        }

        return suppliersNotifiedErrors.contains( supplier );
    }


    @Override
    public void addSuppliersToInvestigate( Collection< Supplier > suppliers )
    {
        suppliersNotifiedErrors.addAll( suppliers );
    }


    @Override
    public void addSupplierToInvestigate( Supplier supplier )
    {
        suppliersNotifiedErrors.add( supplier );
    }


    public boolean hasSuppliersToInvestigate()
    {
        return suppliersNotifiedErrors.size() > 0;
    }


    @Override
    public boolean hasFailedSuppliers()
    {
        return suppliersPartialAvailability.size() > 0 || suppliersFailedAvailability.size() > 0;
    }


    @Override
    public boolean hasFailedSuppliers( Collection< Supplier > suppliers )
    {
        return suppliers
                .stream()
                .anyMatch( suppliersFailedAvailability::contains );
    }


    class Visitor implements Supplier.Visitor
    {
        private final List< String > lines = new ArrayList<>();

        @Override
        public void visit( Supplier supplier, Status status )
        {
            lines.add( format( "%s [%s]", supplier.getId(), status ) );
        }

        @Override
        public boolean assumeUp( Supplier supplier )
        {
            return ! suppliersNotifiedErrors.contains( supplier );
        }

        public String toString()
        {
            return lines.stream().collect( Collectors.joining( "\n" ) );
        }
    }



    @Override
    public void investigateNotifiedSuppliers(Collection< Supplier > rootSuppliers)
    {
        suppliersFailedAvailability.clear();

        final Set< Supplier > availableSuppliers = new HashSet<>();

        Visitor visitor = new Visitor();

        log.debug( format( "Investigating %d suppliers", suppliersNotifiedErrors.size() ) );

        rootSuppliers
                .forEach( s -> {
                    Status availability = s.getAvailability( visitor );

                    switch( availability )
                    {
                        case UP:
                            availableSuppliers.add( s );
                            suppliersPartialAvailability.remove( s );
                            suppliersFailedAvailability.remove( s );
                            break;

                        case PARTIAL:
                            suppliersPartialAvailability.add( s );
                            suppliersFailedAvailability.remove( s );
                            break;

                        case DOWN:
                        case RECOVERY:
                        case UNKNOWN:
                            suppliersFailedAvailability.add( s );
                            break;

                        default:
                            throw new IllegalStateException( format( "Supplier [%s]  has unexpected availability status: %s", s, availability ) );
                    }
                } );

        suppliersNotifiedErrors.removeAll( availableSuppliers );


        log.debug( format(
                "Root Suppliers: available=%d, partial=%d, failed=%d %n%s",
                availableSuppliers.size(),
                suppliersPartialAvailability.size(),
                suppliersFailedAvailability.size(),
                visitor
        ) );
    }
}
