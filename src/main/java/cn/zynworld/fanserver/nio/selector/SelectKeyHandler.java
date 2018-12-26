package cn.zynworld.fanserver.nio.selector;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

import static java.nio.channels.SelectionKey.OP_READ;

/**
 * Created by zhaoyuening on 2018/12/26.
 */
@Slf4j
public class SelectKeyHandler implements Runnable {
    private SelectionKey key;

    private SelectKeyHandler(SelectionKey key) {
        this.key = key;
    }

    /**
     * 构建一个selectKeyHandler
     */
    public static SelectKeyHandler build(SelectionKey selectionKey) {
        return new SelectKeyHandler(selectionKey);
    }

    @Override
    public void run() {
        // 处理接收请求
        if (key.isAcceptable()) {
            handleAccept();
            return;
        }

        // 处理连接
        if (key.isReadable()) {
            handleRead();
            return;
        }
    }

    private void handleAccept() {
        try {
            log.info("begin accept.key:{}", key);
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(key.selector(), SelectionKey.OP_READ);

        } catch (Exception e) {
            log.info("handle accept error.", e);
        }
    }

    private void handleRead() {
        try (
                SocketChannel channel = (SocketChannel) key.channel()
        ){
            log.info("begin read.key:{}", key);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int readCount = channel.read(byteBuffer);
            byteBuffer.flip();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(byteBuffer.array());
            key.interestOps(OP_READ | SelectionKey.OP_WRITE);

            log.info("data content:{}", byteArrayOutputStream.toString("UTF-8"));
        } catch (Exception e) {
            log.error("handle read error.", e);
        }
    }
}
