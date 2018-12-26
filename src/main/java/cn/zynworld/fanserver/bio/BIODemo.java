package cn.zynworld.fanserver.bio;

import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhaoyuening on 2018/12/26.
 */
@Slf4j
public class BIODemo {

    private static final Integer SERVER_PORT = 6060;

    public static void main(String[] args) {
        ExecutorService threadPool = null;
        try (
            ServerSocket server = new ServerSocket(SERVER_PORT);
        ) {
            threadPool = Executors.newCachedThreadPool();
            log.info("begin");
            while (true) {
                Socket socket = server.accept();
                threadPool.execute(new SocketHandler(socket));
            }

        } catch (Exception e) {
            log.error("error", e);
        } finally {
            if (Objects.nonNull(threadPool)) {
                threadPool.shutdown();
            }
        }
    }
}
