package com.geargames.regolith.units.map;

import com.geargames.regolith.units.Element;
import com.geargames.regolith.units.Entity;
import com.geargames.regolith.units.battle.BattleAlliance;

import java.io.Serializable;

/**
 * Абстрактная клетка игрового поля, должна расширятся клиентской и серверной версиями.
 * User: mkutuzov
 * Date: 22.03.12
 */
public abstract class BattleCell implements Serializable {
    private Element element;
    private byte order;

    /**
     * Вернуть преграду или бойца находящегося в этой ячейке.
     * @return
     */
    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Вернуть битовую маску достижимости ячйки карты.
     * 0-6 биты содержат наименьшее количество клеток, которое надо будет преодолеть бойцу, чтоб добраться до этой точки.
     * @return
     */
    public byte getOrder() {
        return order;
    }

    public void setOrder(byte order) {
        this.order = order;
    }

    /**
     * Вернуть битовую маску видимости клеток бойцами военного союза battleAlliance.
     * Если клетка видима бойцом с номером N в военном союзе, то бит карты установлен.
     * @param battleAlliance
     * @return
     */
    public abstract short getVisibility(BattleAlliance battleAlliance);

    /**
     * Вернуть битовую маску оптимального пути бойцов военного союза battleAlliance.
     * Если клетка принадлежит оптимальному пути
     * @param battleAlliance
     * @return
     */
    public abstract short getOptimalPath(BattleAlliance battleAlliance);

    /**
     * Была ли точка засвечена бойцами военного союза battleAlliance.
     * @param battleAlliance
     * @return
     */
    public abstract boolean isVisited(BattleAlliance battleAlliance);

    public abstract void setVisibility(BattleAlliance battleAlliance, short visibility);
    public abstract void setOptimalPath(BattleAlliance battleAlliance, short path);
    public abstract void setVisited(BattleAlliance battleAlliance, boolean visited);
}
