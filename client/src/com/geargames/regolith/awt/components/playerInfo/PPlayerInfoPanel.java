package com.geargames.regolith.awt.components.playerInfo;

import com.geargames.awt.components.PPrototypeElement;
import com.geargames.awt.components.PSimpleLabel;
import com.geargames.awt.components.PTouchButton;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.NullRegion;
import com.geargames.regolith.application.PFontCollection;
import com.geargames.regolith.awt.components.PRegolithPanelManager;
import com.geargames.regolith.awt.components.PRootContentPanel;
import com.geargames.regolith.awt.components.common.PShowingHintLabel;
import com.geargames.regolith.localization.LocalizedStrings;
import com.geargames.regolith.network.ClientRequestHelper;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.BattleGroup;

/**
 * User: abarakov
 * Date: 10.04.13
 * Панель просмотра информации об игроке.
 */
public class PPlayerInfoPanel extends PRootContentPanel {

    private PAwardList awardList;
    private Account playerAccount;
    private BattleGroup battleGroup;

    private PPrototypeElement playerAvatar;
    private PShowingHintLabel labelPlayerName;
    private PShowingHintLabel labelPlayerLevel;
    private PShowingHintLabel labelPlayerScore;
    private PShowingHintLabel labelPlayerWins;
    private PShowingHintLabel labelPlayerLosses;
    private PShowingHintLabel labelPlayerClan;
    private PEvictButton buttonEvict;

    public PPlayerInfoPanel(PObject prototype) {
        super(prototype);
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
        switch (index.getSlot()) {
            case 1: {
                // Аватарка игрока
                playerAvatar = new PPrototypeElement();
                playerAvatar.setPrototype(index.getPrototype());
                playerAvatar.setRegion(NullRegion.instance);
                addPassiveChild(playerAvatar, index);
                break;
            }
            case 2: {
                // Имя игрока
                labelPlayerName = new PShowingHintLabel((PObject) index.getPrototype());
                labelPlayerName.setText("EMPTY");
                labelPlayerName.setFont(PFontCollection.getFontLabel());
                labelPlayerName.setHint(LocalizedStrings.PLAYER_INFO_HINT_PLAYER_NAME);
                addActiveChild(labelPlayerName, index);
                break;
            }
            case 3: {
                // Уровень игрока
                labelPlayerLevel = new PShowingHintLabel((PObject) index.getPrototype());
                labelPlayerLevel.setText("EMPTY");
                labelPlayerLevel.setFont(PFontCollection.getFontLabel());
                labelPlayerLevel.setHint(LocalizedStrings.PLAYER_INFO_HINT_PLAYER_LEVEL);
                addActiveChild(labelPlayerLevel, index);
                break;
            }
            case 4: {
                // Количество очков у игрока
                labelPlayerScore = new PShowingHintLabel((PObject) index.getPrototype());
                labelPlayerScore.setText("EMPTY");
                labelPlayerScore.setFont(PFontCollection.getFontLabel());
                labelPlayerScore.setHint(LocalizedStrings.PLAYER_INFO_HINT_PLAYER_SCORE);
                addActiveChild(labelPlayerScore, index);
                break;
            }
            case 5: {
                // Количество попед
                labelPlayerWins = new PShowingHintLabel((PObject) index.getPrototype());
                labelPlayerWins.setText("EMPTY");
                labelPlayerWins.setFont(PFontCollection.getFontLabel());
                labelPlayerWins.setHint(LocalizedStrings.PLAYER_INFO_HINT_PLAYER_WINS);
                addActiveChild(labelPlayerWins, index);
                break;
            }
            case 6: {
                // Количество потерянных бойцов
                labelPlayerLosses = new PShowingHintLabel((PObject) index.getPrototype());
                labelPlayerLosses.setText("EMPTY");
                labelPlayerLosses.setFont(PFontCollection.getFontLabel());
                labelPlayerLosses.setHint(LocalizedStrings.PLAYER_INFO_HINT_PLAYER_LOSSES);
                addActiveChild(labelPlayerLosses, index);
                break;
            }
            case 7: {
                // Клан, в который входит игрок
                labelPlayerClan = new PShowingHintLabel((PObject) index.getPrototype());
                labelPlayerClan.setText("EMPTY");
                labelPlayerClan.setFont(PFontCollection.getFontLabel());
                labelPlayerClan.setHint(LocalizedStrings.PLAYER_INFO_HINT_PLAYER_CLAN);
                addActiveChild(labelPlayerClan, index);
                break;
            }
            case 8: {
                // Список наград
                awardList = new PAwardList((PObject) index.getPrototype());
                addActiveChild(awardList, index);
                break;
            }
            case 9:
                // Кнопка OtherInfo
                PTouchButton buttonOtherInfo = new PTouchButton((PObject) index.getPrototype());
                addActiveChild(buttonOtherInfo, index);
                break;
            case 10:
                // Кнопка "Написать сообщение"
                PTouchButton buttonChat = new PTouchButton((PObject) index.getPrototype());
                addActiveChild(buttonChat, index);
                break;
            case 11:
                // Кнопка Evict
                buttonEvict = new PEvictButton((PObject) index.getPrototype());
//                buttonEvict.setText(LocalizedStrings.PLAYER_INFO_EVICT_BUTTON);
//                buttonEvict.setFont(PFontCollection.getFontButtonCaption());
                addActiveChild(buttonEvict, index);
                break;
            case 109: {
                // Заголовок окна
                PSimpleLabel labelTitle = new PSimpleLabel(index);
                labelTitle.setText(LocalizedStrings.PLAYER_INFO_PANEL_TITLE);
                labelTitle.setFont(PFontCollection.getFontFormTitle());
                addPassiveChild(labelTitle, index);
                break;
            }
        }
    }

