package com.sugar.common.util;


import com.sugar.msg.Proto;

import java.util.List;

import static com.sugar.server.module.player.SessionManager.GROUP;

/**
 * 消息工具类
 *
 * @author astupidcoder
 */
public class MessageUtil {

    /**
     * 发给单人
     */
    public static void send(Proto.Message message, long uid) {
        //SessionManager.getInstance().send(uid, message);
    }

    /**
     * 发给多人
     */
    public static void sendUids(Proto.Message message, long... uids) {
        for (long uid : uids) {
            send(message, uid);
        }
    }

    public static void sendUids(Proto.Message message, List<Long> uids) {
        if (uids.size() == 0) {
            return;
        }
        for (long uid : uids) {
            send(message, uid);
        }
    }


    /**
     * 发给全服【无视是否登录】
     */
    public static void sendServer(Proto.Message message) {
        GROUP.writeAndFlush(message);
    }

}
