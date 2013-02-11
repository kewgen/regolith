package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.Medikit;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 26.04.12
 */
public class ServerMedikitCollection extends MedikitCollection {

    private List<Medikit> medikits;

    public List<Medikit> getMedikits() {
        return medikits;
    }

    public void setMedikits(List<Medikit> medikits) {
        this.medikits = medikits;
    }

    public Medikit get(int index) {
        return medikits.get(index);
    }

    public void add(Medikit medikit) {
        medikits.add(medikit);
    }

    public void insert(Medikit medikit, int index) {
        medikits.add(index, medikit);
    }

    public void remove(int index) {
        medikits.remove(index);
    }

    public int size() {
        return medikits.size();
    }

    public void set(Medikit medikit, int index) {
        medikits.set(index, medikit);
    }
}
