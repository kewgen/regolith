package com.geargames.regolith.helpers;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.units.tackle.Armor;

/**
 * @author Mikhail_Kutuzov
 *         created: 16.03.12  17:12
 */
public class ArmorHelper {

    /**
     * Вернуть прибавку к крепкости за счёт уровня upgrade брони.
     *
     * @param armor
     * @return
     */
    public static int getArmorBonus(Armor armor, BaseConfiguration baseConfiguration) {
        int max = baseConfiguration.getMaxWorkShopLevel();
        return armor.getUpgrade() + (armor.getUpgrade() == max ? 2 : 0);
    }

    /**
     * Подсчитать степень защиты брони.
     * предполагается:
     * 1. n уровень брони снимает n% повреждения при попадении(ограничение n <= 95)
     * 2. влияние уровня брони падает с ухудшением состояния брони
     * state = 0 -> броня влияет на 50%, state = прочность -> броня влияет на 100%
     * 3. n уровень брони складывается из базового значения и числа пропорционального обновлению брони.
     *
     * @param armor
     * @return
     */
    public static double getArmorInfluence(Armor armor, BaseConfiguration baseConfiguration) {
        int armorValue = armor.getArmorType().getArmor() + getArmorBonus(armor, baseConfiguration);
        return ((double) armorValue / 100.0) * ((50 + armor.getState() * 50 / armor.getFirmness()) / 100.0);
    }

    /**
     * Метод возвращает остаток урона damage, который броня armor не смогла свести на нет.
     *
     * @param armor
     * @param damage
     * @return
     */
    public static int getArmorDamageInfluence(Armor armor, int damage, BaseConfiguration baseConfiguration) {
        return (int) (damage * (1 - getArmorInfluence(armor, baseConfiguration)));
    }

}
