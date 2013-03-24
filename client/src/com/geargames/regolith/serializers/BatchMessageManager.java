package com.geargames.regolith.serializers;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.util.ArrayList;
import com.geargames.regolith.ClientConfiguration;
import com.geargames.regolith.ClientConfigurationFactory;
import com.geargames.regolith.Packets;
import com.geargames.common.network.ClientDeferredAnswer;
import com.geargames.common.network.MessageLock;
import com.geargames.regolith.serializers.requests.ClientSerializedMessage;

/**
 * User: mikhail v. kutuzov
 * Помошник для отправки из UI стопок сообщений и массового приёма ответов.
 */
public class BatchMessageManager {
    private static BatchMessageManager instance;

    private ClientConfiguration configuration;
    private BatchRequest request;
    private SimpleBatchAnswer answer;

    private ClientDeferredAnswer deferredAnswer;

    public static BatchMessageManager getInstance() {
        if (instance == null) {
            instance = new BatchMessageManager();
        }
        return instance;
    }

    private BatchMessageManager() {
        configuration = ClientConfigurationFactory.getConfiguration();
        request = new BatchRequest(configuration);
        request.setRequests(new ArrayList(20));
        answer = new SimpleBatchAnswer();
        answer.setAnswers(new ArrayList(20));
    }

    /**
     * Отослать стопку сообщений скопом и настроить.
     */
    public void commitMessages() {
        deferredAnswer = configuration.getNetwork().sendSynchronousMessage(request, answer);
    }

    /**
     * Ожидать ответа с сервера count раз.
     *
     * @param count количество циклов ожидания.
     * @return если ответ пришёл вернуть true
     */
    public boolean retrieve(int count) throws Exception {
        return deferredAnswer.retrieve(count);
    }

    public BatchAnswer getAnswer() {
        return answer;
    }

    /**
     * Добавить сообщение message для отправки.
     *
     * @param message сообщение для отправки
     */
    public void deferredSend(ClientSerializedMessage message, ClientDeSerializedMessage messageAnswer) {
        answer.getAnswers().add(messageAnswer);
        request.getRequests().add(message);
    }

    /**
     * Размер очереди сообщений.
     * @return
     */
    public int size() {
        return request.getRequests().size();
    }
}
