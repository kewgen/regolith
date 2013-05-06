package com.geargames.regolith;

import com.geargames.regolith.helpers.ServerBattleHelper;
import com.geargames.regolith.units.map.BattleCell;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * User: mkutuzov
 * Date: 31.05.12
 */
public class SimpleSerialize {
    @Test
    public void test() throws IOException, ClassNotFoundException {
        BattleCell[][] cells = ServerBattleHelper.createBattleMap(10).getCells();
        byte[] bytes = ServerBattleHelper.serializeBattleCells(cells);
        BattleCell[][] dcells = ServerBattleHelper.deserializeBattleCells(bytes);
        Assert.assertEquals(dcells.length, 10);
    }
}
