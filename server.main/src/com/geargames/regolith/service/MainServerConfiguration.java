package com.geargames.regolith.service;

import com.geargames.regolith.RegolithConfiguration;
import com.geargames.regolith.managers.*;
import org.hibernate.SessionFactory;

import javax.xml.bind.annotation.*;

/**
 * User: mkutuzov
 * Date: 09.02.12
 * Time: 11:51
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "serverConfiguration")
public class MainServerConfiguration {
    @XmlTransient
    private ClientReader reader;
    @XmlTransient
    private ClientWriter writer;
    @XmlTransient
    private ServerBaseWarriorMarketManager baseWarriorMarketManager;
    @XmlTransient
    private RegolithConfiguration regolithConfiguration;
    @XmlTransient
    private ServerCommonManager commonManager;
    @XmlTransient
    private ServerBattleMarketManager battleMarketManager;
    @XmlTransient
    private ServerTrainingBattleCreationManager battleCreationManager;
    @XmlTransient
    private ServerContext serverContext;
    @XmlTransient
    private SessionFactory sessionFactory;
    @XmlTransient
    private BrowseBattlesSchedulerService browseBattlesSchedulerService;

    @XmlElement
    private int baseWarriorsMarketRevision;
    @XmlElement
    private short battleRevision;
    @XmlElement
    private short baseRevision;
    @XmlElement
    private int messageBufferSize;
    @XmlElement
    private int messageOutputDataProcessorsAmount;
    @XmlElement
    private int port;
    @XmlElement
    private String imageDirectory;
    @XmlElement
    private int browseBattlesTimeInterval;

    public BrowseBattlesSchedulerService getBrowseBattlesSchedulerService() {
        return browseBattlesSchedulerService;
    }

    public void setBrowseBattlesSchedulerService(BrowseBattlesSchedulerService browseBattlesSchedulerService) {
        this.browseBattlesSchedulerService = browseBattlesSchedulerService;
    }

    public int getBrowseBattlesTimeInterval() {
        return browseBattlesTimeInterval;
    }

    public void setBrowseBattlesTimeInterval(int browseBattlesTimeInterval) {
        this.browseBattlesTimeInterval = browseBattlesTimeInterval;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }

    public ClientReader getReader() {
        return reader;
    }

    public void setReader(ClientReader reader) {
        this.reader = reader;
    }

    public ClientWriter getWriter() {
        return writer;
    }

    public void setWriter(ClientWriter writer) {
        this.writer = writer;
    }

    public int getBaseWarriorsMarketRevision() {
        return baseWarriorsMarketRevision;
    }

    public void setBaseWarriorsMarketRevision(int baseWarriorsMarketRevision) {
        this.baseWarriorsMarketRevision = baseWarriorsMarketRevision;
    }

    public ServerBaseWarriorMarketManager getBaseWarriorMarketManager() {
        return baseWarriorMarketManager;
    }

    public void setBaseWarriorMarketManager(ServerBaseWarriorMarketManager baseWarriorMarketManager) {
        this.baseWarriorMarketManager = baseWarriorMarketManager;
    }

    public ServerTrainingBattleCreationManager getBattleCreationManager() {
        return battleCreationManager;
    }

    public void setBattleCreationManager(ServerTrainingBattleCreationManager battleCreationManager) {
        this.battleCreationManager = battleCreationManager;
    }

    public ServerBattleMarketManager getBattleMarketManager() {
        return battleMarketManager;
    }

    public void setBattleMarketManager(ServerBattleMarketManager battleMarketManager) {
        this.battleMarketManager = battleMarketManager;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMessageOutputDataProcessorAmount() {
        return messageOutputDataProcessorsAmount;
    }

    public void setMessageOutputDataProcessorsAmount(int messageOutputDataProcessorsAmount) {
        this.messageOutputDataProcessorsAmount = messageOutputDataProcessorsAmount;
    }

    public short getBattleRevision() {
        return battleRevision;
    }

    public void setBattleRevision(short battle) {
        this.battleRevision = battle;
    }

    public short getBaseRevision() {
        return baseRevision;
    }

    public void setBaseRevision(short base) {
        this.baseRevision = base;
    }


    public RegolithConfiguration getRegolithConfiguration() {
        return regolithConfiguration;
    }

    public void setRegolithConfiguration(RegolithConfiguration regolithConfiguration) {
        this.regolithConfiguration = regolithConfiguration;
    }

    public ServerContext getServerContext() {
        return serverContext;
    }

    public void setServerContext(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    public ServerCommonManager getCommonManager() {
        return commonManager;
    }

    public void setCommonManager(ServerCommonManager commonManager) {
        this.commonManager = commonManager;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int getMessageBufferSize() {
        return messageBufferSize;
    }

    public void setMessageBufferSize(int messageBufferSize) {
        this.messageBufferSize = messageBufferSize;
    }
}
