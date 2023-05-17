package com.fleeesch.miditranslator.event;

import com.fleeesch.miditranslator.element.input.InputElement;

public interface MpeEventHandler {

    void onMpeZChange(InputElement pInputElement, double pVal);

}
