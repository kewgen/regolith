package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.tackle.StateTackle;

import java.util.Vector;

/**
 * @author Mikhail_Kutuzov
 *         created: 29.05.12  23:50
 */
public class ClientStateTackleCollection extends StateTackleCollection {
    private Vector tackles;

    public ClientStateTackleCollection(){
    }

    public ClientStateTackleCollection(Vector tackles) {
        this.tackles = tackles;
    }

    public Vector getTackles() {
        return tackles;
    }

    public void setTackles(Vector tackles) {
        this.tackles = tackles;
    }

    public StateTackle get(int index) {
        return (StateTackle)tackles.elementAt(index);
    }

    public void add(StateTackle abstractTackle) {
        tackles.addElement(abstractTackle);
    }

    public void insert(StateTackle abstractTackle, int index) {
        tackles.insertElementAt(abstractTackle, index);
    }

    public void set(StateTackle abstractTackle, int index) {
        tackles.setElementAt(abstractTackle, index);
    }

    public void remove(int index) {
        tackles.removeElementAt(index);
    }

    public int size() {
        return tackles.size();
    }
}
