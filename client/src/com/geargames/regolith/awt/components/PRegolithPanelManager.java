package com.geargames.regolith.awt.components;

import com.geargames.awt.*;
import com.geargames.common.Render;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.application.Graph;
import com.geargames.regolith.awt.components.battles.PBattlesPanel;
import com.geargames.regolith.awt.components.battles.PBattlesPanelWindow;
import com.geargames.regolith.awt.components.battles.PWarriorInformation;
import com.geargames.regolith.awt.components.selectMaps.PSelectMapPanel;
import com.geargames.regolith.awt.components.main.PHeadlinePanel;
import com.geargames.regolith.awt.components.main.PLeftPanel;
import com.geargames.regolith.awt.components.main.PMainMenuPanel;
import com.geargames.regolith.awt.components.main.PMoneyRegolithPanel;
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

public class PRegolithPanelManager extends PPanelManager {
    private static PRegolithPanelManager instance;

    private DrawablePPanel left;
    private DrawablePPanel headline;
    private DrawablePPanel right;

    private DrawablePPanel weaponFromStoreHouse;
    private DrawablePPanel projectileFromStoreHouse;
    private DrawablePPanel armorFromStoreHouse;
    private DrawablePPanel medikitFromStoreHouse;

    private DrawablePPanel armorFromWarrior;
    private DrawablePPanel weaponFromWarrior;

    private DrawablePPanel armorFromBag;
    private DrawablePPanel projectileFromBag;
    private DrawablePPanel weaponFromBag;
    private DrawablePPanel medikitFromBag;

    private DrawablePPanel warrior;
    private DrawablePPanel mainMenu;
    private DrawablePPanel warriorInfo;
    private DrawablePPanel battleCreate;
    private DrawablePPanel selectMap;
    private DrawablePPanel selectWarriors;
    private DrawablePPanel battlesWindow;

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
        warriorInfo = new DefaultDrawablePPanel();
        warriorInfo.setElement(new PWarriorInformation(render.getObject(Graph.PAN_FIGHTER_INFO)));

        left = new DefaultDrawablePPanel();
        left.setAnchor(Anchors.TOP_LEFT_ANCHOR);
        left.setElement(new PLeftPanel(render.getObject(Graph.PAN_LEFT)));

        headline = new DefaultDrawablePPanel();
        headline.setAnchor(Anchors.CENTER_ANCHOR);
        headline.setElement(new PHeadlinePanel(render.getObject(Graph.PAN_CENTER)));

        right = new DefaultDrawablePPanel();
        right.setAnchor(Anchors.TOP_RIGHT_ANCHOR);
        right.setElement(new PMoneyRegolithPanel(render.getObject(Graph.PAN_RIGHT)));

        PObject panelWeaponSelect = render.getObject(Graph.PAN_WEAPON_SELECT);
        PWeaponFromStoreHousePanel weaponFromStoreHousePanel = new PWeaponFromStoreHousePanel(panelWeaponSelect);
        weaponFromStoreHouse = new DefaultDrawablePPanel();
        weaponFromStoreHousePanel.setVisible(true);
        weaponFromStoreHouse.setElement(weaponFromStoreHousePanel);

        PProjectileFromStoreHousePanel projectileFromStoreHousePanel = new PProjectileFromStoreHousePanel(panelWeaponSelect);
        projectileFromStoreHouse = new DefaultDrawablePPanel();
        projectileFromStoreHousePanel.setVisible(true);
        projectileFromStoreHouse.setElement(projectileFromStoreHousePanel);

        PArmorFromStoreHousePanel armorFromStoreHousePanel = new PArmorFromStoreHousePanel(panelWeaponSelect);
        armorFromStoreHouse = new DefaultDrawablePPanel();
        armorFromStoreHousePanel.setVisible(true);
        armorFromStoreHouse.setElement(armorFromStoreHousePanel);

        PMedikitFromStoreHousePanel medikitFromStoreHousePanel = new PMedikitFromStoreHousePanel(panelWeaponSelect);
        medikitFromStoreHouse = new DefaultDrawablePPanel();
        medikitFromStoreHousePanel.setVisible(true);
        medikitFromStoreHouse.setElement(medikitFromStoreHousePanel);

        PArmorFromWarriorPanel armorFromWarriorPanel = new PArmorFromWarriorPanel(panelWeaponSelect);
        armorFromWarrior = new DefaultDrawablePPanel();
        armorFromWarriorPanel.setVisible(true);
        armorFromWarrior.setElement(armorFromWarriorPanel);

        PWeaponFromWarriorPanel weaponFromWarriorPanel = new PWeaponFromWarriorPanel(panelWeaponSelect);
        weaponFromWarrior = new DefaultDrawablePPanel();
        weaponFromWarriorPanel.setVisible(true);
        weaponFromWarrior.setElement(weaponFromWarriorPanel);

