package com.geargames.regolith.awt.components.warrior.exchange;

import com.geargames.awt.components.*;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.UIUtils;
import com.geargames.regolith.units.tackle.Medikit;

/**
 *Панель для перемещения аптечек.
 */
public abstract class PMedikitExchangePanel extends PExchangePanel {
    private PLabel caption;
    private PLabel weight;
    private PLabel minSkill;
    private PLabel actionScores;
    private PLabel value;
    private PLabel category;
    private PStepSpinBox box;
    private PLabel amount;

    public PMedikitExchangePanel(PObject prototype) {
        super(prototype);
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 0:
                addButton1((PObject)index.getPrototype(), index);
                break;
            case 1:
                addButton2((PObject) index.getPrototype(), index);
                break;
            case 2:
                caption = new PSimpleLabel(index);
                addPassiveChild(caption, index);
                break;
            case 3:
                weight = new PSimpleLabel(index);
                addPassiveChild(weight, index);
                break;
            case 4:
                category = new PSimpleLabel(index);
                addPassiveChild(category, index);
                break;
            case 5:
                minSkill = new PSimpleLabel(index);
                addPassiveChild(minSkill, index);
                break;
            case 6:
                actionScores = new PSimpleLabel(index);
                addPassiveChild(actionScores, index);
                break;
            case 7:
                value = new PSimpleLabel(index);
                addPassiveChild(value, index);
                break;
            case 8:
                amount = new PSimpleLabel(index);
                addPassiveChild(amount, index);
                break;
            case 15:
                box = new PStepSpinBox((PObject)index.getPrototype());
                box.setStep((byte)1);
                addActiveChild(box, index);
                break;
        }
    }

    public PStepSpinBox getAmountBox() {
        return box;
    }

    protected void initiate() {
        Medikit medikit =(Medikit) getTackle();

        caption.setText(medikit.getName());
        weight.setText(UIUtils.getWeightRepresentation(medikit.getWeight()));

        minSkill.setText(""+medikit.getMinSkill().getAction()+""+medikit.getMinSkill().getExperience());
        actionScores.setText(""+medikit.getActionScores());
        value.setText(""+medikit.getValue());
        category.setText(medikit.getCategory().getName());
        amount.setText(""+getAmount());
    }
}
