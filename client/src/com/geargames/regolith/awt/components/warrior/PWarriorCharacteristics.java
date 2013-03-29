package com.geargames.regolith.awt.components.warrior;

import com.geargames.awt.components.*;
import com.geargames.common.Render;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.helpers.ClientHelper;
import com.geargames.regolith.application.Application;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.menues.HorizontalFaces;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.common.String;
import com.geargames.regolith.units.dictionaries.ClientWeaponCategoryCollection;

/**
 * User: mikhail v. kutuzov
 * Панель характеристик бойца.
 */
public class PWarriorCharacteristics extends PContentPanel {
    private PWarriorPanel panel;
    private HorizontalFaces faces;
    private PLabel fighter;

    private PVitalityIndicator vitality;
    private PStrengthIndicator strength;
    private PSpeedIndicator speed;
    private PMarksmanshipIndicator marksmanship;
    private PCraftinessIndicator craftiness;

    private PHealthLabel healthLabel;
    private PVitalityLabel vitalityLabel;
    private PStrengthLabel strengthLabel;
    private PSpeedLabel speedLabel;
    private PMarksmanshipLabel marksmanshipLabel;
    private PCraftinessLabel craftinessLabel;

    private PSkillIndicator indicator0;
    private PSkillIndicator indicator1;
    private PSkillIndicator indicator2;
    private PSkillIndicator indicator3;

    private PNextWarriorButton next;
    private PPreviousWarriorButton previous;
    private PHealthIndicator healthIndicator;

    public PWarriorCharacteristics(PObject prototype, PWarriorPanel panel) {
        super(prototype);
        this.panel = panel;
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 10:
                fighter = new PSimpleLabel(index);
                addPassiveChild(fighter, index);
                break;
            case 11:
                vitality = new PVitalityIndicator((PObject) index.getPrototype());
                addPassiveChild(vitality, index);
                break;
            case 12:
                strength = new PStrengthIndicator((PObject) index.getPrototype());
                addPassiveChild(strength, index);
                break;
            case 13:
                speed = new PSpeedIndicator((PObject) index.getPrototype());
                addPassiveChild(speed, index);
                break;
            case 14:
                marksmanship = new PMarksmanshipIndicator((PObject) index.getPrototype());
                addPassiveChild(marksmanship, index);
                break;
            case 15:
                craftiness = new PCraftinessIndicator((PObject) index.getPrototype());
                addPassiveChild(craftiness, index);
                break;
            case 16:
                healthLabel = new PHealthLabel();
                healthLabel.setAnchor((byte) (1 << index.getShift()));
                healthLabel.setFont(PFontCollection.getFont10());
                addPassiveChild(healthLabel, index);
                break;
            case 17:
                vitalityLabel = new PVitalityLabel();
                vitalityLabel.setAnchor((byte) (1 << index.getShift()));
                vitalityLabel.setFont(PFontCollection.getFont10());
                addPassiveChild(vitalityLabel, index);
                break;
            case 18:
                strengthLabel = new PStrengthLabel();
                strengthLabel.setAnchor((byte) (1 << index.getShift()));
                strengthLabel.setFont(PFontCollection.getFont10());
                addPassiveChild(strengthLabel, index);
                break;
            case 19:
                speedLabel = new PSpeedLabel();
                speedLabel.setAnchor((byte) (1 << index.getShift()));
                speedLabel.setFont(PFontCollection.getFont10());
                addPassiveChild(speedLabel, index);
                break;
            case 20:
                marksmanshipLabel = new PMarksmanshipLabel();
                marksmanshipLabel.setAnchor((byte) (1 << index.getShift()));
                marksmanshipLabel.setFont(PFontCollection.getFont10());
                addPassiveChild(marksmanshipLabel, index);
                break;
            case 21:
                craftinessLabel = new PCraftinessLabel();
                craftinessLabel.setAnchor((byte) (1 << index.getShift()));
                craftinessLabel.setFont(PFontCollection.getFont10());
                addPassiveChild(craftinessLabel, index);
                break;
            case 26:
                indicator0 = new PSkillIndicator((PObject) index.getPrototype());
                addActiveChild(indicator0, index);
                break;
            case 27:
                indicator1 = new PSkillIndicator((PObject) index.getPrototype());
                addActiveChild(indicator1, index);
                break;
            case 28:
                indicator2 = new PSkillIndicator((PObject) index.getPrototype());
                addActiveChild(indicator2, index);
                break;
            case 29:
                indicator3 = new PSkillIndicator((PObject) index.getPrototype());
                addActiveChild(indicator3, index);
                break;
            case 30:
                Account account = ClientConfigurationFactory.getConfiguration().getAccount();
                Render render = Application.getInstance().getGraphics().getRender();
                faces = new HorizontalFaces(account.getWarriors(), (PObject)index.getPrototype(), render);
                addActiveChild(faces, index);
                break;
            case 31:
                healthIndicator = new PHealthIndicator((PObject) index.getPrototype());
                addPassiveChild(healthIndicator, index);
                break;
            case 32:
                previous = new PPreviousWarriorButton((PObject) index.getPrototype(), this);
                addActiveChild(previous, index);
                break;
            case 33:
                next = new PNextWarriorButton((PObject) index.getPrototype(), this);
                addActiveChild(next, index);
                break;
        }
    }

    /**
     * Установить данные текущего бойца в поля формы.
     */
    public void reset() {
        Warrior warrior = faces.getWarrior();

        panel.getBagTacklesElement().setWarrior(warrior);
        panel.getWarriorTacklesElement().setWarrior(warrior);

        healthIndicator.setWarrior(warrior);
        fighter.setText(String.valueOfC(warrior.getName()));
        vitality.setWarrior(warrior);
        strength.setWarrior(warrior);
        speed.setWarrior(warrior);
        marksmanship.setWarrior(warrior);
        craftiness.setWarrior(warrior);

        healthLabel.setWarrior(warrior);
        vitalityLabel.setWarrior(warrior);
        strengthLabel.setWarrior(warrior);
        speedLabel.setWarrior(warrior);
        marksmanshipLabel.setWarrior(warrior);
        craftinessLabel.setWarrior(warrior);

        ClientWeaponCategoryCollection categories = ClientHelper.getOrderedWeaponCategories(warrior);

        indicator0.setCategory(categories.get(0));
        indicator0.setWarrior(warrior);
        indicator1.setCategory(categories.get(1));
        indicator1.setWarrior(warrior);
        indicator2.setCategory(categories.get(2));
        indicator2.setWarrior(warrior);
        indicator3.setCategory(categories.get(3));
        indicator3.setWarrior(warrior);
    }

    public PNextWarriorButton getNext() {
        return next;
    }

    public PPreviousWarriorButton getPrevious() {
        return previous;
    }

    public boolean hasNext(){
        return faces.hasNext();
    }

    public boolean hasPrevious(){
        return faces.hasPrevious();
    }

    /**
     * Переключиться на следующего бойца клиента.
     */
    public void next() {
        faces.next();
        reset();
    }

    /**
     * Переключиться на предыдущего бойца клиента.
     */
    public void previous() {
        faces.previous();
        reset();
    }

    public Warrior getWarrior(){
        return faces.getWarrior();
    }
}
