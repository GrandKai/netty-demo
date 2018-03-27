package com;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new LineBasedFrameDecoder(64 * 1024)).addLast(new FrameHandler());


        // writing big data (using zero-copy feature of NIO by FileRegion)
        File file = new File("");
        FileInputStream in = new FileInputStream(file);

        // A region of a file that is sent via a Channel which supports zero-copy file transfer.
        FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());

        ch.writeAndFlush(region).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    Throwable cause = future.cause();

                    // do something
                }
            }
        });
    }

    private static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuffer> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuffer msg) throws Exception {
            // do something with the data extracted from the frame
        }
    }
}
