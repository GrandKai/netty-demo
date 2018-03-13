import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;


public class FixedLengthFrameDecoderTest {

    @Test
    public void testFrameDecoded() {
        ByteBuf buf = Unpooled.buffer();

        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // write bytes
        assertTrue(channel.writeInbound(input.retain()));
        assertTrue(channel.finish());

        System.out.println("可读字节：" + buf.readableBytes());
        System.out.println("读索引：" + buf.readerIndex());
        System.out.println("================");
        // read messages
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        System.out.println("可读字节：" + buf.readableBytes());
        System.out.println("读索引：" + buf.readerIndex());
        System.out.println("================");

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        System.out.println("可读字节：" + buf.readableBytes());
        System.out.println("读索引：" + buf.readerIndex());
        System.out.println("================");

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        System.out.println("可读字节：" + buf.readableBytes());
        System.out.println("读索引：" + buf.readerIndex());
        System.out.println("================");

        assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    public void testFrameDecoded2() {
        
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(9);
        }

        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // true if the write operation did add something to the inbound buffer
        // readInbound() 可以如读出数据则 writeInbound(...) 返回 true
        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(1)));
        assertTrue(channel.writeInbound(input.readBytes(1)));
        assertTrue(channel.writeInbound(input.readBytes(1)));
        assertTrue(channel.writeInbound(input.readBytes(1)));
        assertTrue(channel.writeInbound(input.readBytes(3)));

        assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }

    public static void main(String[] args) {

        ByteBuf buf = Unpooled.buffer();

        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        System.out.println(buf.readableBytes());
        System.out.println(buf.readerIndex());

        buf.readByte();
        buf.readByte();
        System.out.println(buf.readerIndex());
        System.out.println(buf.writerIndex());
        System.out.println(buf.markWriterIndex());
        System.out.println(buf.markReaderIndex());

        ByteBuf input = buf.duplicate();
        System.out.println("=============");
        System.out.println(input.readableBytes());
        System.out.println(input.readerIndex());
        System.out.println(input.writerIndex());
        System.out.println(input.capacity());
        System.out.println(input.markWriterIndex());
        System.out.println(input.markReaderIndex());


        byte STATE_HANDLER_REMOVED_PENDING = 2;

    }
}