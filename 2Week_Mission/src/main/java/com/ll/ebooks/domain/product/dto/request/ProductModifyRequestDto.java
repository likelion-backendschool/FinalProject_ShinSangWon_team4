package com.ll.ebooks.domain.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ProductModifyRequestDto {

    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    @Size(max = 200)
    private String subject;

    @NotNull(message = "가격은 필수 입력 사항입니다.")
    @DecimalMin(value= "1", message = "1원 이상의 값을 입력하세요.")
    private int price;


    @Builder
    public ProductModifyRequestDto(String subject, int price) {
        this.subject = subject;
        this.price = price;
    }
}
