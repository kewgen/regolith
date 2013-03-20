package com.geargames.regolith;

import com.geargames.regolith.managers.*;
import com.geargames.regolith.network.MessageLock;
import com.geargames.regolith.network.Network;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.units.Account;
import com.geargames.regolith.units.battle.Warrior;

/**
 * @author Mikhail_Kutuzov
 *         created: 25.05.12  12:56
 */
public class ClientConfiguration {
    private ClientBattleServiceManager battleServiceManager;
    private ClientBaseWarriorMarketManager baseWarriorMarketManager;
    private ClientCommonManager commonManager;
    private BaseConfiguration baseConfiguration;
    private BattleConfiguration battleConfiguration;
    private ClientBattleMarketManager battleMarketManager;
    private ClientBattleCreationManager battleCreationManager;
    private ClientBaseManager baseManager;

    private Network network;
    private Account account;
    private MicroByteBuffer answersBuffer;
    private MicroByteBuffer messageBuffer;
    private int maxErrorsAmount;
    private String server;
    private int port;
    private byte[] incomingMessage;
    private int outgoingMessageSize;
    private MessageLock messageLock;
    private Warrior[] baseWarriors;

    public ClientConfiguration() {
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

    private boolean ready;

    public MicroByteBuffer getAnswersBuffer() {
        return answersBuffer;
    }

    public void setAnswersBuffer(MicroByteBuffer answersBuffer) {
        this.answersBuffer = answersBuffer;
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

    public MessageLock getMessageLock() {
        return messageLock;
    }

    public void setMessageLock(MessageLock messageLock) {
        this.messageLock = messageLock;
    }
}
