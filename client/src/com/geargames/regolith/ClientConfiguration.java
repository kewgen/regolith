package com.geargames.regolith;

import com.geargames.common.network.MessageDispatcher;
import com.geargames.common.network.Network;
import com.geargames.regolith.managers.*;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.Login;
import com.geargames.regolith.units.battle.Battle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.map.finder.ProjectionFinder;
import com.geargames.regolith.units.map.finder.ReverseProjectionFinder;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 25.05.12
 */
public class ClientConfiguration {
    private BaseConfiguration baseConfiguration;
    private BattleConfiguration battleConfiguration;

    private ClientBattleContext battleContext;

    private ClientBattleServiceManager battleServiceManager;
    private ClientBaseWarriorMarketManager baseWarriorMarketManager;
    private ClientCommonManager commonManager;
    private ClientBattleMarketManager battleMarketManager;
    private ClientBattleCreationManager battleCreationManager;
    private ClientBaseManager baseManager;

    private Login login;
    private Network network;
    private Account account;
    private Battle battle;
    private MicroByteBuffer messageBuffer;
    private int maxErrorsAmount;
    private String server;
    private int port;
    private byte[] incomingMessage;
    private int outgoingMessageSize;
    private Warrior[] baseWarriors;
    private MessageDispatcher messageDispatcher;
    private boolean ready;
    private ReverseProjectionFinder coordinateFinder;
    private ProjectionFinder cellFinder;

    public ReverseProjectionFinder getCoordinateFinder() {
        return coordinateFinder;
    }

    public void setCoordinateFinder(ReverseProjectionFinder coordinateFinder) {
        this.coordinateFinder = coordinateFinder;
    }

    public ProjectionFinder getCellFinder() {
        return cellFinder;
    }

    public void setCellFinder(ProjectionFinder cellFinder) {
        this.cellFinder = cellFinder;
    }

    public ClientConfiguration() {
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public BattleConfiguration getBattleConfiguration() {
        return battleConfiguration;
    }

    public void setBattleConfiguration(BattleConfiguration battleConfiguration) {
        this.battleConfiguration = battleConfiguration;
    }

    public ClientBattleServiceManager getBattleServiceManager() {
        return battleServiceManager;
    }

    public void setBattleServiceManager(ClientBattleServiceManager battleServiceManager) {
        this.battleServiceManager = battleServiceManager;
    }

    public ClientBaseWarriorMarketManager getBaseWarriorMarketManager() {
        return baseWarriorMarketManager;
    }

    public void setBaseWarriorMarketManager(ClientBaseWarriorMarketManager baseWarriorMarketManager) {
        this.baseWarriorMarketManager = baseWarriorMarketManager;
    }

    public Warrior[] getBaseWarriors() {
        return baseWarriors;
    }

    public void setBaseWarriors(Warrior[] baseWarriors) {
        this.baseWarriors = baseWarriors;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public ClientBaseManager getBaseManager() {
        return baseManager;
    }

    public void setBaseManager(ClientBaseManager baseManager) {
        this.baseManager = baseManager;
    }

    public ClientBattleMarketManager getBattleMarketManager() {
        return battleMarketManager;
    }

    public void setBattleMarketManager(ClientBattleMarketManager battleMarketManager) {
        this.battleMarketManager = battleMarketManager;
    }

    public ClientBattleCreationManager getBattleCreationManager() {
        return battleCreationManager;
    }

    public void setBattleCreationManager(ClientBattleCreationManager battleCreationManager) {
        this.battleCreationManager = battleCreationManager;
    }

    public int getOutgoingMessageSize() {
        return outgoingMessageSize;
    }

    public void setOutgoingMessageSize(int outgoingMessageSize) {
        this.outgoingMessageSize = outgoingMessageSize;
    }

    public byte[] getIncomingMessage() {
        return incomingMessage;
    }

    public void setIncomingMessage(byte[] incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public MicroByteBuffer getMessageBuffer() {
        return messageBuffer;
    }

    public void setMessageBuffer(MicroByteBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    public int getMaxErrorsAmount() {
        return maxErrorsAmount;
    }

    public void setMaxErrorsAmount(int maxErrorsAmount) {
        this.maxErrorsAmount = maxErrorsAmount;
    }

    public ClientCommonManager getCommonManager() {
        return commonManager;
    }

    public void setCommonManager(ClientCommonManager commonManager) {
        this.commonManager = commonManager;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public BaseConfiguration getBaseConfiguration() {
        return baseConfiguration;
    }

    public void setBaseConfiguration(BaseConfiguration baseConfiguration) {
        this.baseConfiguration = baseConfiguration;
    }

    public ClientBattleContext getBattleContext() {
        return battleContext;
    }

    public void setBattleContext(ClientBattleContext battleContext) {
        this.battleContext = battleContext;
    }

}
