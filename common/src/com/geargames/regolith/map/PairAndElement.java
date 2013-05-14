package com.geargames.regolith.map;

import com.geargames.regolith.units.map.CellElement;

/**
 * User: mvkutuzov
 * Date: 13.05.13
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class PairAndElement extends Pair {
    private CellElement element;

    public CellElement getElement() {
        return element;
    }

    public void setElement(CellElement element) {
        this.element = element;
    }
}
