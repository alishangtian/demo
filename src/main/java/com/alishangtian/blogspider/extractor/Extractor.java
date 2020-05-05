package com.alishangtian.blogspider.extractor;

import java.io.IOException;

/**
 * @Author maoxiaobing
 * @Description
 * @Date 2020/4/24
 */
public interface Extractor {

    /**
     * @Author maoxiaobing
     * @Description crawling
     * @Date 2020/4/24
     * @Param [url]
     * @Return java.lang.String
     */
    public String extractFromHtml(String html, String articleSelector) throws IOException, Exception;

    /**
     * @Author maoxiaobing
     * @Description crawling
     * @Date 2020/4/24
     * @Param [url]
     * @Return java.lang.String
     */
    public String extractFromUrl(String url, String articleSelector) throws IOException, Exception;

    /**
     * 获取服务编号
     *
     * @return
     */
    public String getServiceCode();

}
