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
            // 获取读写流
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
        ){
            log.info("size :{}",socket.getInputStream().available());
            while (!reader.ready()) {
                Thread.sleep(100);
            }

            byte[] bytes = new byte[200];
            int readCount = in.read(bytes);

            String content = null;
            content = reader.readLine();
            StringBuilder builder = new StringBuilder();
            while (Objects.nonNull(content) && reader.ready()) {
                log.info("read data: {}", content);
                builder.append(content);
                builder.append("\n");
                content = reader.readLine();
            }

            //将响应头发送给客户端
            String responseFirstLine = "HTTP/1.1 200 OK\r\n";
            String responseHead = "Content-Type: text/html\r\n";
            writer.write(responseFirstLine);
            writer.write(responseHead);
            writer.write("\r\n");
            writer.write(builder.toString());
            writer.flush();
            writer.close();
            log.info("### 线程结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
