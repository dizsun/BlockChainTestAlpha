package com.dizsun.service;

import com.dizsun.component.Peer;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 管理peer节点的连接和移除及通信
 */
public class PeerService {
    private ArrayList<Peer> peers;
    private static PeerService peerService;
    private int localPort;
    private P2PService p2PService;
    private int nonce;
    private Object peersLock;

    public PeerService(P2PService _p2PService) {
        peers = new ArrayList<>();
        this.p2PService = _p2PService;
        this.localPort = p2PService.getPort();
        this.nonce=_p2PService.getNonce();
        peersLock=new Object();
    }
//
//    public static PeerService newPeerService(P2PService _p2PService) {
//        if (peerService == null) {
//            peerService = new PeerService(_p2PService);
//        }
//        return peerService;
//    }

    public void addPeer(WebSocket webSocket) {
        int talkport = webSocket.getRemoteSocketAddress().getPort();
        if (!peers.contains(webSocket) && talkport != localPort) {
//            peers.add(port);
            Peer peer = new Peer(webSocket);
            peer.setTalkPort(talkport);
            peers.add(peer);
        }
    }

    public void addPeer(WebSocket webSocket, int port) {
        if (port <= 1024 || port >= 65535 || port == localPort ||peers.contains(webSocket)|| peers.contains(port)) {
            return;
        }else {
            Peer peer = new Peer(webSocket);
            peer.setListenPort(port);
            peers.add(peer);
        }
    }

    public void removePeer(WebSocket webSocket) {
        if (webSocket != null) {
            for (int i = 0; i < peers.size(); i++) {
                if (peers.get(i).getWebSocket().equals(webSocket))
                    peers.remove(i);
            }
//            peers.remove(webSocket.getRemoteSocketAddress().getPort());
//            sockets.remove(webSocket);
        }
    }

    public void write(WebSocket webSocket, String msg) {
        webSocket.send(msg);
    }

    public void broadcast(String msg) {
        for (Peer peer : peers) {
            this.write(peer.getWebSocket(), msg);
        }
//        for (WebSocket ws : sockets) {
//            this.write(ws, msg);
//        }
    }

    public boolean contains(WebSocket webSocket) {
        for (Peer peer : peers) {
            if (peer.getWebSocket().equals(webSocket))
                return true;
        }
        return false;
    }

    public boolean contains(int listenPort) {
        for (Peer peer : peers) {
            if (peer.getListenPort() == listenPort)
                return true;
        }
        return false;
    }

    /**
     * 连接peer
     */
    public void connectToPeer(int port) {
        System.out.println("[nonce "+nonce+"][PeerService]loacalPort:"+localPort+" connect "+port);
        if (port <= 1024 || port >= 65535 || port == localPort || contains(port)) {
            return;
        }
        try {
            final WebSocketClient socket = new WebSocketClient(new URI("http://127.0.0.1:" + port)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    write(this, p2PService.responsePort());
                    write(this, p2PService.queryChainLengthMsg());
                    write(this, p2PService.queryAllPeers());
                    write(this, p2PService.queryAllVMsg());
                    addPeer(this, port);
//                    addPeer(port);
                }

                @Override
                public void onMessage(String s) {
                    //handleMessage(this, s);
                    p2PService.handleMsgThread(this, s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("[nonce "+nonce+"][PeerService]connection failed:closed");
                    removePeer(this);
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("[nonce "+nonce+"][PeerService]connection failed:error");
                    e.printStackTrace();
                    removePeer(this);
                }
            };
            socket.connect();
        } catch (URISyntaxException e) {
            System.out.println("[nonce "+nonce+"][PeerService]p2p connect is error:" + e.getMessage());
        }

    }

    public int length() {
        return peers.size();
    }

//    public ArrayList<WebSocket> getSockets() {
//        return sockets;
//    }

    public Object[] getPeerArray() {
        ArrayList<Integer> ports = new ArrayList<>();
        peers.sort(new Comparator<Peer>() {
            @Override
            public int compare(Peer o1, Peer o2) {
                return o2.getStability()-o1.getStability();
            }
        });
        for (Peer peer : peers) {
            if (peer.getListenPort() > 0)
                ports.add(peer.getListenPort());
        }
        return ports.toArray();
    }

    public boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        return ipAddress;
    }

    public void updateSI(WebSocket webSocket,int _stability){
        for (int i = 0; i < peers.size(); i++) {
            if(peers.get(i).getWebSocket().equals(webSocket)){
                peers.get(i).addStability(_stability);
                break;
            }
        }
    }

}

