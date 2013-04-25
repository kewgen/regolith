package com.geargames.regolith.units.map;

import com.geargames.regolith.units.battle.BattleAlliance;

/**
 * User: mkutuzov
 * Date: 19.02.12
 * Клиентская реализация клетки игрового поля.
 */
public class ClientBattleCell extends BattleCell {
    /**
     *  Мысли вслух:
     *
     *  byte scope:
     *  значение  назначение
     *  0         ячейка скрытая туманом войны для всего альянса
     *  1         открывалась ли когда-либо ячейка карты (эта ячейка не под туманом войны)
     *  2         ячейка входит в видимую область отдельным бойцом (не входящая ни в одну из областей стрельбы)
     *  3         дальняя область стрельбы
     *  4         оптимальная область стрельбы
     *  5         ближняя область стрельбы
     *
     *  short visibility:
     *  значение 0 - ячейка
     *  № бита   назначение
     *  0         ячейка в прямой видимости бойца [0-го игрока (первого) в альянсе, его 0-го бойца (первого)]
     *  ...
     *  15        ячейка в прямой видимости бойца [3-го игрока (последнего) в альянсе, его 3-го бойца (последнего)]
     *
     *
     *  область скрытая туманом войны
     *  открытая часть карты, видимая когда-либо (не скрытая туманом войны)
     *  видимая область карты (видимая каким-либо бойцом альянса в данный момент) (НЕ ТРЕБУЕТСЯ)
     *  00  видимая область отдельным бойцом (не входящая ни в одну из областей стрельбы)
     *  01  дальняя область стрельбы
     *  10  оптимальная область стрельбы
     *  11  ближняя область стрельбы
     *  00  область между центром самого бойца и до ближней области стрельбы (СОВМЕЩАЕТСЯ с областью видимости бойца)
     *  ячейка одна из клеток в маршруте движения бойца
     */


    private short visible;
    private short path;
    private byte visited;

    public short getVisibility(BattleAlliance battleAlliance) {
        return visible;
    }

    public short getOptimalPath(BattleAlliance battleAlliance) {
        return path;
    }

    public void setVisibility(BattleAlliance battleAlliance, short visibility) {
        visible = visibility;
    }

    public void setOptimalPath(BattleAlliance battleAlliance, short path) {
        this.path = path;
    }

    public boolean isVisited(BattleAlliance battleAlliance) {
        return visited != 0;
    }

    public void setVisited(BattleAlliance battleAlliance, boolean visited) {
        if (visited) {
            this.visited = (byte) 1;
        } else {
            this.visited = (byte) 0;
        }
    }

}
