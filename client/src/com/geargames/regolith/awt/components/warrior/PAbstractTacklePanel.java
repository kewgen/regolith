package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.*;
import com.geargames.common.*;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.packer.PSprite;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.app.Application;
import com.geargames.common.util.NullRegion;
import com.geargames.regolith.units.tackle.AbstractTackle;
import com.geargames.regolith.units.tackle.TackleType;
import com.geargames.common.String;
import com.geargames.regolith.units.tackle.Weapon;
import com.geargames.regolith.units.tackle.WeaponHelper;

/**
 * User: mikhail v. kutuzov
 * Класс реализует панель аммуниции, отрисовывается в зависимости от вида аммуниции.
 */
public abstract class PAbstractTacklePanel extends PContentPanel {


    private AbstractTackle tackle;
    private int amount;
    private boolean initiated;

    private PPrototypeElement image;

    private PLabel paramName1;
    private PLabel paramName2;
    private PLabel paramName3;
    private PLabel paramName4;

    private PLabel paramValue1;
    private PLabel paramValue2;
    private PLabel paramValue3;
    private PLabel paramValue4;

    private PSimpleIndicator indicator1;
    private PSimpleIndicator indicator2;
    private PSimpleIndicator indicator3;
    private PSimpleIndicator indicator4;

    private PLabel label1;
    private PLabel label2;
    private PLabel label3;
    private PLabel label4;

    private PLabel elementSize;


    public PAbstractTacklePanel(PObject prototype) {
        super(prototype);
        initiated = false;
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 76:
                image = new PPrototypeElement();
                addPassiveChild(image, index.getX(), index.getY());
                image.setRegion(NullRegion.instance);
                break;
            case 60:
                paramName1 = new PSimpleLabel(index);
                addPassiveChild(paramName1, index);
                break;
            case 61:
                paramName2 = new PSimpleLabel(index);
                addPassiveChild(paramName2, index);
                break;
            case 62:
                paramName3 = new PSimpleLabel(index);
                addPassiveChild(paramName3, index);
                break;
            case 63:
                paramName4 = new PSimpleLabel(index);
                addPassiveChild(paramName4, index);
                break;
            case 64:
                indicator1 = new PSimpleIndicator((PObject) index.getPrototype());
                addPassiveChild(indicator1, index.getX(), index.getY());
                break;
            case 65:
                indicator2 = new PSimpleIndicator((PObject) index.getPrototype());
                addPassiveChild(indicator2, index.getX(), index.getY());
                break;
            case 66:
                indicator3 = new PSimpleIndicator((PObject) index.getPrototype());
                addPassiveChild(indicator3, index.getX(), index.getY());
                break;
            case 67:
                indicator4 = new PSimpleIndicator((PObject) index.getPrototype());
                addPassiveChild(indicator4, index.getX(), index.getY());
                break;
            case 68:
                paramValue1 = new PSimpleLabel(index);
                addPassiveChild(paramValue1);
                break;
            case 69:
                paramValue2 = new PSimpleLabel(index);
                addPassiveChild(paramValue2, index);
                break;
            case 70:
                paramValue3 = new PSimpleLabel(index);
                addPassiveChild(paramValue3, index);
                break;
            case 71:
                paramValue4 = new PSimpleLabel(index);
                addPassiveChild(paramValue4, index);
                break;
            case 72:
                label1 = new PSimpleLabel(index);
                addPassiveChild(label1, index);
                break;
            case 73:
                label2 = new PSimpleLabel(index);
                addPassiveChild(label2, index);
                break;
            case 74:
                label3 = new PSimpleLabel(index);
                addPassiveChild(label3, index);
                break;
            case 75:
                label4 = new PSimpleLabel(index);
                addPassiveChild(label4, index);
                break;
            case 115:
                elementSize = new PSimpleLabel(index);
                addPassiveChild(elementSize, index);
                break;
        }
    }

    public AbstractTackle getTackle() {
        return tackle;
    }

    public int getAmount() {
        return amount;
    }

    public void setTackle(AbstractTackle tackle, int amount) {
        this.tackle = tackle;
        this.amount = amount;
        this.initiated = false;
    }

    //TODO: сделать отрисовку для случая брони, патронов, аптечки и сборщика
    public void draw(Graphics graphics, int x, int y) {
        if (!initiated) {
            Render render = graphics.getRender();
            PSprite tackleImage = render.getSprite(tackle.getFrameId());
            image.setPrototype(tackleImage);
            Application application = Application.getInstance();
            switch (tackle.getType()) {
                case TackleType.WEAPON:
                    Weapon weapon = (Weapon) tackle;
                    paramName1.setText(String.valueOfC("ТОЧНОСТЬ"));
                    paramName1.setFont(application.getFont8());
                    paramName2.setText(String.valueOfC("ДАЛЬНОСТЬ"));
                    paramName2.setFont(application.getFont8());
                    paramName3.setText(String.valueOfC("УРОН"));
                    paramName3.setFont(application.getFont8());
                    paramName4.setText(String.valueOfC("ВЕС"));
                    paramName4.setFont(application.getFont8());

                    BaseConfiguration baseConfiguration = ClientConfigurationFactory.getConfiguration().getBaseConfiguration();

                    int value = WeaponHelper.getWeaponAccuracy(weapon, baseConfiguration) * indicator1.getCardinality() / 100;
                    indicator1.setValue(value);
                    paramValue1.setText(String.valueOfI(value));
                    paramValue1.setFont(application.getFont8());
                    value = weapon.getWeaponType().getMaxDamage().getMaxDistance() * indicator2.getCardinality() / baseConfiguration.getMaxDistance();
                    indicator2.setValue(value);
                    paramValue2.setText(String.valueOfI(value));
                    paramValue2.setFont(application.getFont8());
                    value = WeaponHelper.getMaxWeaponDamage(weapon, weapon.getWeaponType().getMaxDamage().getOptDistance(), baseConfiguration) * indicator3.getCardinality() / baseConfiguration.getMaxDamage();
                    indicator3.setValue(value);
                    paramValue3.setText(String.valueOfI(value));
                    paramValue3.setFont(application.getFont8());
                    value = weapon.getWeight() * indicator4.getCardinality() / baseConfiguration.getMaxWeight();
                    indicator4.setValue(value);
                    paramValue4.setText(String.valueOfI(value));
                    paramValue4.setFont(application.getFont8());

                    label1.setText(String.valueOfC(weapon.getName()));
                    label1.setFont(application.getFont10());
                    label2.setText(String.valueOfI(weapon.getUpgrade()));
                    label2.setFont(application.getFont8());
                    label3.setText(String.valueOfC(weapon.getLoad() + "/" + weapon.getWeaponType().getCapacity()));
                    label3.setFont(application.getFont8());
                    label4.setText(String.valueOfC(weapon.getState() + "/" + weapon.getFirmness()));
                    label4.setFont(application.getFont8());

                    break;
                default:
            }

            initiated = true;
        }

        super.draw(graphics, x, y);
    }
}
