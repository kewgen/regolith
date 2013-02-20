package com.geargames.regolith.awt.components.battles;

import com.geargames.awt.components.PPrototypeElement;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.awt.components.PRadioButton;
import com.geargames.common.*;
import com.geargames.common.String;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.Graph;
import com.geargames.regolith.units.Account;

/**
 * User: mikhail v. kutuzov
 * Кнопка - слот игрок подсоединённый к бою. Должен быть нарисован в выключенном состоянии если игрок не подключился,
 * подключение реализуется после нажатия на кнопку неподсоединённым пользователем.
 */
public class PPlayerButton extends PRadioButton {
    private boolean visible;
    private Account account;
    private int number;
    private PPrototypeElement flag;

    private PSimpleLabel title;
    private boolean initiated;

    public PPlayerButton(PObject prototype) {
        super(prototype);
        IndexObject index = (IndexObject) prototype.getIndexBySlot(3);
        title = new PSimpleLabel(index);
        title.setX(index.getX());
        title.setY(index.getY());
        index = (IndexObject) prototype.getIndexBySlot(2);
        flag = new PPrototypeElement();
        flag.setX(index.getX());
        flag.setY(index.getY());
        initiated = false;
    }

    public void draw(Graphics graphics, int x, int y) {
        if (!initiated) {
            initiate(graphics.getRender());
        }
        super.draw(graphics, x, y);
    }

    protected void initiate(Render render) {
        if (account != null) {
            title.setData(String.valueOfC(account.getName()));
        } else {
            title.setData(null);
        }
        flag.setPrototype(render.getSprite(Graph.SPR_TEAM_COLOR + number));
        initiated = true;
    }

    protected void action() {

    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        initiated = false;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        initiated = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
