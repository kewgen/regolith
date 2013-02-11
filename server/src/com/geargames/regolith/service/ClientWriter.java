package com.geargames.regolith.service;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mikhail_Kutuzov
 *         created: 11.06.12  15:52
 */
public class ClientWriter {
    private ExecutorService messages;

    public ClientWriter(int dataOutputProcessorAmount) {
        messages = Executors.newFixedThreadPool(dataOutputProcessorAmount);
    }

    public void addMessageToClient(MessageToClient messageToClient){
        messages.execute(messageToClient);
    }
}
