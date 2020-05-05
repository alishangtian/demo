package com.alishangtian.blogspider.extractor.impl;

import com.alishangtian.blogspider.extractor.AbstractExtractor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author maoxiaobing
 * @Description
 * @Date 2020/4/24
 */
@Service
@Log4j2
public class TouTiaoExtractor extends AbstractExtractor {
    private static final String SERVICE_CODE = "toutiao";
    static Map<String, String> hTagMap = new HashMap<>();

    static {
        hTagMap.put("h1", "# ");
        hTagMap.put("h2", "## ");
        hTagMap.put("h3", "### ");
        hTagMap.put("h4", "#### ");
        hTagMap.put("h5", "#### ");
        hTagMap.put("h6", "#### ");
    }

    private static final int DEFAULT_TIME_OUT = 60 * 1000;
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new ThreadFactory() {
        AtomicLong num = new AtomicLong();

        @Override
        public Thread newThread(Runnable r) {
            Thread a = new Thread(r);
            a.setName("spider-thread-pool-" + num.getAndIncrement());
            return a;
        }
    });

    /**
     * script执行引擎
     */
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * @Author maoxiaobing
     * @Description crawling
     * @Date 2020/4/24
     * @Param [url]
     * @Return java.lang.String
     */
    @Override
    public String extractFromHtml(String body, String articleSelector) throws Exception {
        StringBuilder builder = new StringBuilder();
        Future<String> future = executor.submit(() -> {
            try {
                Document doc = Jsoup.parse(body);
                extractMd(doc.selectFirst(articleSelector).childNodes(), builder);
                return builder.toString();
            } catch (Exception e) {
                log.error("{}", e);
            }
            return "null";
        });
        return future.get();
    }

    @Override
    public String extractFromUrl(String url, String articleSelector) throws IOException, Exception {
        StringBuilder builder = new StringBuilder();
        Future<String> future = executor.submit(() -> {
            try {
                Document doc = Jsoup.parse(new URL(url), DEFAULT_TIME_OUT);
                extractMd(doc.selectFirst(articleSelector).childNodes(), builder);
                return builder.toString();
            } catch (Exception e) {
                log.error("{}", e);
            }
            return "null";
        });
        return future.get();
    }

    @Override
    public String getServiceCode() {
        return SERVICE_CODE;
    }

    /**
     * 提取json中的文档
     *
     * @param nodes
     * @param builder
     */
    public void extractMd(List<Node> nodes, StringBuilder builder) {
        for (Node node : nodes) {
            if (node instanceof Element) {
                Element ele = (Element) node;
                String nodeName = ele.nodeName().toLowerCase();
                String mdTag;
                //h tag
                if (nodeName.equals("div") && (ele.hasClass("article-sub") || ele.hasClass("article-tag"))) {
                    continue;
                }
                if (StringUtils.isNotEmpty(mdTag = hTagMap.get(ele.nodeName()))) {
                    builder.append("\n").append(mdTag).append(ele.text()).append("\n");
                    continue;
                }
                // img
                if (nodeName.equals("img")) {
                    String imgSrc = ele.attr("abs:src");
                    builder.append("\n").append(String.format("![img](%s)", imgSrc)).append("\n");
                    continue;
                }
                // code
                if (nodeName.equals("pre")) {
                    builder.append("\n").append("```java").append("\n").append(ele.text()).append("\n").append("```").append("\n");
                    continue;
                }
                // blockquote
                if (nodeName.equals("blockquote")) {
                    Elements imgs = ele.select("img");
                    if (null != imgs && imgs.size() > 0) {
                        Element imgEle = imgs.first();
                        String imgSrc = imgEle.attr("abs:src");
                        builder.append("\n").append(ele.text()).append("\n");
                        builder.append("\n").append(String.format("![img](%s)", imgSrc)).append("\n");
                    } else {
                        builder.append("\n").append(ele.outerHtml()).append("\n");
                    }
                    continue;
                }
                // a
                if (nodeName.equals("a")) {
                    builder.append("\n").append(ele.outerHtml()).append("\n");
                    continue;
                }
                // strong
                if (nodeName.equals("strong")) {
                    builder.append("\n").append(String.format("***%s***", ele.ownText())).append("\n");
                    continue;
                }
                // ul
                if (nodeName.equals("ul")) {
                    Elements lis = ele.select("li");
                    lis.forEach(element -> {
                        builder.append("\n").append(String.format("- %s", element.text())).append("\n");
                    });
                    continue;
                }
                if (nodeName.equals("ol")) {
                    Elements lis = ele.select("li");
                    for (int i = 0; i < lis.size(); i++) {
                        builder.append("\n").append(String.format("%s. %s", i + 1, lis.get(i).text())).append("\n");
                    }
                    continue;
                }
            } else if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                if (StringUtils.isNotEmpty(textNode.text())) {
                    builder.append("\n").append(textNode.text()).append("\n");
                }
            }
            extractMd(node.childNodes(), builder);
        }
    }

}
