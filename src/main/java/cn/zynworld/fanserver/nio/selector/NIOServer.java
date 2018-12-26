package cn.zynworld.fanserver.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by zhaoyuening on 2018/12/26.
 */
public class NIOServer {

    // 默认监听端口
    private static final Integer DEFAULT_PORT = 6060;
    // 线程池大小
    private static final Integer THREAD_POOL_SIZE = 50;

    // 是否停止运行
    private Boolean stop = false;
    private Executor executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void start(Integer port) {
        try {
            if (Objects.isNull(port)) {
                port = DEFAULT_PORT;
            }

            // 构建 serverSocketChannel 并注册
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            // 构建 selector
            Selector selector = Selector.open();

            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 不断轮训
            while (!stop) {
                int selectCount = selector.select(5000);
                if (selectCount <= 0) {
                    System.out.print(" . ");
                    continue;
                }

                // 获取所有selectKey
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    // 获取被select 的 key
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    // 处理
                    executor.execute(SelectKeyHandler.build(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
