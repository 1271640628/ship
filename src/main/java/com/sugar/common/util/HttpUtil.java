package com.sugar.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Http工具类
 *
 * @author astupidcoder
 */

@Slf4j
public class HttpUtil {

    public static String post(String url, Map<String, Object> params, Map<String, String> headers) {
        CloseableHttpClient client = null;
        CloseableHttpResponse ret = null;
        try {
            client = HttpClients.createDefault();
            RequestConfig requestConfig =
                    RequestConfig
                            .custom()
                            .setSocketTimeout(10000)
                            .setConnectTimeout(10000)
                            .setConnectionRequestTimeout(10000)
                            .setRedirectsEnabled(false)
                            .build();

            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            List<NameValuePair> formParams = new ArrayList<>();

            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value != null) {
                    formParams.add(new BasicNameValuePair(key, String.valueOf(value)));
                }
            }

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            post.setEntity(entity);
            ret = client.execute(post);
            HttpEntity response = ret.getEntity();
            return EntityUtils.toString(response, Consts.UTF_8);
        } catch (Exception e) {
            log.error("请求失败,url:" + url + ",params:" + params, e);
            return null;
        } finally {
            doFinally(client, ret);
        }
    }

    public static String postJson(String url, Map<String, Object> params) {
        CloseableHttpClient client = null;
        CloseableHttpResponse ret = null;
        try {
            client = HttpClients.createDefault();
            RequestConfig requestConfig =
                    RequestConfig
                            .custom()
                            .setSocketTimeout(10000)
                            .setConnectTimeout(10000)
                            .setConnectionRequestTimeout(10000)
                            .setRedirectsEnabled(false)
                            .build();

            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);

            JSONObject jsonObject = new JSONObject(params);
            //装填参数
            StringEntity strEntity = new StringEntity(jsonObject.toString(), "utf-8");
            strEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //header信息
            post.setHeader("Content-type", "application/json");
            post.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //设置参数到请求对象中
            post.setEntity(strEntity);
            ret = client.execute(post);
            HttpEntity response = ret.getEntity();
            return EntityUtils.toString(response, Consts.UTF_8);
        } catch (Exception e) {
            log.error("请求失败,url:" + url + ",params:" + params, e);
            return null;
        } finally {
            doFinally(client, ret);
        }
    }

    public static String post(String url, Map<String, Object> params, Map<String, String> headers, String contentTypeStr) {
        CloseableHttpClient client = null;
        CloseableHttpResponse ret = null;
        try {
            client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).setConnectionRequestTimeout(10000).setRedirectsEnabled(false).build();
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            String jsonStr = JSON.toJSONString(params);
            StringEntity entity = new StringEntity(jsonStr, Consts.UTF_8);
            entity.setContentEncoding("UTF-8");
            entity.setContentType(contentTypeStr);//发送json数据需要设置contentType
            post.setEntity(entity);
            ret = client.execute(post);
            HttpEntity response = ret.getEntity();
            return EntityUtils.toString(response, Consts.UTF_8);
        } catch (Exception e) {
            log.error("请求失败,url:" + url + ",params:" + params, e);
            return null;
        } finally {
            doFinally(client, ret);
        }
    }

    private static void doFinally(CloseableHttpClient client, CloseableHttpResponse ret) {
        if (ret != null) {
            try {
                ret.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String post(String url, Map<String, Object> params) {
        return post(url, params, null);
    }

    public static String get(String url, Map<String, Object> params) {
        return get(url, params, 10000);
    }

    public static String get(String url, Map<String, Object> params, int timeout) {
        CloseableHttpClient client = null;
        CloseableHttpResponse ret = null;
        try {
            client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                    .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setRedirectsEnabled(false).build();
            StringBuilder builder = new StringBuilder(url);
            builder.append("?");
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value != null) {
                    builder.append(key);
                    builder.append("=");
                    builder.append(value);
                    builder.append("&");
                }
            }
            log.info("request url:{}", builder.toString());
            HttpGet get = new HttpGet(builder.toString());
            get.setConfig(requestConfig);
            ret = client.execute(get);
            HttpEntity response = ret.getEntity();
            return EntityUtils.toString(response, Consts.UTF_8);
        } catch (Exception e) {
            log.error("请求失败,url:" + url + ",params:" + params, e);
            return null;
        } finally {
            doFinally(client, ret);
        }
    }

    public static String getNomal(String url, Map<String, Object> params, int timeout) {
        CloseableHttpClient client = null;
        CloseableHttpResponse ret = null;
        try {
            client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                    .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setRedirectsEnabled(false).build();
            StringBuilder builder = new StringBuilder(url);
            builder.append("?");
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value != null) {
                    builder.append(key);
                    builder.append("=");
                    builder.append(value);
                    builder.append("&");
                }
            }
            builder.deleteCharAt(builder.length() - 1);
            log.info("request url:{}", builder.toString());
            HttpGet get = new HttpGet(builder.toString());
            get.setConfig(requestConfig);
            ret = client.execute(get);
            HttpEntity response = ret.getEntity();
            return EntityUtils.toString(response, Consts.UTF_8);
        } catch (Exception e) {
            log.error("请求失败,url:" + url + ",params:" + params, e);
            return null;
        } finally {
            doFinally(client, ret);
        }
    }


    /**
     * 使用 Map按key进行排序后拼接a=1&b=2
     */
    public static String paramStr(Map<String, ?> params, String flag) {
        return paramStr(params, flag, Sets.newHashSet(), false);
    }

    /**
     * 使用 Map按key进行排序后拼接a=1&b=2
     */
    public static String paramStr(Map<String, ?> params, String flag, boolean urlEncode) {
        return paramStr(params, flag, Sets.newHashSet(), urlEncode);
    }

    public static String paramStr(Map<String, ?> params, String flag, Set<String> excludeKeys) {
        return paramStr(params, flag, excludeKeys, false);
    }

    public static String paramStr(Map<String, ?> params, String flag, Set<String> excludeKeys, boolean urlEncode) {
        StringBuilder paramStr = new StringBuilder();

        TreeMap<String, Object> sortMap = new TreeMap<>(Comparator.naturalOrder());
        sortMap.putAll(params);

        try {
            Set<String> keySet = sortMap.keySet();
            for (String key : keySet) {
                if (excludeKeys.contains(key)) {
                    continue;
                }
                if ("sign".equals(key)) {
                    continue;
                }
                if ("qudao".equals(key)) {
                    continue;
                }
                if (urlEncode) {
                    paramStr.append(URLEncoder.encode(key, "utf-8")).append("=").append(URLEncoder.encode(String.valueOf(sortMap.get(key)), "utf-8"));
                } else {
                    paramStr.append(key).append("=").append(sortMap.get(key));
                }
                if (!isEmpty(flag)) {
                    paramStr.append(flag);
                }
            }
            return paramStr.substring(0, isEmpty(flag) ? paramStr.length() : (paramStr.length() - 1));
        } catch (Exception e) {
            log.error("", e);
            return "";
        }
    }

    public static String paramStrNoSort(Map<String, ?> params, String flag, Set<String> excludeKeys, boolean urlEncode) {
        StringBuilder paramStr = new StringBuilder();

        HashMap<String, Object> map = Maps.newLinkedHashMap(params);
        try {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                if (excludeKeys.contains(key)) {
                    continue;
                }
                if ("sign".equals(key)) {
                    continue;
                }
                if ("qudao".equals(key)) {
                    continue;
                }
                if (urlEncode) {
                    paramStr.append(URLEncoder.encode(key, "utf-8")).append("=").append(URLEncoder.encode(String.valueOf(map.get(key)), "utf-8"));
                } else {
                    paramStr.append(key).append("=").append(map.get(key));
                }
                if (!isEmpty(flag)) {
                    paramStr.append(flag);
                }
            }
            return paramStr.substring(0, isEmpty(flag) ? paramStr.length() : (paramStr.length() - 1));
        } catch (Exception e) {
            log.error("", e);
            return "";
        }
    }

    public static String paramValueStr(Map<String, String> params) {
        StringBuilder paramStr = new StringBuilder();

        TreeMap<String, String> sortMap = new TreeMap<>(Comparator.naturalOrder());
        sortMap.putAll(params);

        Set<String> keySet = sortMap.keySet();
        for (String key : keySet) {
            if ("sign".equals(key)) {
                continue;
            }
            if ("qudao".equals(key)) {
                continue;
            }
            paramStr.append(sortMap.get(key));
        }
        return paramStr.toString();
    }

    public static String paramStr(Map<String, String> params) {
        return paramStr(params, null);
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        } else {
            return str.trim().length() == 0;
        }
    }
}
