package com.geargames.regolith.units.map.finder;

import com.geargames.regolith.ClientBattleContext;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.map.Finder;
import com.geargames.regolith.map.Pair;

/**
 * User: mkutuzov
 * Date: 16.02.12
 * Вычисляем ячейку сетки по точке карты за конечное число шагов.
 * Проецируя точки пересечения двух прямых, проходящих через точку карты
 * x y параллельно соответсвующим сторонам сетки, с противоположными сторонами сеткиж получаем значения.
 * Для примера возьмём левую ось сетки:
 * проекция пересечния прямой и стороны сетки на ось X =  x1, поделив её на проекцию длины стороны клетки на ось X = BattleScreen.HORIZONTAL_RADIUS
 * получим индекс = [количество клеток - 1] - min[x1 + BattleScreen.HORIZONTAL_RADIUS /BattleScreen.HORIZONTAL_RADIUS]
 * который равен индексу нажатой ячейки по левой оси сетки.
 */
public class ProjectionFinder extends Finder {
    private Pair cell;

    public ProjectionFinder() {
        cell = new Pair();
    }

    public Pair find(int x, int y, BattleScreen battleScreen) {
        int length = ClientConfigurationFactory.getConfiguration().getBattleContext().getBattle().getMap().getCells().length;
        double k = ClientBattleContext.TANGENT;
        int b1 = battleScreen.getB1();
        int b4 = battleScreen.getB4();
        int b41 = (int) (y - k * x);
        int b11 = (int) (y + k * x);

        int x1 = (b4 - b41) / (int) (k * 2);
        int x2 = (b11 - b1) / (int) (k * 2);

        int index1 = length - 1 - (x1 + ClientBattleContext.HORIZONTAL_RADIUS) / ClientBattleContext.HORIZONTAL_RADIUS;
        int index2 = x2 / ClientBattleContext.HORIZONTAL_RADIUS - length + 1;

        cell.setY(index1);
        cell.setX(index2);

        return cell;
    }

}
