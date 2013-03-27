package com.geargames.regolith.helpers;

import com.geargames.regolith.units.Bag;
import com.geargames.regolith.units.dictionaries.StateTackleCollection;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * User: mikhail v. kutuzov
 *
 */
public class BagHelper {

    /**
     * Положить снаряжение в сумку.
     *
     * @param bag
     * @param tackle
     */
    public static void putIntoBag(Bag bag, StateTackle tackle) {
        bag.getTackles().add(tackle);
        bag.setWeight((short) (tackle.getWeight() + bag.getWeight()));
    }


    /**
     * Вынуть снаряжение из указанного кармана.
     *
     * @param bag
     * @param number
     * @return
     */
    public static StateTackle putOut(Bag bag, int number){
        StateTackleCollection tackles = bag.getTackles();
        StateTackle tackle = tackles.get(number);
        tackles.remove(number);
        bag.setWeight((short)(bag.getWeight() - tackle.getWeight()));
        return tackle;
    }

    public static int getSize(Bag bag){
        return bag.getTackles().size();
    }
}
