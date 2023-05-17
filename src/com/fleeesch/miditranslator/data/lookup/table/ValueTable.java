package com.fleeesch.miditranslator.data.lookup.table;

public class ValueTable implements ValueTableInterface {

    //************************************************************
    //      Variables
    //************************************************************

    public final String name;

    public int size;

    public int[] valuesInt;
    public double[] valuesDouble;

    //************************************************************
    //      Constructor
    //************************************************************

    public ValueTable(String pName) {

        name = pName;

    }

    //************************************************************
    //      Method : Generate Values
    //************************************************************

    @Override
    public void generateValues(int count, double curve, double multiplier) {

        size = count - 1;

        valuesInt = new int[count];
        valuesDouble = new double[count];


        for (int i = 0; i < count; i++) {

            valuesDouble[i] = Math.pow(i * (1.0 / (count - 1)), curve) * multiplier;

            valuesInt[i] = (int) (valuesDouble[i] * (count - 1));

        }


        // hardcode start and end values

        valuesInt[0] = 0;
        valuesInt[size] = size;

        valuesDouble[0] = 0;
        valuesDouble[size] = 1;

    }

    //************************************************************
    //      Method : Get
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Double

    @Override
    public double get(double pos) {
        return valuesDouble[(int) (pos * size)];
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Int

    @Override
    public int get(int pos) {
        return valuesInt[pos];
    }
}
