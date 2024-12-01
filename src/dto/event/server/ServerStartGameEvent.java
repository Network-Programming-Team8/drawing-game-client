package dto.event.server;

import dto.event.Event;

import java.io.Serial;
import java.util.List;

public class ServerStartGameEvent extends Event {

    @Serial
    private static final long serialVersionUID = 9022104994394221836L;
    private final String selectedTopic;
    private final int selectedUser;
    private final List<Integer> orderList;

    public ServerStartGameEvent(String selectedTopic, int selectedUser, List<Integer> orderList) {
        this.selectedTopic = selectedTopic;
        this.selectedUser = selectedUser;
        this.orderList = orderList;
    }
}
