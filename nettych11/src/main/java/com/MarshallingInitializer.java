package com;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import java.io.Serializable;

public class MarshallingInitializer extends ChannelInitializer<Channel> {
    private final UnmarshallerProvider unMarshallerProvider;
    private final MarshallerProvider marshallerProvider;

    public MarshallingInitializer(UnmarshallerProvider unMarshallerProvider, MarshallerProvider marshallerProvider) {
        this.unMarshallerProvider = unMarshallerProvider;
        this.marshallerProvider = marshallerProvider;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // add MarshallingDecoder to convert ByteBufs to POJOS
        pipeline.addLast(new MarshallingDecoder(unMarshallerProvider));
        // add MarshallingEncoder to convert POJOS to ByteBufs
        pipeline.addLast(new MarshallingEncoder(marshallerProvider));
        // add an ObjectHandler for normal POJOS that implement Serializable
        pipeline.addLast(new ObjectHandler());
    }

    private class ObjectHandler extends SimpleChannelInboundHandler<Serializable> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {
            // do something
        }
    }
}
