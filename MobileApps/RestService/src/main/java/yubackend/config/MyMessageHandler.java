package yubackend.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class MyMessageHandler extends TextWebSocketHandler {

    private static WebSocketSession webSocketSession;


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // The WebSocket has been closed
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
       ;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        System.out.println("Message received: " + textMessage.getPayload());
        webSocketSession=session;
    }

    public static void sendVolunteerMessage(String message) throws IOException {
        System.out.println("Trimit notificare ->");
        webSocketSession.sendMessage(new TextMessage(message));
    }
}
