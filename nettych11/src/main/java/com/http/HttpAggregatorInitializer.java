package com.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public HttpAggregatorInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (isClient) {
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
        }
        // 将 FullHttpRequest, FullHttpResponse 的多个组成部分，聚合成完整的 FullHttpRequest, FullHttpResponse， 不用关心消息碎片
        // 代价是消息分段需要被缓冲, 直到可以转发一个完整的消息给下一个 ChannelInboudHandler ???
        pipeline.addLast("aggregator", new HttpObjectAggregator(512*1024));
    }
}
