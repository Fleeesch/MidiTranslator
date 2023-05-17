package com.fleeesch.miditranslator.element.virtual.mpe;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.pad.PadPressureSensitive;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.MpeElement;


public class MpeDataHandler extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    // original surface
    public final MpeSurface mpeSurface;

    // position in coordinate system
    public final int posX;
    public final int posY;

    // Mpe Element
    public MpeElement mpeElement;

    // pressure sensitive Pad
    public PadPressureSensitive padPressureSensitive;

    public boolean hasPressure = false;

    public boolean hardPress = false;

    //************************************************************
    //      Constructor
    //************************************************************

    public MpeDataHandler(String pName, MpeSurface pSurface, int pX, int pY, InputElement pInput, OutputElement pOutput) {

        super(pName);

        if (pInput != null) addSource(pInput);

        addTarget(pOutput);

        if (pInput != null && sourceElements.get(0) instanceof PadPressureSensitive) {
            padPressureSensitive = (PadPressureSensitive) sourceElements.get(0);
            hasPressure = true;
        }

        mpeSurface = pSurface;

        posX = pX;
        posY = pY;


    }

    //************************************************************
    //      Method : Set Color
    //************************************************************

    public void setColor(double r, double g, double b) {

        setParameterValue(1);
        setParameterValue(1, r);
        setParameterValue(2, g);
        setParameterValue(3, b);
        setParameterValue(4, 1);

        // update the LED
        for (OutputElement e : targetElements) e.update();

    }


    //************************************************************
    //      Event : Handle Input
    //************************************************************

    @Override
    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {
        super.handleInput(pSource, pVal, pMidiMsg);

        if (hasPressure) hardPress = padPressureSensitive.hardPress;

        mpeElement.handleTrigger(pVal);

    }

    //************************************************************
    //      Event : On MPE Z Change
    //************************************************************

    public void onMpeZChange(InputElement pInputElement, double pVal) {

        mpeElement.handleMpeZ(pVal);
    }

    //************************************************************
    //      Event : On Raw Pressure Change
    //************************************************************

    @Override
    public void handleInputRawPressure(double pVal) {

        mpeElement.handlePressure(pVal);

    }


}
