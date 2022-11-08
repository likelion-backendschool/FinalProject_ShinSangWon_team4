package com.ll.ebooks.domain.order.dto.response;

import com.ll.ebooks.domain.order.entity.Order;
import com.ll.ebooks.domain.order.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderListResponseDto {

    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private OrderStatus orderStatus;

    public OrderListResponseDto(Order entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        this.orderStatus = entity.getOrderStatus();
    }


}
