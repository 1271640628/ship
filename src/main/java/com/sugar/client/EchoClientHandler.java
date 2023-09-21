package com.sugar.client;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sugar.msg.Proto;
import com.sugar.msg.ProtoPlayer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class EchoClientHandler extends SimpleChannelInboundHandler<Proto.Message> {

    public static List<Channel> SESSION = new ArrayList<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

    }

    //在第一次建立连接的时候，发送一条消息
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws InvalidProtocolBufferException {
        System.out.println("开始建立连接");
        SESSION.add(ctx.channel());
    }


    //发送异常时关闭
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Proto.Message message) throws Exception {
        System.out.println("收到服务端的消息");
        System.out.println("已收到token:" + message.getHeader().getToken());
        System.out.println("发送寻宝请求");

        Proto.Response resp = message.getResponse();
        if (message.getHeader().getCode() == 1001) {
            System.out.println("登录成功");
            ProtoPlayer.PlayerLoginResp ss = resp.getBody().unpack(ProtoPlayer.PlayerLoginResp.class);
            System.out.println("获得token：" + message.getHeader().getToken());

            System.out.println("返回数据" + ss.getPlayerUid());
            return;
        } else {
            System.out.println("发送登录请求");
            Proto.Message.Builder builder = Proto.Message.newBuilder();
            Proto.Header.Builder header = Proto.Header.newBuilder();
            header.setCode(1001);
            ProtoPlayer.PlayerLoginReq.Builder req = ProtoPlayer.PlayerLoginReq.newBuilder();
            builder.setPlayerUid(12345);
            builder.setHeader(header).setRequest(Any.pack(req.build()));
            ctx.channel().writeAndFlush(builder);
        }

    }


    /**
     --测试代码
     登录测试
     if (message.getHeader().getCode() == 1001) {
     System.out.println("登录成功");
     ProtoPlayer.PlayerLoginResp ss = resp.getBody().unpack(ProtoPlayer.PlayerLoginResp.class);
     System.out.println("获得token：" + message.getHeader().getToken());

     System.out.println("返回数据" + ss.getPlayerUid());
     return;
     } else {
     System.out.println("发送登录请求");
     Proto.Message.Builder builder = Proto.Message.newBuilder();
     Proto.Header.Builder header = Proto.Header.newBuilder();
     header.setCode(1001);
     ProtoPlayer.PlayerLoginReq.Builder req = ProtoPlayer.PlayerLoginReq.newBuilder();
     builder.setPlayerUid(12345);
     builder.setHeader(header).setRequest(Any.pack(req.build()));
     ctx.channel().writeAndFlush(builder);
     }

     合卡
     if (message.getHeader().getCode() == 1002) {
     System.out.println("发送合卡请求成功");
     ProtoPlayer.FleetSynthesisResp ss = resp.getBody().unpack(ProtoPlayer.FleetSynthesisResp.class);
     System.out.println("获得token：" + message.getHeader().getToken());

     System.out.println("返回数据" + ss.getShipCard().getCardUid()+"结果："+ss.getResult());
     return;
     } else {
     System.out.println("发送合卡请求");
     Proto.Message.Builder builder = Proto.Message.newBuilder();
     Proto.Header.Builder header = Proto.Header.newBuilder();
     header.setCode(1002);
     ProtoPlayer.FleetSynthesisReq.Builder req = ProtoPlayer.FleetSynthesisReq.newBuilder();
     builder.setPlayerUid(12345);
     req.setShipCardUid(1).setEquipmentCardUid(2);
     builder.setHeader(header).setRequest(Any.pack(req.build()));
     ctx.channel().writeAndFlush(builder);
     }

     //寻宝
     if (message.getHeader().getCode() == 1003) {
     System.out.println("发送寻宝请求成功");
     ProtoPlayer.TreasureHuntResp ss = resp.getBody().unpack(ProtoPlayer.TreasureHuntResp.class);
     System.out.println("获得token：" + message.getHeader().getToken());

     System.out.println("返回数据" + ss.getReward()+"结果："+ss.getCount());
     return;
     } else {
     System.out.println("发送寻宝请求");
     Proto.Message.Builder builder = Proto.Message.newBuilder();
     Proto.Header.Builder header = Proto.Header.newBuilder();
     header.setCode(1003);
     ProtoPlayer.TreasureHuntReq.Builder req = ProtoPlayer.TreasureHuntReq.newBuilder();
     builder.setPlayerUid(12345);
     req.setType(1);
     builder.setHeader(header).setRequest(Any.pack(req.build()));
     ctx.channel().writeAndFlush(builder);
     }

     背包
     if (message.getHeader().getCode() == 1004) {
     System.out.println("发送背包请求成功");
     ProtoPlayer.PlayerAllGoodsResp ss = resp.getBody().unpack(ProtoPlayer.PlayerAllGoodsResp.class);
     System.out.println("获得token：" + message.getHeader().getToken());

     System.out.println("返回数据" + ss.getGoodsList().get(0).getItemId());
     return;
     } else {
     System.out.println("发送背包请求");
     Proto.Message.Builder builder = Proto.Message.newBuilder();
     Proto.Header.Builder header = Proto.Header.newBuilder();
     header.setCode(1004);
     ProtoPlayer.PlayerAllGoodsReq.Builder req = ProtoPlayer.PlayerAllGoodsReq.newBuilder();
     builder.setPlayerUid(12345);
     builder.setHeader(header).setRequest(Any.pack(req.build()));
     ctx.channel().writeAndFlush(builder);
     }

     战斗
     if (message.getHeader().getCode() == 1005) {
     System.out.println("发送战斗请求成功");
     ProtoPlayer.CardBattleResp ss = resp.getBody().unpack(ProtoPlayer.CardBattleResp.class);
     System.out.println("获得token：" + message.getHeader().getToken());

     System.out.println("返回数据" + ss.getReward());
     return;
     } else {
     System.out.println("发送战斗请求");
     Proto.Message.Builder builder = Proto.Message.newBuilder();
     Proto.Header.Builder header = Proto.Header.newBuilder();
     header.setCode(1005);
     builder.setPlayerUid(12345);

     ProtoPlayer.CardBattleReq.Builder req = ProtoPlayer.CardBattleReq.newBuilder();
     req.addCardUid(1);
     req.setType(100);
     req.setConfigId(1001);
     builder.setHeader(header).setRequest(Any.pack(req.build()));
     ctx.channel().writeAndFlush(builder);
     }





     */

}

