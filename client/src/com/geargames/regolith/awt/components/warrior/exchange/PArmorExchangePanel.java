package com.geargames.regolith.awt.components.warrior.exchange;

import com.geargames.awt.components.*;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.helpers.ClientGUIHelper;
import com.geargames.regolith.units.tackle.Armor;
import com.geargames.regolith.units.tackle.ArmorType;

/**
 * Created by IntelliJ IDEA.
 * User: mikhail.kutuzov
 * Date: 08.12.12
 * Панель для отображения брони на складе которую требуется переложить в сумку
 * бойца либо надеть на него.
 */
public abstract class PArmorExchangePanel extends PExchangePanel {
    private PLabel caption;

    private PLabel weight;
    private PLabel firmness;
    private PLabel state;
    private PLabel upgrade;

    private PLabel armor;
    private PLabel baseFirmness;
    private PLabel craftinessBonus;
    private PLabel marksmanshipBonus;
    private PLabel regenerationBonus;
    private PLabel visibilityBonus;

    public PArmorExchangePanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                addButton1((PObject) index.getPrototype(), index);
                break;
            case 1:
                addButton2((PObject) index.getPrototype(), index);
                break;
            case 109:
                caption = new PSimpleLabel(index);
                addPassiveChild(caption, index);
                break;
            case 3:
                firmness = new PSimpleLabel(index);
                addPassiveChild(firmness, index);
                break;
            case 4:
                state = new PSimpleLabel(index);
                addPassiveChild(state, index);
                break;
            case 5:
                weight = new PSimpleLabel(index);
                addPassiveChild(weight, index);
                break;
            case 6:
                upgrade = new PSimpleLabel(index);
                addPassiveChild(upgrade, index);
                break;
            case 7:
                armor = new PSimpleLabel(index);
                addPassiveChild(armor, index);
                break;
            case 8:
                baseFirmness = new PSimpleLabel(index);
                addPassiveChild(baseFirmness, index);
                break;
            case 9:
                craftinessBonus = new PSimpleLabel(index);
                addPassiveChild(craftinessBonus, index);
                break;
            case 10:
                marksmanshipBonus = new PSimpleLabel(index);
                addPassiveChild(marksmanshipBonus, index);
                break;
            case 11:
                regenerationBonus = new PSimpleLabel(index);
                addPassiveChild(regenerationBonus, index);
                break;
/*
                    case 12:
                        speedBonus = new PSimpleLabel(index);
                        addPassiveChild(speedBonus, index);
                        break;
                    case 13:
                        strengthBonus = new PSimpleLabel(index);
                        addPassiveChild(strengthBonus, index);
                        break;
*/
            case 14:
                visibilityBonus = new PSimpleLabel(index);
                addPassiveChild(visibilityBonus, index);
                break;
        }
    }

    protected void initiate() {
        Armor tackle = (Armor)getTackle();

        caption.setText(tackle.getName());
        weight.setText(ClientGUIHelper.getWeightRepresentation(tackle.getWeight()));
        firmness.setText(""+tackle.getFirmness());
        state.setText(""+tackle.getState());
        upgrade.setText(""+tackle.getUpgrade());

        ArmorType armorType = tackle.getArmorType();
        armor.setText(""+armorType.getArmor());
        baseFirmness.setText(""+armorType.getBaseFirmness());

        craftinessBonus.setText(""+armorType.getCraftinessBonus());
        marksmanshipBonus.setText(""+armorType.getMarksmanshipBonus());
        regenerationBonus.setText(""+armorType.getRegenerationBonus());
/*
        speedBonus.setText(String.valueOfI(armorType.getSpeedBonus()));
        strengthBonus.setText(ClientGUIHelper.getWeightRepresentation(armorType.getStrengthBonus()));
*/
        visibilityBonus.setText(""+armorType.getVisibilityBonus());
    }

    public PStepSpinBox getAmountBox() {
        return null;
    }
}
