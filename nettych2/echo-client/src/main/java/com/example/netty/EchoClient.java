package com.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        new EchoClient(host, port).start();
    }

    private void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            final AttributeKey<Integer> id = AttributeKey.newInstance("id");
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(this.host, this.port))
                    /*.handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }

                    });*/
                    .handler(new SimpleChannelInboundHandler<ByteBuffer>() {

                        // TODO: 不知道为什么这种方式接收不到服务端回复的消息
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuffer msg) throws Exception {
                            System.out.println("客户端收到消息：" + msg.toString());
                        }

                        @Override
                        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                            Integer inValue = ctx.channel().attr(id).get();
                            System.out.println(id);
                        }

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {

                            ctx.writeAndFlush(Unpooled.copiedBuffer("我是客户端发送的消息", CharsetUtil.UTF_8));
                        }
                    });
            // 存储 id 属性
            b.attr(id, 123456);
            // 连接到远程节点, 阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            // 阻塞,直到 Channel 关闭
            f.channel().closeFuture().sync();

        } finally {
            // 关闭线程池并且释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
