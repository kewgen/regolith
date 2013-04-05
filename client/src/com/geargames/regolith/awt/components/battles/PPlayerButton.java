package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PPrototypeElement;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.awt.components.PTouchButton;
import com.geargames.common.Graphics;
import com.geargames.common.Render;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.Graph;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: mikhail v. kutuzov, abarakov
 * Кнопка - слот игрок подсоединённый к бою. Должен быть нарисован в выключенном состоянии, если игрок не подключился,
 * подключение реализуется после нажатия на кнопку неподсоединённым пользователем.
 */
public class PPlayerButton extends PTouchButton {
    private BattleGroup battleGroup;
//    private int number;
    private PPrototypeElement flag;
    private PSimpleLabel titleLabel;
    private boolean initiated;

    public PPlayerButton(PObject prototype) {
        super(prototype);
        initiated = false;

        IndexObject index = (IndexObject) prototype.getIndexBySlot(3);
        titleLabel = new PSimpleLabel(index);
        titleLabel.setX(index.getX());
        titleLabel.setY(index.getY());

        index = (IndexObject) prototype.getIndexBySlot(2);
        flag = new PPrototypeElement();
        flag.setX(index.getX());
        flag.setY(index.getY());
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        if (!initiated) {
            initiate(graphics.getRender());
        }
//      super.draw(graphics, x, y);
        if (battleGroup.getAccount() != null) {
            //todo: рисовать рожицу игрока
            getNormalSkin().draw(graphics, x, y);
        } else {
            getPushedSkin().draw(graphics, x, y);
        }
        titleLabel.draw(graphics, x + titleLabel.getX(), y + titleLabel.getY());
        flag.draw(graphics, x + flag.getX(), y + flag.getY());
    }

    protected void initiate(Render render) {
        Account account = battleGroup.getAccount();
        if (account != null) {
            titleLabel.setText(account.getName());
        } else {
            titleLabel.setText("ПУСТО");
        }
        flag.setPrototype(render.getSprite(Graph.SPR_TEAM_COLOR + battleGroup.getAlliance().getNumber()));
        initiated = true;
    }

    public BattleGroup getBattleGroup() {
        return battleGroup;
    }

    public void setBattleGroup(BattleGroup battleGroup) {
        this.battleGroup = battleGroup;
        initiated = false;
    }

//    /**
//     * Номер боевого союза.
//     * @return
//     */
//    public int getNumber() {
//        return number;
//    }
//
//    public void setNumber(int number) {
//        this.number = number;
//        initiated = false;
//    }

    @Override
    public void onClick() {
        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getBattlesPanel().onPlayerButtonClick(battleGroup);
    }

}
