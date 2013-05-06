package com.geargames.regolith.serializers.answers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SimpleDeserializer;
import com.geargames.common.serialization.SimpleSerializer;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.BattleMapHelper;
import com.geargames.regolith.helpers.ClientBattleHelper;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.dictionaries.ClientHumanElementCollection;
import com.geargames.regolith.units.map.BattleCell;
import com.geargames.regolith.units.map.BattleMap;
import com.geargames.regolith.units.map.ClientHumanElement;

/**
 * Users: mvkutuzov, abarakov
 * Date: 27.04.13
 * Time: 16:41
 * Класс отвечает за пометку точек пути вражеского бойца на карте клиентского приложения.
 * Работает в предположении, что до первого попадения в зону видимости клиента о вражеском бойце не шлётся никаких
 * данных.
 * итак: первая и последняя координата пути - реальные координаты вражеского бойца(точки где он был засечён клиентом)
 * между реальных координат существуют пробелы которые заполняются прямой линией(чистый произвол)
 */
public class ClientMoveEnemyAnswer extends ClientDeSerializedMessage {
    private ClientHumanElement enemy;

    private Battle battle;

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public ClientHumanElement getEnemy() {
        return enemy;
    }

    @Override
    public void deSerialize(MicroByteBuffer buffer) throws Exception {
//        int  allianceId = SimpleDeserializer.deserializeInt(buffer);
//        BattleAlliance alliance = ClientBattleHelper.findAllianceById(battle, allianceId);
//        int groupId = SimpleDeserializer.deserializeInt(buffer);
//        BattleGroup group = ClientBattleHelper.findBattleGroupInAllianceById(alliance, groupId);
        int enemyId = SimpleDeserializer.deserializeInt(buffer);
        ClientHumanElementCollection enemyUnits = PRegolithPanelManager.getInstance().getBattleScreen().getEnemyUnits();
        enemy = (ClientHumanElement) BattleMapHelper.getHumanElementByHumanId(enemyUnits, enemyId);
//        enemy = ClientBattleHelper.findWarriorInBattleGroup(group,warriorId);
        if (enemy == null) {
            throw new Exception();
        }
        BattleMap map = battle.getMap();
        BattleCell[][] cells = map.getCells();
        byte size = buffer.get();
        boolean isGap = false;
        int x0 = 0;
        int y0 = 0;
        for (int i = 0; i < size; i++) {
            int x = SimpleDeserializer.deserializeInt(buffer);
            int y = SimpleDeserializer.deserializeInt(buffer);
            if (x != SimpleSerializer.NULL_COORDINATE) {
                BattleMapHelper.setShortestPathCell(cells[x][y], enemy);
                if (isGap) {
                    isGap = false;
                    ClientBattleHelper.makeEnemyFakePath(x, y, x0, y0, cells, enemy);
                }
                x0 = x;
                y0 = y;
            } else {
                isGap = true;
            }
        }
    }

}
