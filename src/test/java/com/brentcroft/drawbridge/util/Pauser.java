package com.brentcroft.drawbridge.util;

import java.util.concurrent.TimeUnit;

public class Pauser
{
    public static void pauseMillis(long millis)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep( millis );
        }
        catch (InterruptedException ie)
        {
            Thread.currentThread().interrupt();
        }
    }
}
