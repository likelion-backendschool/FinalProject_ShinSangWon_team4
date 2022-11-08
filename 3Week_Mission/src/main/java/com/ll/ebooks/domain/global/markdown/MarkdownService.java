package com.ll.ebooks.domain.global.markdown;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class MarkdownService {

    public String toMarkdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node node = parser.parse(markdown);
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        return htmlRenderer.render(node);
    }
}
