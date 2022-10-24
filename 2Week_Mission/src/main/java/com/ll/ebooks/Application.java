package com.ll.ebooks;

import com.ll.ebooks.domain.global.initdata.InitDataBefore;
import com.ll.ebooks.domain.member.service.MemberService;
import com.ll.ebooks.domain.post.service.PostService;
import com.ll.ebooks.domain.postkeyword.service.PostKeywordService;
import com.ll.ebooks.domain.product.repository.ProductRepository;
import com.ll.ebooks.domain.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    public class init implements InitDataBefore {

        @Bean
        CommandLineRunner initData(MemberService memberService, PostService postService,
                                   ProductService productService, ProductRepository productRepository,
                                   PostKeywordService postKeywordService) {
            return args -> {
                tearUp(memberService, postService, productService, productRepository, postKeywordService);
            };

        }
    }
}
