package com.geargames.regolith;

import com.geargames.regolith.units.BattleHelper;
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
        BattleCell[][] cells = BattleHelper.createBattleMap(10).getCells();
        byte[] bytes = BattleHelper.serializeBattleCells(cells);
        BattleCell[][] dcells = BattleHelper.deserializeBattleCells(bytes);
        Assert.assertEquals(dcells.length, 10);
    }
}
