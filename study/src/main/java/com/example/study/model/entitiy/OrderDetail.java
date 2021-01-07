package com.example.study.model.entitiy;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

//import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"orderGroup","item"})
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    private LocalDateTime arrivalDate;

    private Integer quantity;

    private BigDecimal totalPrice;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    //private Long itemId;

    //private Long orderGroupId;

//    @ManyToOne // 객체의 이름을 적어주어야 한다. 1대N 매치를 위해 실행
//    private User user;
//
//    @ManyToOne
//    private Item item;

    //orderDetail N : 1 orderGroup
    @ManyToOne
    private OrderGroup orderGroup;

    //orderDetail N : 1 item
    @ManyToOne
    private Item item;
}
