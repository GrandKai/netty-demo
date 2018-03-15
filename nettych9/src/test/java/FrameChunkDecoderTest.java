import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        System.out.println("初始化input第一次：" + input.readerIndex());

        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        assertTrue(channel.writeInbound(input.readBytes(2)));
        System.out.println("初始化input第二次：" + input.readerIndex());

        try {
            channel.writeInbound(input.readBytes(4));
            System.out.println("初始化input第三次：" + input.readerIndex());

            Assert.fail();
        } catch (TooLongFrameException e) {
            // expected exception

//            System.out.println(e);
        }

        System.out.println("初始化input第四次：" + input.readerIndex());
        assertTrue(channel.writeInbound(input.readBytes(3)));
        assertTrue(channel.finish());
        System.out.println("初始化input第五次：" + input.readerIndex());

        // Read frames
        System.out.println("初始化：" + buf.readerIndex());
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(2), read);
        read.release();
        System.out.println("第一次读取：" + buf.readerIndex());

        read = channel.readInbound();
        assertEquals(buf.skipBytes(4).readSlice(3), read);
        System.out.println("第二次读取：" + buf.readerIndex());
        read.release();
        buf.release();

    }

}