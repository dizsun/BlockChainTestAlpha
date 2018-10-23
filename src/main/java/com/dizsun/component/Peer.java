package com.dizsun.component;


import org.java_websocket.WebSocket;

public class Peer {
    private WebSocket webSocket;
    private int listenPort;
    private int talkPort;
    private int stability=0;

    public Peer() {
    }

    public Peer(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getTalkPort() {
        return talkPort;
    }

    public void setTalkPort(int talkPort) {
        this.talkPort = talkPort;
    }

    public int getStability() {
        return stability;
    }

    public void setStability(int _stability) {
        this.stability = _stability;
    }


    public void addStability(int _stability){
        this.stability+=_stability;
    }
}
