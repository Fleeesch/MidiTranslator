package com.fleeesch.miditranslator.data.parameter.condition;

import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.event.EventHandler;
import com.fleeesch.miditranslator.event.EventSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Condition extends EventSource {


    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Static
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Variables
    //************************************************************


    public static final List<Condition> list = new ArrayList<>(); // list of all created conditions

    public static Condition currentInstance; // current condition used in mapping

    public static int index = 0; // current index of condition

    public static boolean inverted; // inverted

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Full Entry

    // condition entries
    public List<ConditionEntry> entries = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Without Negation
    // hashmap for looking up an entry via parameter
    public final HashMap<Parameter, ConditionEntry> hash = new HashMap<>();

    // current condition check state
    public boolean state;

    // previous condition
    public final Condition previousCondition;

    // has entry been added?
    public boolean entryAdded = false;

    //************************************************************
    //      Constructor
    //************************************************************

    public Condition() {

        previousCondition = Condition.currentInstance; // store previous condition

        Condition.currentInstance = this; // mark this one as current condition

        // don't transfer previous entries if there's no previous condition
        if (previousCondition == null || previousCondition.entries == null) return;

        // copy all the entries from the previous condition
        entries = new ArrayList<>(previousCondition.entries);

    }

    //************************************************************
    //      Method : Look for Duplicate
    //************************************************************

    public static Condition add(Parameter pParameter, double pValue, int pCompFlag) {

        // check for a duplicate that would match created condition chain
        Condition duplicate = lookForDuplicate(pParameter, pValue, pCompFlag);

        // duplicate found? prevent creating a new condition
        if (duplicate != null) {
            set(duplicate); // set current condition to duplicate
            return duplicate; // return the duplicate,
        }

        list.add(new Condition()); // create new condition, add to list

        currentInstance.addEntry(pParameter, pValue, pCompFlag); // copy condition entry chain to new condition

        Device.last.conditions.add(currentInstance); // add condition to a list of a device for checkups

        index++; // increment index

        return currentInstance; // return created condition
    }

    //************************************************************
    //      Method : Invert
    //************************************************************

    public static void invert(boolean pState) {

        inverted = pState;

    }

    //************************************************************
    //      Method : Match Entries
    //************************************************************

    public static Condition add(Parameter pParameter, double pValue) {

        return add(pParameter, pValue, 0);
    }

    //************************************************************
    //      Method : Back
    //************************************************************

    public static void back() {

        // no need to do anything of already at the start
        if (index <= 0) return;

        // back at the start? clear everything
        if (index == 1) {
            clear();
            return;
        }

        Condition.set(Condition.currentInstance.previousCondition); // return to previous condition

    }

    //************************************************************
    //      Method : Clear
    //************************************************************

    public static void clear() {

        currentInstance = null; // no current condition
        inverted = false;

        index = 0; // index is set back to start

    }


    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Public
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /*

        The individual Condition, containing a bunch of Entries that are serially created.
        Right now they are a just an AND comparison with an optional negation and a comparison flag.

     */

    //************************************************************
    //      Method : Get
    //************************************************************

    public static Condition get() {

        return currentInstance; // just the current condition with its entries

    }
    //************************************************************
    //      Method : Set
    //************************************************************

    public static void set(Condition pCondition) {

        // condition can be null through automated processes
        if (pCondition == null) {
            clear(); // just clear everything and skip the rest
            return;
        }

        currentInstance = pCondition; // transfer condition

        index = pCondition.entries.size(); // get index from entry count

    }

    //************************************************************
    //      Method : Look for Duplicate
    //************************************************************


    public static Condition lookForDuplicate(Parameter pParameter, double pValue, int pCompFlag) {


        for (Condition c : list) {

            ConditionEntry lastEntry = c.entries.get(c.entries.size() - 1); // get the latest entry from listed condition

            // check if all the parameters match
            if (lastEntry.parameter != pParameter) continue;
            if (lastEntry.value != pValue) continue;
            if (lastEntry.comparisonFlag != pCompFlag) continue;

            // first index / no given instance?
            if (currentInstance == null) {

                if (c.entries.size() == 1) return c;  // check if current condition was also the first one, then pick it

                continue; // nope, no match, go to the next one
            }

            // current condition is not the first one in the chain AND
            // the size of the listed condition matches
            if (currentInstance.entries.size() == c.entries.size() - 1) {

                if (!matchEntries(currentInstance.entries, c.entries)) continue; // entries must be absolutely identical

                return c; // yap, condition is a duplicate, pick this one

            }

        }

        return null; // no duplicate found
    }

    //************************************************************
    //      Method : Match Entries
    //************************************************************

    private static boolean matchEntries(List<ConditionEntry> entryA, List<ConditionEntry> entryB) {

        int l = Math.min(entryA.size(), entryB.size()); // use the shortest entry length as the reference

        // go through entries, compare values
        for (int i = 0; i < l; i++) {

            if (entryA.get(i).parameter != entryB.get(i).parameter) return false;
            if (entryA.get(i).value != entryB.get(i).value) return false;
            if (entryA.get(i).comparisonFlag != entryB.get(i).comparisonFlag) return false;

        }

        // everything matches!
        return true;
    }

    //************************************************************
    //      Method : Check All Conditions
    //************************************************************
    public static void checkAllConditions() {

        for (Condition c : list) c.check();

    }

    //************************************************************
    //      Constructor
    //************************************************************

    public static void checkDeviceConditions(Device d) {

        for (Condition c : d.conditions) c.check(); // check conditions of device

        d.filterAvailableElements(); // filter available virtual elements on device

    }

    //************************************************************
    //      Method : Add Entry
    //************************************************************

    public void addEntry(Parameter pParameter, double pValue, int pCompFlag) {

        if (entryAdded) return; // there can only be one new entry

        entries.add(new ConditionEntry(pParameter, pValue, pCompFlag)); // add entry

        // go through entries
        for (ConditionEntry d : entries) {

            hash.put(d.parameter, d); // store hash
            addEventHandler(d.parameter); // add event handler (so it knows that it is used in this condition)

        }

        entryAdded = true; // entry added, there can be no more than one

    }

    //************************************************************
    //      Method : Check
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Check whole Condition

    public boolean check() {

        // go through entry, check them individually
        for (ConditionEntry c : entries) if (!c.check()) return setState(false);

        // all checks passed? set state to true
        return setState(true);

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Check only Parameter

    public boolean check(Parameter pParameter) {

        hash.get(pParameter).check(); // get entry via hashmap, check it

        // go through the rest, check if their last checks passed
        for (ConditionEntry d : entries) if (!d.state) return setState(false);

        // all checks passed, condition is true
        return setState(true);

    }

    //************************************************************
    //      Method : Set State
    //************************************************************

    public boolean setState(boolean pState) {

        state = pState; // store current state

        informEventHandlers(); // inform handlers that something has changed

        return state; // return the state

    }

    //************************************************************
    //      Method : Inform Event Handlers
    //************************************************************

    public void informEventHandlers() {
        // go through elements, trigger condition change method

        for (EventHandler eventHandler : eventHandlers) eventHandler.onConditionChange(this, state);


    }


}
