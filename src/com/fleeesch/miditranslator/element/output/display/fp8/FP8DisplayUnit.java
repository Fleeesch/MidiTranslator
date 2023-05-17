package com.fleeesch.miditranslator.element.output.display.fp8;


import com.fleeesch.miditranslator.element.output.display.Display;
import com.fleeesch.miditranslator.functions.math.Combine;

import java.nio.charset.StandardCharsets;


public class FP8DisplayUnit extends Display {

    //************************************************************
    //      Variables
    //************************************************************

    public final int index; // index of display

    public int displayMode = 0; // current display mode

    // per line data
    public final String[] text = new String[8]; // currently displayed text
    public final int[] fontFlag = new int[8]; // currently used font flags

    //************************************************************
    //      Constructor
    //************************************************************

    public FP8DisplayUnit(int pIndex) {

        index = pIndex; // store index

        init();

    }

    //************************************************************
    //      Method : Init Display
    //************************************************************

    public void init() {

        // clear display
        setDisplayMode(0, true);
        clearDisplay();

        // fill variables
        for (int i = 0; i < 4; i++) {
            text[i] = ""; // no text
            fontFlag[i] = 0x00; // default font flag (centered normal)
        }

        device.midi.SendMessage(0, 0xB0, 0x38 + index, 4); // value bar invisible
        device.midi.SendMessage(0, 0xD0 + index, 0, 0); // peak meter
        device.midi.SendMessage(0, 0xD8 + index, 0, 0); // reduction meter


    }

    //************************************************************
    //      Method : Set Value Bar
    //************************************************************

    public void setValueBar(double pVal){
        device.midi.SendMessage(0, 0xB0, 0x38 + index, 2); // value bar invisible
        device.midi.SendMessage(0,0xB0, 0x30 + index, (int)(pVal * 0x7F));
    }

    //************************************************************
    //      Method : Set Display Mode
    //************************************************************

    public void setDisplayMode(int pMode, boolean pDel) {

        if (displayMode == pMode) return;

        if (pDel) device.midi.SendSysEx((byte) 0x13, (byte) index, (byte) (0x10 + pMode));
        else device.midi.SendSysEx((byte) 0x13, (byte) index, (byte) pMode);

        displayMode = pMode;

    }

    //************************************************************
    //      Method : Clear Display
    //************************************************************

    public void clearDisplay() {

        device.midi.SendSysEx((byte) 0x13, (byte) index, (byte) (0x10 + displayMode));

    }


    //************************************************************
    //      Method : Print
    //************************************************************

    public void print(int pOffset, int pFontFlag, String... pMsg) {

        for (String s : pMsg) {

            if (s.length() <= 0) s = " ";

            fontFlag[pOffset] = pFontFlag;
            text[pOffset] = s;

            byte[] prefix = {(byte) 0x12, (byte) index, (byte) pOffset, (byte) pFontFlag};
            byte[] msg = s.getBytes(StandardCharsets.US_ASCII);

            device.midi.SendSysEx(Combine.asByteArray(prefix, msg));

            pOffset++;
        }

    }

    //************************************************************
    //      Method : Print Replace
    //************************************************************

    public void printReplace(int pOffset, int pFontFlag, String... pMsg) {

        clearDisplay();

        print(pOffset, pFontFlag, pMsg);


    }


}
