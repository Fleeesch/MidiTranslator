package com.fleeesch.miditranslator.element.virtual.controller.motorfader.control;

import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.MotorFaderController;

public abstract class MfcControl extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    public MotorFaderController controller; // controller object

    //************************************************************
    //      Constructor
    //************************************************************

    public MfcControl(String pName){

        super(pName);

    }


}
