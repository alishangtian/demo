package com.alishangtian.blogspider.extractor;

import java.io.IOException;

/**
 * @Description AbstractSpider
 * @Date 2020/4/24 下午2:22
 * @Author maoxiaobing
 **/
public abstract class AbstractExtractor implements Extractor {
    @Override
    public abstract String extractFromHtml(String html, String articleSelector) throws IOException, Exception;

    @Override
    public abstract String extractFromUrl(String url, String articleSelector) throws IOException, Exception;
}
