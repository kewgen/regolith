package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Medikit;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ClientMedikitCollection extends MedikitCollection {
    private Vector medikits;

    public Vector getMedikits() {
        return medikits;
    }

    public void setMedikits(Vector medikits) {
        this.medikits = medikits;
    }

    public Medikit get(int index) {
        return (Medikit)medikits.elementAt(index);
    }

    public void add(Medikit medikit) {
        medikits.addElement(medikit);
    }

    public void insert(Medikit medikit, int index) {
        medikits.insertElementAt(medikit, index);
    }

    public void remove(int index) {
        medikits.removeElementAt(index);
    }

    public int size() {
        return medikits.size();
    }

    public void set(Medikit medikit, int index) {
        medikits.set(index, medikit);
    }
}
