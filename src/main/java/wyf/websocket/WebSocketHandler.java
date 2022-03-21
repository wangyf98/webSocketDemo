package wyf.websocket;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import wyf.config.SpringWebSocketConfigurator;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



@ServerEndpoint(value = "/websocket/test",configurator = SpringWebSocketConfigurator.class)
@Service
public class WebSocketHandler {

    private final  Map<String, WebSocketHandler> sessionMap = new HashMap<>();

    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    @OnOpen
    public void open(Session session){
        setSession(session);
        sessionMap.put(session.getId(), this);
        System.out.println("size is " + sessionMap.size());
    }

    @OnClose
    public void close(Session session){
        System.out.println("关闭一个连接");
        sessionMap.remove(session.getId());
    }

    @OnMessage
    public void sendMessage(String message){
        for (Map.Entry<String, WebSocketHandler> sessionEntry : sessionMap.entrySet()) {
            try {
                sessionEntry.getValue().session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
