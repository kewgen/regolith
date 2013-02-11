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
        ArrayList indexes = prototype.getIndexes();
        int size = indexes.size();
        for (int i = 1; i < size; i++) {
            IndexObject index = (IndexObject) indexes.get(i);
            if (index.isSlot()) {
                switch (i) {
                    case 2:
                        image = new PPrototypeElement();
                        addPassiveChild(image, index.getX(), index.getY());
                        image.setRegion(NullRegion.instance);
                        break;
                    case 3:
                        paramName1 = new PSimpleLabel(index);
                        addPassiveChild(paramName1, index);
                        break;
                    case 4:
                        paramName2 = new PSimpleLabel(index);
                        addPassiveChild(paramName2, index);
                        break;
                    case 5:
                        paramName3 = new PSimpleLabel(index);
                        addPassiveChild(paramName3, index);
                        break;
                    case 6:
                        paramName4 = new PSimpleLabel(index);
                        addPassiveChild(paramName4, index);
                        break;
                    case 7:
                        indicator1 = new PSimpleIndicator((PObject) index.getPrototype());
                        addPassiveChild(indicator1, index.getX(), index.getY());
                        break;
                    case 8:
                        indicator2 = new PSimpleIndicator((PObject) index.getPrototype());
                        addPassiveChild(indicator2, index.getX(), index.getY());
                        break;
                    case 9:
                        indicator3 = new PSimpleIndicator((PObject) index.getPrototype());
                        addPassiveChild(indicator3, index.getX(), index.getY());
                        break;
                    case 10:
                        indicator4 = new PSimpleIndicator((PObject) index.getPrototype());
                        addPassiveChild(indicator4, index.getX(), index.getY());
                        break;
                    case 11:
                        paramValue1 = new PSimpleLabel(index);
                        addPassiveChild(paramValue1);
                        break;
                    case 12:
                        paramValue2 = new PSimpleLabel(index);
                        addPassiveChild(paramValue2, index);
                        break;
                    case 13:
                        paramValue3 = new PSimpleLabel(index);
                        addPassiveChild(paramValue3, index);
                        break;
                    case 14:
                        paramValue4 = new PSimpleLabel(index);
                        addPassiveChild(paramValue4, index);
                        break;
                    case 15:
                        label1 = new PSimpleLabel(index);
                        addPassiveChild(label1, index);
                        break;
                    case 16:
                        label2 = new PSimpleLabel(index);
                        addPassiveChild(label2, index);
                        break;
                    case 17:
                        label3 = new PSimpleLabel(index);
                        addPassiveChild(label3, index);
                        break;
                    case 18:
                        label4 = new PSimpleLabel(index);
                        addPassiveChild(label4, index);
                        break;
                    case 19:
                        elementSize = new PSimpleLabel(index);
                        addPassiveChild(elementSize, index);
                        break;
                }
            }
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
                    paramName1.setData(String.valueOfC("ТОЧНОСТЬ"));
                    paramName1.setFont(application.getFont8());
                    paramName2.setData(String.valueOfC("ДАЛЬНОСТЬ"));
                    paramName2.setFont(application.getFont8());
                    paramName3.setData(String.valueOfC("УРОН"));
                    paramName3.setFont(application.getFont8());
                    paramName4.setData(String.valueOfC("ВЕС"));
                    paramName4.setFont(application.getFont8());

                    BaseConfiguration baseConfiguration = ClientConfigurationFactory.getConfiguration().getBaseConfiguration();

                    int value = WeaponHelper.getWeaponAccuracy(weapon, baseConfiguration) * indicator1.getCardinality() / 100;
                    indicator1.setValue(value);
                    paramValue1.setData(String.valueOfI(value));
                    paramValue1.setFont(application.getFont8());
                    value = weapon.getWeaponType().getMaxDamage().getMaxDistance() * indicator2.getCardinality() / baseConfiguration.getMaxDistance();
                    indicator2.setValue(value);
                    paramValue2.setData(String.valueOfI(value));
                    paramValue2.setFont(application.getFont8());
                    value = WeaponHelper.getMaxWeaponDamage(weapon, weapon.getWeaponType().getMaxDamage().getOptDistance(), baseConfiguration) * indicator3.getCardinality() / baseConfiguration.getMaxDamage();
                    indicator3.setValue(value);
                    paramValue3.setData(String.valueOfI(value));
                    paramValue3.setFont(application.getFont8());
                    value = weapon.getWeight() * indicator4.getCardinality() / baseConfiguration.getMaxWeight();
                    indicator4.setValue(value);
                    paramValue4.setData(String.valueOfI(value));
                    paramValue4.setFont(application.getFont8());

                    label1.setData(String.valueOfC(weapon.getName()));
                    label1.setFont(application.getFont10());
                    label2.setData(String.valueOfI(weapon.getUpgrade()));
                    label2.setFont(application.getFont8());
                    label3.setData(String.valueOfC(weapon.getLoad() + "/" + weapon.getWeaponType().getCapacity()));
                    label3.setFont(application.getFont8());
                    label4.setData(String.valueOfC(weapon.getState() + "/" + weapon.getFirmness()));
                    label4.setFont(application.getFont8());

                    break;
                default:
            }

            initiated = true;
        }

        super.draw(graphics, x, y);
    }
}