        PArmorFromBagPanel armorFromBagPanel = new PArmorFromBagPanel(panelWeaponSelect);
        armorFromBag = new DefaultDrawablePPanel();
        armorFromBagPanel.setVisible(true);
        armorFromBag.setElement(armorFromBagPanel);

        PProjectileFromBagPanel projectileFromBagPanel = new PProjectileFromBagPanel(panelWeaponSelect);
        projectileFromBag = new DefaultDrawablePPanel();
        projectileFromBagPanel.setVisible(true);
        projectileFromBag.setElement(projectileFromBagPanel);

        PWeaponFromBagPanel weaponFromBagPanel = new PWeaponFromBagPanel(panelWeaponSelect);
        weaponFromBag = new DefaultDrawablePPanel();
        weaponFromBagPanel.setVisible(true);
        weaponFromBag.setElement(weaponFromBagPanel);

        PMedikitFromBagPanel medikitFromBagPanel = new PMedikitFromBagPanel(panelWeaponSelect);
        medikitFromBag = new DefaultDrawablePPanel();
        medikitFromBagPanel.setVisible(true);
        medikitFromBag.setElement(medikitFromBagPanel);

        PWarriorPanel warriorPanel = new PWarriorPanel(render.getObject(Graph.PAN_FIGHTER));
        warrior = new WarriorDrawablePPanel();
        warrior.setAnchor(Anchors.CENTER_ANCHOR);
        warrior.setElement(warriorPanel);

        PMainMenuPanel mainMenuPanel = new PMainMenuPanel(render.getObject(Graph.PAN_MAIN_MENU));
        mainMenu = new DefaultDrawablePPanel();
        mainMenu.setAnchor(Anchors.CENTER_ANCHOR);
        mainMenu.setElement(mainMenuPanel);

        PBattleCreatePanel battleCreatePanel = new PBattleCreatePanel(render.getObject(Graph.PAN_BATTLE_CREATE));
        battleCreate = new DefaultDrawablePPanel();
        battleCreate.setAnchor(Anchors.CENTER_ANCHOR);
        battleCreate.setElement(battleCreatePanel);

        PSelectMapPanel selectMapPanel = new PSelectMapPanel(render.getObject(Graph.PAN_MAP_SELECT));
        selectMap = new DefaultDrawablePPanel();
        selectMap.setAnchor(Anchors.CENTER_ANCHOR);
        selectMap.setElement(selectMapPanel);

        PSelectWarriorsPanel selectWarriorsPanel = new PSelectWarriorsPanel(render.getObject(Graph.PAN_FIGHTER_SELECT));
        selectWarriors = new DefaultDrawablePPanel();
        selectWarriors.setAnchor(Anchors.CENTER_ANCHOR);
        selectWarriors.setElement(selectWarriorsPanel);

        PBattlesPanel battlesPanel = new PBattlesPanel(render.getObject(Graph.PAN_BATTLE_LIST));
        battlesWindow = new PBattlesPanelWindow();
        battlesWindow.setAnchor(Anchors.CENTER_ANCHOR);
        battlesWindow.setElement(battlesPanel);
    }

    public DrawablePPanel getBattlesWindow() {
        return battlesWindow;
    }

    public DrawablePPanel getWarriorInfo() {
        return warriorInfo;
    }

    public DrawablePPanel getWeaponFromStoreHouse() {
        return weaponFromStoreHouse;
    }

    public DrawablePPanel getProjectileFromStoreHouse() {
        return projectileFromStoreHouse;
    }

    public DrawablePPanel getArmorFromStoreHouse() {
        return armorFromStoreHouse;
    }

    public DrawablePPanel getMedikitFromStoreHouse() {
        return medikitFromStoreHouse;
    }

    public DrawablePPanel getArmorFromWarrior() {
        return armorFromWarrior;
    }

    public DrawablePPanel getWeaponFromWarrior() {
        return weaponFromWarrior;
    }

    public DrawablePPanel getArmorFromBag() {
        return armorFromBag;
    }

    public DrawablePPanel getProjectileFromBag() {
        return projectileFromBag;
    }

    public DrawablePPanel getWeaponFromBag() {
        return weaponFromBag;
    }

    public DrawablePPanel getMedikitFromBag() {
        return medikitFromBag;
    }

    public DrawablePPanel getWarrior() {
        return warrior;
    }

    public DrawablePPanel getMainMenu() {
        return mainMenu;
    }

    public DrawablePPanel getLeft() {
        return left;
    }

    public DrawablePPanel getHeadline() {
        return headline;
    }

    public DrawablePPanel getRight() {
        return right;
    }

    public DrawablePPanel getBattleCreate() {
        return battleCreate;
    }

    public PBattleCreatePanel getBattleCreatePanel() {
        return (PBattleCreatePanel) battleCreate.getElement();
    }

    public DrawablePPanel getSelectMap() {
        return selectMap;
    }

    public PSelectMapPanel getSelectMapPanel() {
        return (PSelectMapPanel) selectMap.getElement();
    }

    public DrawablePPanel getSelectWarriors() {
        return selectWarriors;
    }

}
