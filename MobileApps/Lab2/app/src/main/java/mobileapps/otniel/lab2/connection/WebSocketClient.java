package mobileapps.otniel.lab2.connection;

import java.net.URI;

import mobileapps.otniel.lab2.ws.WebsocketClientEndpoint;


//https://stackoverflow.com/questions/34131718/what-is-a-simple-way-to-implement-a-websocket-client-in-android-is-the-followin
public class WebSocketClient extends WebsocketClientEndpoint {
    public WebSocketClient(URI endpointURI) {
        super(endpointURI);
    }
}
