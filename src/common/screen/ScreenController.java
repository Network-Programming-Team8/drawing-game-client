package common.screen;

import utils.ClientSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ScreenController {
    private ClientSocket clientSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

//    private final Map<String, DTOClass> dtoMapper;

    public ScreenController() throws IOException {
        clientSocket = new ClientSocket();
        in = clientSocket.getObjectInputStream();
        out = clientSocket.getObjectOutputStream();
    }

    public void run() throws IOException, ClassNotFoundException {
        while(true){
            //TODO : outputStream 에서 계속 읽어서 DTO 값에 따라 ScreenViewer 에 데이터 넘겨주면서 어떤 화면 보여줄지 결정
            in.readObject();
        }
    }
}
