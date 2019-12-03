package com.brentcroft.drawbridge.util;

import com.brentcroft.drawbridge.Supplier;
import lombok.Getter;

@Getter
public class SupplierException extends RuntimeException
{
    private static final long serialVersionUID = -1749408459610763783L;
    
    private final Supplier supplier;

    public < I, O > SupplierException( Supplier supplier, String m, Exception e )
    {
        super( m, e );
        this.supplier = supplier;
    }

    public < I, O > SupplierException( Supplier supplier, String m )
    {
        super( m );
        this.supplier = supplier;
    }

    public < I, O > SupplierException( Supplier supplier, Exception e )
    {
        super( e );
        this.supplier = supplier;
    }
}