    @Override
    public void onShow() {
        Debug.debug("Dialog 'Player info': onShow");
    }

    @Override
    public void onHide() {
        Debug.debug("Dialog 'Player info': onHide");
    }

    public void showPanel(BattleGroup battleGroup, boolean evictAllowed/*, DrawablePPanel callerPanel*/) {
        Debug.debug("Dialog 'Player info'");
        this.battleGroup = battleGroup;
        this.playerAccount = battleGroup.getAccount();

        //playerAvatar.setAvatar; //todo: изменить аватар игрока
        labelPlayerName.setText(playerAccount.getName());
//        labelPlayerLevel.setText("0");
        labelPlayerScore.setText(String.valueOf(playerAccount.getExperience()));
//        labelPlayerWins.setText(playerAccount.getStatistics.getWinCount());
//        labelPlayerLosses.setText(playerAccount.getStatistics.getLossCount());
        if (playerAccount.getClan() == null) {
//            Debug.error("Null Clan"); //todo: NullPointerException
            labelPlayerClan.setText("EMPTY");
        } else {
            labelPlayerClan.setText(playerAccount.getClan().getName());
        }

        buttonEvict.setVisible(evictAllowed);

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
//        panelManager.hide(callerPanel);
        panelManager.showModal(panelManager.getPlayerInfoWindow());
    }

    /**
     * Обработчик нажатия на кнопку Evict.
     */
    public void onEvictButtonClick() {
//        ClientConfiguration clientConfiguration = ClientConfigurationFactory.getConfiguration();
//        ClientBattleCreationManager battleCreationManager = clientConfiguration.getBattleCreationManager();
//        ClientBattleMarketManager battleMarketManager = clientConfiguration.getBattleMarketManager();

        if (!ClientRequestHelper.evictAccountFromBattleGroup(battleGroup, this, LocalizedStrings.BATTLES_MSG_EVICT_ACCOUNT_EXCEPTION)) {
            //todo: А если игрок уже покинул боевую группу? следует вывести об этом сообщение, или просто проигнорировать.
//            return;
        }

        PRegolithPanelManager panelManager = PRegolithPanelManager.getInstance();
        panelManager.getBattlesPanel().doUpdateButtonAccount(battleGroup, false);

        panelManager.hide(panelManager.getPlayerInfoWindow());
////        panelManager.show(panelManager.getMainMenu());
//        panelManager.getBattlesPanel().showPanel(battleGroup.getAlliance().getBattle(), panelManager.getSelectWarriorsWindow(), true);
    }

}
