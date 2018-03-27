import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class T {

    public static void main(String[] args) {
        byte a = ' ';
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf byteBuf = Unpooled.copiedBuffer("netty in action rocks!", utf8);

        int index = byteBuf.indexOf(byteBuf.readerIndex(), byteBuf.writerIndex(), a);

        System.out.println(byteBuf.readerIndex());
        System.out.println(byteBuf.writerIndex());
        System.out.println(index);

        ByteBuf sliced = byteBuf.slice(byteBuf.readerIndex(), index);
        ByteBuf sliced2 = byteBuf.slice(index + 1, byteBuf.writerIndex());

        System.out.println("netty in action rocks!".length());

        System.out.println(sliced.toString(utf8));
        System.out.println(sliced2.toString(utf8));
    }
}
