package com.geargames.regolith.awt.components;

import com.geargames.awt.*;
import com.geargames.common.Render;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.Graph;
import com.geargames.regolith.awt.components.battles.PBattleServiceLoginWait;
import com.geargames.regolith.awt.components.battles.PBattlesPanel;
import com.geargames.regolith.awt.components.main.*;
import com.geargames.regolith.awt.components.playerInfo.PPlayerInfoPanel;
import com.geargames.regolith.awt.components.selectMap.PSelectMapPanel;
import com.geargames.regolith.awt.components.battleCreate.PBattleCreatePanel;
import com.geargames.regolith.awt.components.selectWarriors.PSelectWarriorsPanel;
import com.geargames.regolith.awt.components.warrior.PWarriorPanel;
import com.geargames.regolith.awt.components.warrior.WarriorDrawablePPanel;
import com.geargames.regolith.awt.components.warrior.exchange.bag.PArmorFromBagPanel;
import com.geargames.regolith.awt.components.warrior.exchange.bag.PMedikitFromBagPanel;
import com.geargames.regolith.awt.components.warrior.exchange.bag.PProjectileFromBagPanel;
import com.geargames.regolith.awt.components.warrior.exchange.bag.PWeaponFromBagPanel;
import com.geargames.regolith.awt.components.warrior.exchange.storehouse.PArmorFromStoreHousePanel;
import com.geargames.regolith.awt.components.warrior.exchange.storehouse.PMedikitFromStoreHousePanel;
import com.geargames.regolith.awt.components.warrior.exchange.storehouse.PProjectileFromStoreHousePanel;
import com.geargames.regolith.awt.components.warrior.exchange.storehouse.PWeaponFromStoreHousePanel;
import com.geargames.regolith.awt.components.warrior.exchange.warrior.PArmorFromWarriorPanel;
import com.geargames.regolith.awt.components.warrior.exchange.warrior.PWeaponFromWarriorPanel;
import com.geargames.regolith.units.BattleScreen;
import com.geargames.regolith.units.MainScreen;
import com.geargames.regolith.units.map.finder.ProjectionFinder;
import com.geargames.regolith.units.map.finder.ReverseProjectionFinder;
import com.geargames.regolith.units.map.verifier.CubeBorderCorrector;

public class PRegolithPanelManager extends PPanelManager {
    private static PRegolithPanelManager instance;

    private DefaultDrawablePPanel left;
    private DefaultDrawablePPanel headline;
    private DefaultDrawablePPanel right;

    private DefaultDrawablePPanel weaponFromStoreHouse;
    private DefaultDrawablePPanel projectileFromStoreHouse;
    private DefaultDrawablePPanel armorFromStoreHouse;
    private DefaultDrawablePPanel medikitFromStoreHouse;

    private DefaultDrawablePPanel armorFromWarrior;
    private DefaultDrawablePPanel weaponFromWarrior;

    private DefaultDrawablePPanel armorFromBag;
    private DefaultDrawablePPanel projectileFromBag;
    private DefaultDrawablePPanel weaponFromBag;
    private DefaultDrawablePPanel medikitFromBag;

    private DefaultDrawablePPanel warrior;
    private DefaultDrawablePPanel mainMenu;

    private DefaultDrawablePPanel battlesWindow;
    private DefaultDrawablePPanel battleCreateWindow;
    private DefaultDrawablePPanel selectMapWindow;
    private DefaultDrawablePPanel selectWarriorsWindow;
    private DefaultDrawablePPanel playerInfoWindow;

    private MainScreen mainScreen;
    private BattleScreen battleScreen;

    private DefaultDrawablePPanel fontTestWindow;
    private TopDrawablePPanel loginBattleServiceWait;

    private PRegolithPanelManager() {
    }

    public static PRegolithPanelManager getInstance() {
        if (instance == null) {
            instance = new PRegolithPanelManager();
        }
        return instance;
    }

