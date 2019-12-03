package com.brentcroft.drawbridge;

import java.util.Collection;

import static com.brentcroft.drawbridge.Status.*;
import static java.lang.String.format;

public interface Shutter
{
    Shutter self();

    Status getStatus();

    Collection< Supplier > getSuppliers();

    Collection< Clerk > getClerks();

    Investigator getInvestigator();

    void setInvestigator( Investigator investigator );

    boolean isInvestigating();

    boolean isRecovering();

    void startSuppliers();

    void openClerks();

    void closeClerks();

    void setStatus( Status status );


    default void start()
    {
        startSuppliers();

        getInvestigator().addSuppliersToInvestigate( getSuppliers() );

        maybeChangeStateNow();
    }


    default void notifySupplierError( Supplier supplier, Throwable error )
    {
        if( getInvestigator().isSupplierErrorInvestigable( supplier, error ) )
        {
            return;
        }

        getInvestigator().addSupplierToInvestigate( supplier );

        maybeChangeStateLater();
    }


    void maybeChangeStateLater();


    default void maybeChangeStateNow()
    {
        getInvestigator().investigateNotifiedSuppliers( getSuppliers() );

        if( getInvestigator().hasFailedSuppliers( getSuppliers() ) )
        {
            enterRecovery();
        }
        else if( getInvestigator().hasFailedSuppliers() )
        {
            enterPartial();
        }
        else
        {
            enterUp();
        }
    }


    default void enterRecovery()
    {
        synchronized( self() )
        {
            if( inRecovery() )
            {
                return;
            }

            setStatus( RECOVERY );
        }

        closeClerks();

        startRecovery();
    }


    void startRecovery();


    default void enterUp()
    {
        setStatus( UP );

        openClerks();
    }


    default void enterPartial()
    {
        setStatus( PARTIAL );

        openClerks();

        startRecovery();
    }


    default boolean inRecovery()
    {
        return getStatus() == RECOVERY;
    }

    default boolean inPartial()
    {
        return getStatus() == PARTIAL;
    }


    default void checkStatus( Status status )
    {
        if( getStatus() != status )
        {
            throw new IllegalStateException( format( "Status is [%s] not [%s]", getStatus(), status ) );
        }
    }
}
