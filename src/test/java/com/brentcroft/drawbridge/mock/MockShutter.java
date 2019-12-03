package com.brentcroft.drawbridge.mock;

import com.brentcroft.drawbridge.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Getter
@Setter
@Log4j
public class MockShutter implements Shutter
{
    private long recoveryPollingIntervalMillis = 1000;

    private ExecutorService checkExecutor = null;
    private ExecutorService recoveryExecutor = null;

    private Status status = Status.UNKNOWN;
    private Collection< Clerk > clerks;
    private Collection< Supplier > suppliers;
    private Investigator investigator;


    public final Shutter self()
    {
        return this;
    }


    public void startSuppliers()
    {
        getSuppliers()
                .parallelStream()
                .filter( s -> ! s.isStarted() )
                .forEach( Supplier::start );
    }

    public void openClerks()
    {
        clerks
                .parallelStream()
                .filter( c -> ! c.isOpen() )
                .forEach( Clerk::open );
    }

    public void closeClerks()
    {
        clerks
                .parallelStream()
                .filter( Clerk::isOpen )
                .forEach( Clerk::close );
    }


    public boolean isInvestigating()
    {
        return Objects.nonNull( checkExecutor );
    }

    public boolean isRecovering()
    {
        return Objects.nonNull( recoveryExecutor );
    }


    public void maybeChangeStateLater()
    {
        synchronized( this )
        {
            if( checkExecutor != null )
            {
                return;
            }

            checkExecutor = Executors.newSingleThreadExecutor();
        }

        log.info( format( "Created check thread: status=%s", getStatus() ) );

        checkExecutor.submit( () -> {

            maybeChangeStateNow();

            checkExecutor = null;

            log.info( format( "Destroyed check thread: status=%s", getStatus() ) );
        } );

        checkExecutor.shutdown();
    }

    @Override
    public void startRecovery()
    {
        synchronized( this )
        {
            if( recoveryExecutor != null )
            {
                return;
            }

            recoveryExecutor = Executors.newSingleThreadExecutor();
        }

        log.info( format( "Created recovery thread: status=%s", getStatus() ) );

        recoveryExecutor.submit( () -> {

            while( inRecovery() || inPartial() )
            {
                try
                {
                    TimeUnit.MILLISECONDS.sleep( recoveryPollingIntervalMillis );
                } catch( InterruptedException ie )
                {
                    Thread.currentThread().interrupt();
                }

                maybeChangeStateNow();
            }

            recoveryExecutor = null;

            log.info( format( "Destroyed recovery thread: status=%s", getStatus() ) );
        } );

        recoveryExecutor.shutdown();
    }
}
