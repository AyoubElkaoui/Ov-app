package com.example.eerstejavafx;

public class FavoriteRoute {
    private String vertrek;
    private String aankomst;
    private String datum;
    private String tijd;

    public FavoriteRoute(String vertrek, String aankomst, String selectedDate, String selectedTime) {
    }

    public FavoriteRoute() {
    }

    @Override
    public String toString() {
        return "Route: " + vertrek + " naar " + aankomst;
    }

    public String getVertrek() {
        return vertrek;
    }

    public void setVertrek(String vertrek) {
        this.vertrek = vertrek;
    }

    public String getAankomst() {
        return aankomst;
    }

    public void setAankomst(String aankomst) {
        this.aankomst = aankomst;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getTijd() {
        return tijd;
    }

    public void setTijd(String tijd) {
        this.tijd = tijd;
    }
}
