package com.geargames.regolith.awt.components.warrior.exchange;

import com.geargames.awt.components.PLabel;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.awt.components.PStepSpinBox;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.helpers.ClientGUIHelper;
import com.geargames.regolith.units.tackle.Weapon;
import com.geargames.regolith.units.tackle.WeaponDistances;
import com.geargames.regolith.units.tackle.WeaponType;

/**
 * User: mikhail v. kutuzov
 * Панель для отображения оружия на складе которое нужно переложить в сумку
 * бойца или одеть на него.
 */
public abstract class PWeaponExchangePanel extends PExchangePanel {
    private int number;

    private PLabel caption;

    private PLabel weight;
    private PLabel firmness;
    private PLabel state;
    private PLabel upgrade;

    private PLabel load;
    private PLabel capacity;
    private PLabel accuracy;
    private PLabel accurateAction;
    private PLabel baseFirmness;
    private PLabel ammunitionPerShoot;

    public PWeaponExchangePanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                addButton1((PObject)index.getPrototype(), index);
                break;
            case 1:
                addButton2((PObject)index.getPrototype(), index);
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
                load = new PSimpleLabel(index);
                addPassiveChild(load, index);
                break;
            case 8:
                capacity = new PSimpleLabel(index);
                addPassiveChild(capacity, index);
                break;
            case 9:
                accuracy = new PSimpleLabel(index);
                addPassiveChild(accuracy, index);
                break;
            case 10:
                accurateAction = new PSimpleLabel(index);
                addPassiveChild(accurateAction, index);
                break;
            case 11:
                baseFirmness = new PSimpleLabel(index);
                addPassiveChild(baseFirmness,  index);
                break;
            case 14:
                ammunitionPerShoot = new PSimpleLabel(index);
                addPassiveChild(ammunitionPerShoot, index);
                break;
            default:
        }
    }

    protected void initiate() {
        Weapon weapon = (Weapon)getTackle();
        caption.setText(weapon.getName());
        weight.setText(ClientGUIHelper.getWeightRepresentation(weapon.getWeight()));
        firmness.setText(""+weapon.getFirmness());
        state.setText(""+weapon.getState());
        upgrade.setText(""+weapon.getUpgrade());
        load.setText(ClientGUIHelper.getWeightRepresentation(weapon.getLoad()));
        WeaponType weaponType = weapon.getWeaponType();

        capacity.setText(""+weaponType.getCapacity());
        accuracy.setText(""+weaponType.getAccuracy());
        accurateAction.setText(""+weaponType.getAccurateAction());
        baseFirmness.setText(""+weaponType.getBaseFirmness());
        /*category.setText(String.valueOfC(weaponType.getCategory().getName()));*/
        WeaponDistances distances = weaponType.getDistance();
        /*distance.setText(String.valueOfC(distances.getMin() + "/" + distances.getMinOptimal() + "/" + distances.getMaxOptimal() + "/" + distances.getMax()));*/
        ammunitionPerShoot.setText(""+weaponType.getAmmunitionPerShoot());
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public PStepSpinBox getAmountBox() {
        return null;
    }
}
