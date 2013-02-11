package com.geargames.regolith;

import com.geargames.common.util.Lock;
import com.geargames.common.Graphics;
import com.geargames.regolith.app.Event;
import com.geargames.regolith.app.MELock;
import com.geargames.regolith.app.Render;
import com.geargames.regolith.awt.components.DrawablePPanel;
import com.geargames.regolith.awt.components.PPanelSingletonFabric;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.*;
import com.geargames.regolith.units.ClientBattleHelper;
import com.geargames.regolith.units.Unit;
import com.geargames.regolith.units.battle.ClientBorder;
import com.geargames.regolith.units.dictionaries.ClientWarriorCollection;
import com.geargames.regolith.units.map.BattleScreen;
import com.geargames.regolith.units.map.ExitZone;
import com.geargames.regolith.units.map.finder.ProjectionFinder;
import com.geargames.regolith.units.map.finder.ReverseProjectionFinder;
import com.geargames.regolith.units.map.verifier.CubeBorderCorrector;

import java.util.Vector;


public class Game {
    private static Lock startLock = new MELock();
    private PPanelSingletonFabric panels;

    private BattleScreen battleMap;
    private Render render;

    public Render getRender() {
        return render;
    }

    public void create(Render render) {
        panels = PPanelSingletonFabric.getInstance();
        panels.initiate(render);
        panels.show(panels.getMainMenu());
        panels.show(panels.getLeft());
/*
        panels.show(panels.getHeadline());
*/
        panels.show(panels.getRight());
    }

    private static Game selfGame;

    public static Game getInstance() {
        if (selfGame == null) {
            startLock.lock();
            if (selfGame == null) {
                selfGame = new Game();
            }
            startLock.release();
        }
        return selfGame;
    }

    public void init_() {
        battleMap.initMap();
        battleMap.initAllies();
    }

    public void draw(Graphics graphics) {
//        battleMap.draw(graphics);


        panels.draw(graphics);
    }

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    public void event(int code, int param, int x, int y) {
        panels.event(code, param, x, y);
        if (battleMap != null) {
            battleMap.event(code, param, x, y);
        }
        switch (code) {
            case Event.EVENT_KEY_UP:
            case Event.EVENT_KEY_DOWN:
                break;
            case Event.EVENT_KEY_PRESSED:
            case Event.EVENT_KEY_REPEATED:
                break;
            case Event.EVENT_TOUCH_RELEASED:
                break;
        }
    }
}

