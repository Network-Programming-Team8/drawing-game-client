package dto.event.client;

import dto.event.Event;

import java.io.Serial;

public class ClientSuggestTopicEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8193520028598419293L;
    private final String topic;

    public ClientSuggestTopicEvent(String topic) {
        this.topic = topic;
    }

    public String getTopic(){
        return topic;
    }
}
