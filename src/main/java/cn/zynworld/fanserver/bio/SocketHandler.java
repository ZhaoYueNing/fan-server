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
            log.info("request. url:{} ",socket.getInetAddress().getHostAddress());
            byte[] bufBytes = new byte[200];

            int readCount = in.read(bufBytes);
            while ( readCount >= 0) {
                log.info("request content : {}", new String(bufBytes, 0, readCount));
                log.info("in.available:{}", in.available());
                if (in.available() <= 0) { break; }
                readCount = in.read(bufBytes);
            }

            //将响应头发送给客户端
            String responseFirstLine = "HTTP/1.1 200 OK\r\n";
            String responseHead = "Content-Type: text/html\r\n";
            out.write((responseFirstLine + responseHead + "\r\n" + "Hello World").getBytes());
            out.flush();

            log.info("### 线程结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