    /**
     * Настроить подчинённые окошки.
     *
     * @param render
     * @return
     */
    public void initiate(Render render) {
        mainScreen = new MainScreen();

        battleScreen = new BattleScreen();
        battleScreen.setCoordinateFinder(new ReverseProjectionFinder());
        battleScreen.setCellFinder(new ProjectionFinder());
        battleScreen.setCorrector(new CubeBorderCorrector());

        left = new TopDrawablePPanel();
        left.setAnchor(Anchors.TOP_LEFT_ANCHOR);
        left.setElement(new PLeftPanel(render.getObject(Graph.PAN_LEFT)));

        headline = new TopDrawablePPanel();
        headline.setAnchor(Anchors.TOP_CENTER_ANCHOR);
        headline.setElement(new PHeadlinePanel(render.getObject(Graph.PAN_CENTER)));

        right = new TopDrawablePPanel();
        right.setAnchor(Anchors.TOP_RIGHT_ANCHOR);
        right.setElement(new PMoneyRegolithPanel(render.getObject(Graph.PAN_RIGHT)));

        PObject panelWeaponSelect = render.getObject(Graph.PAN_WEAPON_SELECT);
        PWeaponFromStoreHousePanel weaponFromStoreHousePanel = new PWeaponFromStoreHousePanel(panelWeaponSelect);
        weaponFromStoreHouse = new MiddleDrawablePPanel();
        weaponFromStoreHousePanel.setVisible(true);
        weaponFromStoreHouse.setElement(weaponFromStoreHousePanel);
        weaponFromStoreHouse.setModalAutoClose(true);

        PProjectileFromStoreHousePanel projectileFromStoreHousePanel = new PProjectileFromStoreHousePanel(panelWeaponSelect);
        projectileFromStoreHouse = new MiddleDrawablePPanel();
        projectileFromStoreHousePanel.setVisible(true);
        projectileFromStoreHouse.setElement(projectileFromStoreHousePanel);
        projectileFromStoreHouse.setModalAutoClose(true);

        PArmorFromStoreHousePanel armorFromStoreHousePanel = new PArmorFromStoreHousePanel(panelWeaponSelect);
        armorFromStoreHouse = new MiddleDrawablePPanel();
        armorFromStoreHousePanel.setVisible(true);
        armorFromStoreHouse.setElement(armorFromStoreHousePanel);
        armorFromStoreHouse.setModalAutoClose(true);

        PMedikitFromStoreHousePanel medikitFromStoreHousePanel = new PMedikitFromStoreHousePanel(panelWeaponSelect);
        medikitFromStoreHouse = new MiddleDrawablePPanel();
        medikitFromStoreHousePanel.setVisible(true);
        medikitFromStoreHouse.setModalAutoClose(true);
        medikitFromStoreHouse.setElement(medikitFromStoreHousePanel);

        PArmorFromWarriorPanel armorFromWarriorPanel = new PArmorFromWarriorPanel(panelWeaponSelect);
        armorFromWarrior = new MiddleDrawablePPanel();
        armorFromWarrior.setModalAutoClose(true);
        armorFromWarriorPanel.setVisible(true);
        armorFromWarrior.setElement(armorFromWarriorPanel);

        PWeaponFromWarriorPanel weaponFromWarriorPanel = new PWeaponFromWarriorPanel(panelWeaponSelect);
        weaponFromWarrior = new MiddleDrawablePPanel();
        weaponFromWarrior.setModalAutoClose(true);
        weaponFromWarriorPanel.setVisible(true);
        weaponFromWarrior.setElement(weaponFromWarriorPanel);

        PArmorFromBagPanel armorFromBagPanel = new PArmorFromBagPanel(panelWeaponSelect);
        armorFromBag = new MiddleDrawablePPanel();
        armorFromBag.setModalAutoClose(true);
        armorFromBagPanel.setVisible(true);
        armorFromBag.setElement(armorFromBagPanel);

        PProjectileFromBagPanel projectileFromBagPanel = new PProjectileFromBagPanel(panelWeaponSelect);
        projectileFromBag = new MiddleDrawablePPanel();
        projectileFromBag.setModalAutoClose(true);
        projectileFromBagPanel.setVisible(true);
        projectileFromBag.setElement(projectileFromBagPanel);

        PWeaponFromBagPanel weaponFromBagPanel = new PWeaponFromBagPanel(panelWeaponSelect);
        weaponFromBag = new MiddleDrawablePPanel();
        weaponFromBag.setModalAutoClose(true);
        weaponFromBagPanel.setVisible(true);
        weaponFromBag.setElement(weaponFromBagPanel);

        PMedikitFromBagPanel medikitFromBagPanel = new PMedikitFromBagPanel(panelWeaponSelect);
        medikitFromBag = new MiddleDrawablePPanel();
        medikitFromBag.setModalAutoClose(true);
        medikitFromBagPanel.setVisible(true);
        medikitFromBag.setElement(medikitFromBagPanel);

        PWarriorPanel warriorPanel = new PWarriorPanel(render.getObject(Graph.PAN_FIGHTER));
        warrior = new WarriorDrawablePPanel();
        warrior.setAnchor(Anchors.CENTER_ANCHOR);
        warrior.setElement(warriorPanel);

        PMainMenuPanel mainMenuPanel = new PMainMenuPanel(render.getObject(Graph.PAN_MAIN_MENU));
        mainMenu = new MiddleDrawablePPanel();
        mainMenu.setAnchor(Anchors.CENTER_ANCHOR);
        mainMenu.setElement(mainMenuPanel);

        PBattlesPanel battlesPanel = new PBattlesPanel(render.getObject(Graph.PAN_BATTLE_LIST));
        battlesWindow = new MiddleDrawablePPanel();
        battlesWindow.setAnchor(Anchors.CENTER_ANCHOR);
        battlesWindow.setElement(battlesPanel);

        PBattleCreatePanel battleCreatePanel = new PBattleCreatePanel(render.getObject(Graph.PAN_BATTLE_CREATE));
        battleCreateWindow = new MiddleDrawablePPanel();
        battleCreateWindow.setAnchor(Anchors.CENTER_ANCHOR);
        battleCreateWindow.setElement(battleCreatePanel);

        PSelectMapPanel selectMapPanel = new PSelectMapPanel(render.getObject(Graph.PAN_MAP_SELECT));
        selectMapWindow = new MiddleDrawablePPanel();
        selectMapWindow.setAnchor(Anchors.CENTER_ANCHOR);
        selectMapWindow.setElement(selectMapPanel);

        PSelectWarriorsPanel selectWarriorsPanel = new PSelectWarriorsPanel(render.getObject(Graph.PAN_FIGHTER_SELECT));
        selectWarriorsWindow = new MiddleDrawablePPanel();
        selectWarriorsWindow.setAnchor(Anchors.CENTER_ANCHOR);
        selectWarriorsWindow.setElement(selectWarriorsPanel);

        PPlayerInfoPanel playerInfoPanel = new PPlayerInfoPanel(render.getObject(Graph.PAN_PLAYER_INFO));
        playerInfoWindow = new MiddleDrawablePPanel();
        playerInfoWindow.setModalAutoClose(true);
        playerInfoWindow.setAnchor(Anchors.CENTER_ANCHOR);
        playerInfoWindow.setElement(playerInfoPanel);

        PFontTestPanel fontTest = new PFontTestPanel(render.getObject(Graph.PAN_BATTLE_CREATE));
        fontTestWindow = new MiddleDrawablePPanel();
        fontTestWindow.setAnchor(Anchors.CENTER_ANCHOR);
        fontTestWindow.setElement(fontTest);

        PBattleServiceLoginWait startBattleWaitPanel = new PBattleServiceLoginWait(render.getObject(Graph.PAN_WAITING_SERVER));
        loginBattleServiceWait = new TopDrawablePPanel();
        loginBattleServiceWait.setAnchor(Anchors.CENTER_ANCHOR);
        loginBattleServiceWait.setElement(startBattleWaitPanel);
    }

