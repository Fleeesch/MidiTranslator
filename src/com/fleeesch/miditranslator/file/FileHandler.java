package com.fleeesch.miditranslator.file;

import com.fleeesch.miditranslator.event.EventSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public abstract class FileHandler extends EventSource {

    //************************************************************
    //      Variables
    //************************************************************

    public final File file; // target file
    public final String name; // no for handler

    public final Properties properties; // (to be) stored data

    //************************************************************
    //      Constructor
    //************************************************************

    public FileHandler(String pName, String pFileName) {

        name = pName; // store name

        file = new File(pFileName); // store file address

        properties = new Properties(); // instance for storing settings

    }


    //************************************************************
    //      Method : Load File
    //************************************************************

    public void loadFile() {

        try {

            FileInputStream is = new FileInputStream(file); // open the input stream

            properties.load(is); // load all the data

            is.close(); // close stream


        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    //************************************************************
    //      Method : Save File
    //************************************************************

    public void saveFile() {

        try {

            FileOutputStream os = new FileOutputStream(file); // open file stream

            properties.store(os, "Settings"); // store settings

            os.close(); // close file stream


        } catch (Exception e) {

            System.out.println(e.getMessage());

        }


    }

    //************************************************************
    //      Method : Load Data
    //************************************************************

    public double loadData(String pKey) {

        try {
            String data = (String) (properties.get(pKey)); // access double value via key, store in string

            return Double.parseDouble(data); // parse as double, return
        } catch (Exception e) {
            return 0;
        }

    }


    //************************************************************
    //      Method : Store
    //************************************************************

    public void storeData(String pKey, double pVal) {

        properties.setProperty(pKey, pVal + ""); // store data under key

    }


}
