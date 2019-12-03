package com.brentcroft.drawbridge;

import java.util.Collection;

public interface Investigator
{
    void addSupplierToInvestigate( Supplier supplier );

    void addSuppliersToInvestigate( Collection< Supplier > suppliers );

    boolean isSupplierErrorInvestigable( Supplier supplier, Throwable error );


    boolean hasFailedSuppliers();

    boolean hasFailedSuppliers( Collection< Supplier > rootSuppliers );

    void investigateNotifiedSuppliers( Collection< Supplier > rootSuppliers );
}
