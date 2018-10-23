package com.dizsun.block;

import com.dizsun.service.*;
import com.dizsun.util.ICallback;
import com.dizsun.util.SQLUtil;

public class Node {
    private int nonce;
    private int httpPort;
    private int p2pPort;
    private P2PService p2pService;
    private Node context;
    private BlockService blockService;
    private VBlockService vBlockService;

    public Node(int nonce, int httpPort, int p2pPort) {
        this.nonce = nonce;
        this.httpPort = httpPort;
        this.p2pPort = p2pPort;
        this.context=this;
    }

    public void start(ICallback context) {
        try {
            blockService  = new BlockService(nonce);
            vBlockService=new VBlockService();
            SQLUtil sqlUtil=new SQLUtil(nonce);
            sqlUtil.initBlocks(null);
            Broadcaster broadcaster = new Broadcaster();
            this.p2pService = new P2PService(this);
//            serviceManager.setP2PService(p2pService);
            broadcaster.subscribe(p2pService);
            p2pService.initP2PServer();
            HTTPService httpService = new HTTPService(this);
            broadcaster.broadcast();
            context.callback();
            httpService.initHTTPServer(httpPort);
        } catch (Exception e) {
            System.out.println("[nonce "+nonce+"][Node]startup is error:" + e.getMessage());
        }
    }

    public void conntectTo(int port){
        System.out.println("[nonce "+nonce+"][Node]localPort:"+p2pPort+" connect "+port);
        p2pService.getPeerService().connectToPeer(port);
    }

    public int getNonce() {
        return nonce;
    }

    public int getP2pPort() {
        return p2pPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public BlockService getBlockService() {
        return blockService;
    }

    public VBlockService getvBlockService() {
        return vBlockService;
    }

    public P2PService getP2pService() {
        return p2pService;
    }
}
