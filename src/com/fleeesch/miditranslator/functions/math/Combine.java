package com.fleeesch.miditranslator.functions.math;

public class Combine {

    //************************************************************
    //      Method : As Byte Array
    //************************************************************

    public static byte[] asByteArray(byte[] pPart1, byte[] pPart2) {

        byte[] byteArray = new byte[pPart1.length + pPart2.length]; // declare byte array

        // copy both arrays into new array
        System.arraycopy(pPart1, 0, byteArray, 0, pPart1.length);
        System.arraycopy(pPart2, 0, byteArray, pPart1.length, pPart2.length);

        return byteArray; // return combined array

    }


}
