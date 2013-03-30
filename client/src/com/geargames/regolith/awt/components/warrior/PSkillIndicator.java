package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.*;
import com.geargames.common.Graphics;
import com.geargames.common.Render;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.NullRegion;
import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.helpers.WarriorHelper;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.tackle.WeaponCategory;

/**
 * User: mikhail v. kutuzov
 * Индикатор навыка бойца по категории оружия.
 * Эта реализация опирается на то что количество спрайтов отвечающих за разные уровни навыка соответсвует
 * количеству этих уровней.
 */
public class PSkillIndicator extends PContentPanel {
    private PSpriteProgressIndicator indicator;
    private PLabel nameLabel;
    private PLabel valueLabel;
    private PPrototypeElement icon;
    private int basePID;
    private boolean initiated;

    private WeaponCategory category;
    private Warrior warrior;

    public PSkillIndicator(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 22:
                indicator = new PSimpleIndicator((PObject) (index.getPrototype()));
                addActiveChild(indicator, index);
                break;
            case 23:
                nameLabel = new PSimpleLabel(index);
                nameLabel.setFont(PFontCollection.getFont10());
                addPassiveChild(nameLabel, index);
                break;
            case 24:
                valueLabel = new PSimpleLabel(index);
                valueLabel.setFont(PFontCollection.getFont10());
                addPassiveChild(valueLabel, index);
                break;
            case 25:
                icon = new PPrototypeElement();
                basePID = index.getPrototype().getPID();
                addPassiveChild(icon, index);
                icon.setRegion(NullRegion.instance);
                break;
        }
    }

    public void draw(Graphics graphics, int x, int y) {
        if (!initiated) {
            Render render = graphics.getRender();
            icon.setPrototype(render.getSprite(basePID + category.getId()));
            short categoryScore = WarriorHelper.getSkillScore(warrior, category);
            nameLabel.setText(category.getName());
            valueLabel.setText(""+categoryScore);
            BaseConfiguration configuration = ClientConfigurationFactory.getConfiguration().getBaseConfiguration();
            indicator.setValue((WarriorHelper.getSkillByExperience(categoryScore, configuration).getId() + 1));
            initiated = true;
        }
        super.draw(graphics, x, y);
    }

    public WeaponCategory getCategory() {
        return category;
    }

    public void setCategory(WeaponCategory category) {
        this.category = category;
        initiated = false;
    }

    public Warrior getWarrior() {
        return warrior;
    }

    /**
     * Установить бойца.
     *
     * @param warrior
     */
    public void setWarrior(Warrior warrior) {
        this.warrior = warrior;
        initiated = false;
    }

}