    public TopDrawablePPanel getLoginBattleServiceWait() {
        return loginBattleServiceWait;
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public BattleScreen getBattleScreen() {
        return battleScreen;
    }

    public DefaultDrawablePPanel getWeaponFromStoreHouse() {
        return weaponFromStoreHouse;
    }

    public DefaultDrawablePPanel getProjectileFromStoreHouse() {
        return projectileFromStoreHouse;
    }

    public DefaultDrawablePPanel getArmorFromStoreHouse() {
        return armorFromStoreHouse;
    }

    public DefaultDrawablePPanel getMedikitFromStoreHouse() {
        return medikitFromStoreHouse;
    }

    public DefaultDrawablePPanel getArmorFromWarrior() {
        return armorFromWarrior;
    }

    public DefaultDrawablePPanel getWeaponFromWarrior() {
        return weaponFromWarrior;
    }

    public DefaultDrawablePPanel getArmorFromBag() {
        return armorFromBag;
    }

    public DefaultDrawablePPanel getProjectileFromBag() {
        return projectileFromBag;
    }

    public DefaultDrawablePPanel getWeaponFromBag() {
        return weaponFromBag;
    }

    public DefaultDrawablePPanel getMedikitFromBag() {
        return medikitFromBag;
    }

    public DefaultDrawablePPanel getWarrior() {
        return warrior;
    }

    public DefaultDrawablePPanel getMainMenu() {
        return mainMenu;
    }

    public DefaultDrawablePPanel getLeft() {
        return left;
    }

    public DefaultDrawablePPanel getHeadline() {
        return headline;
    }

    public DefaultDrawablePPanel getRight() {
        return right;
    }

    public DefaultDrawablePPanel getBattlesWindow() {
        return battlesWindow;
    }

    public PBattlesPanel getBattlesPanel() {
        return (PBattlesPanel) battlesWindow.getElement();
    }

    public DefaultDrawablePPanel getBattleCreateWindow() {
        return battleCreateWindow;
    }

    public PBattleCreatePanel getBattleCreatePanel() {
        return (PBattleCreatePanel) battleCreateWindow.getElement();
    }

    public DefaultDrawablePPanel getSelectMapWindow() {
        return selectMapWindow;
    }

    public PSelectMapPanel getSelectMapPanel() {
        return (PSelectMapPanel) selectMapWindow.getElement();
    }

    public DefaultDrawablePPanel getSelectWarriorsWindow() {
        return selectWarriorsWindow;
    }

    public PSelectWarriorsPanel getSelectWarriorsPanel() {
        return (PSelectWarriorsPanel) selectWarriorsWindow.getElement();
    }

    public DefaultDrawablePPanel getPlayerInfoWindow() {
        return playerInfoWindow;
    }

    public PPlayerInfoPanel getPlayerInfoPanel() {
        return (PPlayerInfoPanel) playerInfoWindow.getElement();
    }

    public DefaultDrawablePPanel getFontTestWindow() {
        return fontTestWindow;
    }

}
