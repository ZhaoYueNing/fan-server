package cn.zynworld.fanserver.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * Created by zhaoyuening on 2018/12/26.
 */
@Slf4j
public class SocketHandler implements Runnable {

    private Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        if (Objects.isNull(socket) || this.socket.isClosed()) {
            return;
        }

        try (
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
        ){
            byte[] bytes = new byte[200];

            while (socket.getInputStream().) {
                Thread.sleep(100);
            }
            int readCount = in.read(bytes);
            StringBuilder builder = new StringBuilder();
            while (readCount > 0) {
                builder.append(new String(bytes));
                readCount = in.read(bytes);
            }

            log.info("{}", builder.toString());

            //将响应头发送给客户端
            String responseFirstLine = "HTTP/1.1 200 OK\r\n";
            String responseHead = "Content-Type: text/html\r\n";
            out.write(responseFirstLine.getBytes());
            out.write(responseHead.getBytes());
            out.write("\r\n".getBytes());
            out.write(builder.toString().getBytes());
            out.flush();
            log.info("### 线程结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
