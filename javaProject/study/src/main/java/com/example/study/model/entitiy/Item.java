package com.example.study.model.entitiy;

import com.example.study.model.enumclass.ItemStatus;
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
import java.util.List;
//import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"orderDetailList","partner"})
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private String name;

    private String title;

    private String content;

    private BigDecimal price;

    private String brandName;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;
    //private Long partnerId;

    //LAZY: 지연로딩    EAGER: 즉시로딩
    //LAZY: select * from item id  = ?
    //EAGER:
    //      item_id = order_detail.item_id
    //      user_id = order_detail.user_id
    //      -> 두개를 JOIN (데이터가 많은 db에 조인을 걸면 성능이 저하가 될 수 있음 따라서 1:1인 경우에 사용하는 경우가 많음)
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
//    private List<OrderDetail> orderDetailList;

    //item 1 : N orderDetail
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private List<OrderDetail> orderDetailList;

    //item N : 1 partner
    @ManyToOne
    private Partner partner;
}