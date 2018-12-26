package cn.zynworld.fanserver.nio.selector;

/**
 * Created by zhaoyuening on 2018/12/26.
 */
public class SelectorMain {

    public static void main(String[] args) {
        NIOServer nioServer = new NIOServer();
        nioServer.start(6060);
    }
}
