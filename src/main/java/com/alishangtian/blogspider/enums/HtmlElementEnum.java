package com.alishangtian.blogspider.enums;

/**
 * @Description TODO
 * @EnumName HtmlElementEnum
 * @Author alishangtian
 * @Date 2020/4/24 20:47
 * @Version 0.0.1
 */
public enum HtmlElementEnum {
    DIV("div"),
    UL("ul"),
    OL("ol"),
    IMG("img"),
    STRONG("strong"),
    LINK("a"),
    BLOCKQUOTO("blockquote"),
    PRE("pre");

    private String type;

    HtmlElementEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
