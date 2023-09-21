package com.sugar.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class TokenUtil {

    /**
     * 设置过期时间 目前为半年 传值单位需为 mill
     * EXPIRE_DATE = 120 * TimeUtil.MINUTE_SECONDS * TimeUtil.ONE_MILLS;
     */
    //private static final long EXPIRE_DATE = 180 * TimeUtil.DAY_MILLIS;
    private static final long EXPIRE_DATE =1;
    /**
     * token秘钥
     */
    private static final String TOKEN_KEY = "b15ca014b71a4e2a9d1e37d5f5acb619";

    /**
     * 注意同一秒生成的两个token是一样的
     */
    public static String token(String uid) {
        try {
            // 过期时间
            Date date = new Date(TimeUtil.getNowOfMills() + EXPIRE_DATE);
            // 秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_KEY);
            // 设置头部信息
            Map<String, Object> header = Maps.newHashMap();
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            // 携带username，password信息，生成签名
            return JWT.create().withHeader(header).withClaim("k30v38s", uid).withExpiresAt(date).sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证token，通过返回true
     * 2019/1/18/018 9:39
     * [token]需要校验的串
     **/
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
