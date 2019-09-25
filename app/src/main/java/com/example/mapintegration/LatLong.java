package com.example.mapintegration;

public class LatLong {
    Double Lati;
    Double Longi;

    public LatLong(Double lati, Double longi) {
        Lati = lati;
        Longi = longi;
    }

    public Double getLati() {
        return Lati;
    }

    public void setLati(Double lati) {
        Lati = lati;
    }

    public Double getLongi() {
        return Longi;
    }

    public void setLongi(Double longi) {
        Longi = longi;
    }
}
