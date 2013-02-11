package com.geargames.regolith.units.tackle;

/**
 * User: mkutuzov
 * Date: 08.02.12
 */
public abstract class Ammunition extends Tackle {
    private AmmunitionCategory category;

    /**
     * Вернуть категорию аммуниции.
     * @return
     */
    public AmmunitionCategory getCategory() {
        return category;
    }

    public void setCategory(AmmunitionCategory category) {
        this.category = category;
    }
}
