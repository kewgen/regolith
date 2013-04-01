package com.geargames.regolith.awt.components.selectMap;

import com.geargames.awt.DrawablePPanel;
import com.geargames.awt.components.*;
import com.geargames.awt.utils.motions.ElasticInertMotionListener;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.helpers.BaseConfigurationHelper;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.managers.ClientBaseManager;
import com.geargames.regolith.managers.ClientBattleMarketManager;
import com.geargames.regolith.serializers.answers.ClientBattleMapAnswer;
import com.geargames.regolith.serializers.answers.ClientBattleMapListAnswer;
import com.geargames.regolith.serializers.answers.ClientConfirmationAnswer;
import com.geargames.regolith.serializers.answers.ClientListenToBattleAnswer;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.BattleType;
import com.geargames.regolith.units.map.BattleMap;

/**
 * User: abarakov
 * Date: 26.03.13
 * Панель просмотра доступных карт и выбора одной из них для битвы.
 */
// PBrowseMapsPanel
public class PSelectMapPanel extends PRootContentPanel {

    private BattleType battleType;
    private BattleMap selectedMap;
    private DrawablePPanel currentPanel;

    private PBattleMapList battleMapList;
    private PSimpleLabel mapDescription;

    public PSelectMapPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 1: {
                // Заголовок списка карт
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.BROWSE_MAPS_LIST_TITLE);
                labelTitle.setFont(PFontCollection.getFontListTitle());
                addPassiveChild(labelTitle, index);
                break;
            }
            case 2:
                // Список карт
                battleMapList = new PBattleMapList((PObject) index.getPrototype());
                addActiveChild(battleMapList, index);

                ElasticInertMotionListener motionListener = new ElasticInertMotionListener();
                battleMapList.setMotionListener(motionListener);
//                CenteredElasticInertMotionListener motionListener = new CenteredElasticInertMotionListener();
//                motionListener.setInstinctPosition(false);
//                battleMapList.setMotionListener(
//                        ScrollHelper.adjustHorizontalCenteredMenuMotionListener(
//                                motionListener,
//                                battleMapList.getDrawRegion(),
//                                battleMapList.getItemsAmount(),
//                                battleMapList.getItemSize(),
//                                battleMapList.getPrototype().getDrawRegion().getMinX()
//                        )
//                );
                break;
            case 5:
                // Название карты
                mapDescription = new PSimpleLabel(index);
                mapDescription.setFont(PFontCollection.getFontLabel());
                mapDescription.setText("<NAME>");
                addPassiveChild(mapDescription, index);
                break;

            // ----- Общие компоненты ----------------------------------------------------------------------------------

            case 3:
                // Кнопка Ok
                PButtonOk buttonOk = new PButtonOk((PObject) index.getPrototype());
                buttonOk.setText(LocalizedStrings.BUTTON_OK);
                buttonOk.setFont(PFontCollection.getFontButtonCaption());
                addActiveChild(buttonOk, index);
                break;
            case 109: {
                // Заголовок окна
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.BROWSE_MAPS_PANEL_TITLE);
                labelTitle.setFont(PFontCollection.getFontFormTitle());
                addPassiveChild(labelTitle, index);
                break;
            }
        }
    }

    public void setSelectedMap(BattleMap map) {
        selectedMap = map;
        mapDescription.setText(map.getName());
    }

    public void showPanel(int allianceAmount, int allianceSize, int groupSize, boolean isRandomMap, DrawablePPanel callerPanel) {
        Debug.debug("Dialog 'Select map'");

        Debug.debug("Find battle type...");
        try {
            battleType = BaseConfigurationHelper.findBattleTypeByArgs(allianceAmount, allianceSize, groupSize,
                    ClientConfigurationFactory.getConfiguration().getBaseConfiguration());
        } catch (Exception e) {
            Debug.critical("", e); //todo: Текст ошибки
            //todo: Сообщить пользователю об ошибке
            //todo: Перенести пользователя в одно из диалоговых окон, от куда он может продолжить игру
        }

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientBaseManager baseManager = clientConfiguration.getBaseManager();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();

        try {
            Debug.debug("The client go to the battle market...");
            ClientConfirmationAnswer confirm = baseManager.goBattleManager();
            if (!confirm.isConfirm()) {
                Debug.critical("The client could not go to the battle market");
            }

            if (isRandomMap) {
                Debug.debug("Browsing a random map...");
                ClientBattleMapAnswer battleMapAnswer = battleMarketManager.browseRandomBattleMap(battleType);
                selectedMap = battleMapAnswer.getBattleMap();
                currentPanel = callerPanel;
                createBattle();
            } else {
                Debug.debug("Browsing maps...");
                ClientBattleMapListAnswer battleMapListAnswer = battleMarketManager.browseBattleMaps(battleType);
                BattleMap[] battleMaps = battleMapListAnswer.getBattleMaps();
                Debug.debug("Received a list of maps (count = " + battleMaps.length + ")");
                if (battleMaps.length == 0) {
                    Debug.critical("There are no maps to use");
                }
                selectedMap = null;

                battleMapList.setMapItems(battleMaps);

                //todo: Выбрать по умолчанию первую карту, или предыдущую, которую юзер использовал в предыдущей создаваемой битве
                battleMapList.getItem(0).setChecked(true);
                battleMapList.getItem(0).onClick(); //todo: должно выполняться автоматически
                //setSelectedMap(battleMapList.getItem(0).getMap());

                PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
                currentPanel = panelManager.getSelectMap();
                panelManager.hide(callerPanel);
                panelManager.show(currentPanel);
            }
        } catch (Exception e) {
            Debug.critical("Could not get the map to create a battle", e);
        }
    }

    public void createBattle() {
        //todo: может вывести панельку, в которой сообщить пользователю о текущем состоянии обмена сообщениями между клиентов и сервером
        // Ожидаем ответа от игроков/сервера. Пожалуйста подождите.

        if (selectedMap == null) {
            Debug.critical("PSelectMapPanel.createBattle(): selectedMap == null");
            return;
        }
        if (currentPanel == null) {
            Debug.critical("PSelectMapPanel.createBattle(): currentPanel == null");
            return;
        }

        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();

        Debug.debug("Creating a battle (map id = " + selectedMap.getId() + ")...");

        ClientListenToBattleAnswer listenToBattleAnswer;
        try {
            listenToBattleAnswer = battleMarketManager.createBattle(selectedMap, battleType);
        } catch (Exception e) {
            Debug.critical("Failed to create the battle", e);
            return;
        }
        Battle battle = listenToBattleAnswer.getBattle();
        Debug.debug("The battle was created (battle id = " + battle.getId() + ")");
//        ObjectManager.getInstance().setClientBattle(battle);

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getSelectWarriorsPanel().showPanel(battle.getAlliances()[0].getAllies().get(0), currentPanel);
    }

    /**
     * Обработчик нажатия на кнопку Ok.
     */
    public void onButtonOkClick() {
        createBattle();
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

}
