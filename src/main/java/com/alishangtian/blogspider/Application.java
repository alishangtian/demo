package com.alishangtian.blogspider;

import com.alishangtian.blogspider.extractor.ExtractorManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

/**
 * @author maoxiaobing
 */
@SpringBootApplication
@Log4j2
@RestController
public class Application {
    @Autowired
    ExtractorManager extractorManager;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/extract/jianshu")
    public String extractJanshu(@RequestParam String url, @RequestParam String articleSelector) throws Exception {
        log.info("/extract/jianshu url:{},articleSelector:{}", url, articleSelector);
        return extractorManager.getExtractor("jianshu").extractFromUrl(url, articleSelector);
    }

    @GetMapping("/extract/toutiao/fromurl")
    public String extractToutiaoFromUrl(@RequestParam String url, @RequestParam String articleSelector) throws Exception {
        log.info("/extract/toutiao articleSelector:{}", articleSelector);
        return extractorManager.getExtractor("toutiao").extractFromUrl(url, articleSelector);
    }

    @PostMapping("/extract/toutiao/fromhtml")
    public String extractToutiaoFromHtml(@RequestBody String body, @RequestParam String articleSelector) throws Exception {
        log.info("/extract/toutiao articleSelector:{}", articleSelector);
        return extractorManager.getExtractor("toutiao").extractFromHtml(body, articleSelector);
    }

}
