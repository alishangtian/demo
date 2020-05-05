package com.alishangtian.blogspider.extractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Description TODO
 * @ClassName ExtractorrManager
 * @Author alishangtian
 * @Date 2020/4/25 09:50
 * @Version 0.0.1
 */
@Service
public class ExtractorManager {
    @Autowired
    List<Extractor> extractorList;
    Map<String, Extractor> extractorMap = new HashMap<>();

    @PostConstruct
    public void init() {
        extractorList.forEach(extractor -> {
            extractorMap.put(extractor.getServiceCode(), extractor);
        });
    }

    /**
     * 根据服务code获取服务
     *
     * @param serviceCode
     * @return
     */
    public Extractor getExtractor(String serviceCode) {
        return extractorMap.get(serviceCode);
    }
}
