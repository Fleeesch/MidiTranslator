package com.fleeesch.miditranslator.functions.math;

public class Convert {

    //************************************************************
    //      Method : Convert to Byte Array
    //************************************************************

    public static byte[] toByteArray(int... pInt) {

        byte[] byteArray = new byte[pInt.length]; // declare byte array

        int c = 0; // data counter
        for (int data : pInt) byteArray[c++] = (byte) data; // copy int to byte one by one

        return byteArray; // return byte array

    }


}
