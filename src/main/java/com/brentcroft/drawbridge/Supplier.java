package com.brentcroft.drawbridge;


public interface Supplier
{
    String getId();

    void start();

    boolean isStarted();

    Status getAvailability( Visitor visitor );

    interface Visitor
    {
        void visit( Supplier supplier, Status status );

        default boolean assumeUp( Supplier supplier )
        {
            return false;
        }
    }
}
