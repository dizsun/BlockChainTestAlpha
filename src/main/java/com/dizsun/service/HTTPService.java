package com.dizsun.service;


import com.alibaba.fastjson.JSON;
import com.dizsun.block.Node;
import com.dizsun.util.DateUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sunysen on 2017/7/6.
 */
public class HTTPService {
    private BlockService blockService;
    private VBlockService vBlockService;
    private P2PService   p2pService;
    private PeerService peerService;

    public HTTPService(Node context) {
        this.blockService = context.getBlockService();
        this.vBlockService= context.getvBlockService();
        this.p2pService = context.getP2pService();
        this.peerService=p2pService.getPeerService();
    }

    public void initHTTPServer(int port) {
        try {
            Server server = new Server(port);
            System.out.println("listening http port on: " + port);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            context.addServlet(new ServletHolder(new BlocksServlet()), "/blocks");
            context.addServlet(new ServletHolder(new VBlockServlet()), "/vblocks");
//            context.addServlet(new ServletHolder(new MineBlockServlet()), "/mineBlock");
            context.addServlet(new ServletHolder(new PeersServlet()), "/peers");
            context.addServlet(new ServletHolder(new AddPeerServlet()), "/addPeer");
            context.addServlet(new ServletHolder(new TimeCenterServlet()), "/setTC");

            server.start();
            server.join();
        } catch (Exception e) {
            System.out.println("init http server is error:" + e.getMessage());
        }
    }

    private class BlocksServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setCharacterEncoding("UTF-8");
//            resp.getWriter().println("Blocks:");
//            for (Block block : blockService.getBlockChain()) {
//                resp.getWriter().println(block.toString());
//            }
            resp.getWriter().println(JSON.toJSONString(blockService.getBlockChain()));
        }
    }

    private class VBlockServlet extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setCharacterEncoding("UTF-8");
//            resp.getWriter().println("VBlocks:");
//            for (VBlock block : vBlockService.getBlockChain()) {
//                resp.getWriter().println(block.toString());
//            }
            resp.getWriter().println(JSON.toJSONString(vBlockService.getBlockChain()));
        }
    }

    /**
     * 格式peer=20001
     */
    private class AddPeerServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            this.doPost(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setCharacterEncoding("UTF-8");
            String peer = req.getParameter("peer");
            peerService.connectToPeer(Integer.valueOf(peer));
            resp.getWriter().print("ok");
        }
    }


    private class PeersServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setCharacterEncoding("UTF-8");
//            StringBuilder sb = new StringBuilder("[");
//            for (Object port: peerService.getPeerArray()) {
//                sb.append((Integer) port+",");
//            }
//            sb.append("]");
//            resp.getWriter().print(sb);
            resp.getWriter().println(JSON.toJSONString(peerService.getPeerArray()));
        }
    }

//    private class MineBlockServlet extends HttpServlet {
//        @Override
//        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            this.doPost(req, resp);
//        }
//
//        @Override
//        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            resp.setCharacterEncoding("UTF-8");
//            String data = req.getParameter("data");
//            Block newBlock = blockService.generateNextBlock(data);
//            blockService.addBlock(newBlock);
//            peerService.broadcast(p2pService.responseLatestMsg());
//            String s = JSON.toJSONString(newBlock);
//            System.out.println("block added: " + s);
//            resp.getWriter().print(s);
//        }
//    }

    private class TimeCenterServlet extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            this.doPost(req,resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setCharacterEncoding("UTF-8");
            String host = req.getParameter("host");
            DateUtil dateUtil=DateUtil.newDataUtil();
            dateUtil.setHost(host);
        }
    }
}

