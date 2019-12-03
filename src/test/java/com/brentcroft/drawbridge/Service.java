package com.brentcroft.drawbridge;

public interface Service< I, O >
{
    Shutter getShutter();

    void start();

}
