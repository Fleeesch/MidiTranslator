package com.fleeesch.miditranslator.data.parameter.condition;

import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.comparison.*;
import com.fleeesch.miditranslator.event.EventSource;

public class ConditionEntry extends EventSource {

    //************************************************************
    //      Variables
    //************************************************************

    private Comparison comparison;

    public final Parameter parameter;
    public final double value;
    public final int comparisonFlag;

    public boolean state = true;



    //************************************************************
    //      Constructor
    //************************************************************

    public ConditionEntry(Parameter pParameter, double pValue, int pCompFlag) {

        parameter = pParameter;
        value = pValue;
        comparisonFlag = pCompFlag;

        switch (comparisonFlag) {
            case 0 -> comparison = new ComparisonEqual();
            case 1 -> comparison = new ComparisonNot();
            case 2 -> comparison = new ComparisonSmaller();
            case 3 -> comparison = new ComparisonLarger();
            case 4 -> comparison = new ComparisonEqualSmaller();
            case 5 -> comparison = new ComparisonEqualLarger();
        }

    }

    //************************************************************
    //      Method : Check
    //************************************************************

    public boolean check() {

        return state = comparison.compare(parameter.get(), value);

    }


}
