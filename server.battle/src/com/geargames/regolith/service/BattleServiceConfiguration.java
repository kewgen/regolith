package com.geargames.regolith.service;

import com.geargames.regolith.RegolithConfiguration;

import javax.xml.bind.annotation.*;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "serverConfiguration")
public class BattleServiceConfiguration {
    @XmlTransient
    private BattleSchedulerService battleSchedulerService;
    @XmlTransient
    private RegolithConfiguration configuration;
    @XmlTransient
    private BattleServiceContext context;
    @XmlTransient
    private ClientWriter writer;
    @XmlTransient
    private ClientReader reader;
    @XmlElement
    private int actionTime;
    @XmlElement
    private int beginDelay;
    @XmlElement
    private int messageBufferSize;
    @XmlElement
    private int messageOutputDataProcessorsAmount;


    public int getMessageOutputDataProcessorsAmount() {
        return messageOutputDataProcessorsAmount;
    }

    public void setMessageOutputDataProcessorsAmount(int messageOutputDataProcessorsAmount) {
        this.messageOutputDataProcessorsAmount = messageOutputDataProcessorsAmount;
    }

    public int getMessageBufferSize() {
        return messageBufferSize;
    }

    public void setMessageBufferSize(int messageBufferSize) {
        this.messageBufferSize = messageBufferSize;
    }

    public int getActionTime() {
        return actionTime;
    }

    public void setActionTime(int actionTime) {
        this.actionTime = actionTime;
    }

    public int getBeginDelay() {
        return beginDelay;
    }

    public void setBeginDelay(int beginDelay) {
        this.beginDelay = beginDelay;
    }

    public BattleSchedulerService getBattleSchedulerService() {
        return battleSchedulerService;
    }

    public void setBattleSchedulerService(BattleSchedulerService battleSchedulerService) {
        this.battleSchedulerService = battleSchedulerService;
    }

    public RegolithConfiguration getRegolithConfiguration() {
        return configuration;
    }

    public void setRegolithConfiguration(RegolithConfiguration configuration) {
        this.configuration = configuration;
    }

    public BattleServiceContext getContext() {
        return context;
    }

    public void setContext(BattleServiceContext context) {
        this.context = context;
    }

    public ClientWriter getWriter() {
        return writer;
    }

    public void setWriter(ClientWriter writer) {
        this.writer = writer;
    }

    public ClientReader getReader() {
        return reader;
    }

    public void setReader(ClientReader reader) {
        this.reader = reader;
    }
}
