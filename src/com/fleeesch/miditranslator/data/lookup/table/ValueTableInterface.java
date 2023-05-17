package com.fleeesch.miditranslator.data.lookup.table;

public interface ValueTableInterface {

    void generateValues(int count, double curve, double multiplier);

    double get(double pos);

    int get(int pos);


}
