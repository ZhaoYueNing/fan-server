package cn.zynworld.fanserver.nio.buffer;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zhaoyuening on 2018/12/26.
 * 练习buffer 的读写
 */
@Slf4j
public class BufferDemo {

    private static final String FILE_NAME = "/demo.txt";
    private static final String RW_MOD = "rw";
    private static final Integer BUF_SIZE = 100;

    public static void main(String[] args) {
        try {
            String path = BufferDemo.class.getResource(FILE_NAME).toURI().getPath();
            log.info("path :{}", path);
            // 构建文件通道
            FileChannel channel = new RandomAccessFile(path, RW_MOD).getChannel();
            // 构建缓冲区
            ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE);

            // 持续读写
            int readCount = channel.read(buf);

            buf.flip();
            log.info("buf tostring:{} ", buf.toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (buf.hasRemaining()) {
                baos.write(buf.get());
            }
            log.info("{}",baos.toString("UTF-8"));
        } catch (Exception e) {
            log.error("error.", e);
        }
    }

}
