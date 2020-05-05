package com.alishangtian.blogspider.remoting;

import com.alishangtian.blogspider.cluster.Node;
import com.alishangtian.blogspider.util.JSONUtils;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;

/**
 * 发送心跳
 *
 * @Description TODO
 * @ClassName Remoting
 * @Author alishangtian
 * @Date 2020/4/28 21:58
 * @Version 0.0.1
 */
@Log4j2
public class Remoting {
    private static OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .readTimeout(Duration.ofSeconds(5))
            .writeTimeout(Duration.ofSeconds(5))
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public static boolean ping(String server, Collection<Node> knownNodes, String cuServer, int cuPort) {
        RequestBody body = RequestBody.create(JSONUtils.toJSONString(knownNodes), JSON);
        String url = "http://" + server + "/ping" + "/" + cuServer + "/" + cuPort;
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string().equals("pong");
        } catch (Exception e) {
            log.error("{}", e);
            return false;
        }
    }
}
