package com.example.study.service;

import com.example.study.model.entitiy.OrderDetail;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.OrderDetailApiRequest;
import com.example.study.model.network.response.OrderDetailApiResponse;
import com.example.study.repository.ItemRepository;
import com.example.study.repository.OrderGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderDetailLogicService extends BaseService<OrderDetailApiRequest, OrderDetailApiResponse, OrderDetail>{

    @Autowired
    private OrderGroupRepository orderGroupRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Header<OrderDetailApiResponse> create(Header<OrderDetailApiRequest> request) {

        OrderDetailApiRequest body = request.getData();

        OrderDetail orderDetail = OrderDetail.builder()
                .status(body.getStatus())
                .arrivalDate(body.getArrivalDate())
                .quantity(body.getQuantity())
                .totalPrice(body.getTotalPrice())
                .createdAt(LocalDateTime.now())
                .createdBy("AdminServer")
                .orderGroup(orderGroupRepository.getOne(body.getOrderGroupId()))
                .item(itemRepository.getOne(body.getItemId()))
                .build();

        OrderDetail newOrderDetail1 = baseRepository.save(orderDetail);

        return response(newOrderDetail1);
    }

    @Override
    public Header<OrderDetailApiResponse> read(Long id) {

        return baseRepository.findById(id)
                .map(orderDetail -> response(orderDetail))
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<OrderDetailApiResponse> update(Header<OrderDetailApiRequest> request) {

        OrderDetailApiRequest body = request.getData();

        return baseRepository.findById(body.getId())
                .map(entityOrderDetail ->{
                    entityOrderDetail
                            .setStatus(body.getStatus())
                            .setArrivalDate(body.getArrivalDate())
                            .setQuantity(body.getQuantity())
                            .setTotalPrice(body.getTotalPrice())
                            .setUpdatedAt(LocalDateTime.now())
                            .setUpdatedBy(body.getUpdatedBy())
                            .setOrderGroup(orderGroupRepository.getOne(body.getOrderGroupId()))
                            .setItem(itemRepository.getOne(body.getItemId()));
                    return entityOrderDetail;
                }).map(changeOrderDetail-> baseRepository.save(changeOrderDetail))
                .map(newOrderDetail-> response(newOrderDetail))
                .orElseGet(()->Header.ERROR("업데이트 실패"));

    }

    @Override
    public Header delete(Long id) {

        return baseRepository.findById(id)
                .map(orderDetail -> {
                    baseRepository.delete(orderDetail);
                    return Header.OK();
                })
                .orElseGet(()->Header.ERROR("데이터 없음"));

    }

    private Header<OrderDetailApiResponse> response(OrderDetail orderDetail){
        OrderDetailApiResponse body = OrderDetailApiResponse.builder()
                .status(orderDetail.getStatus())
                .arrivalDate(orderDetail.getArrivalDate())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .createdAt(orderDetail.getCreatedAt())
                .createdBy(orderDetail.getCreatedBy())
                .updatedAt(orderDetail.getUpdatedAt())
                .updatedBy(orderDetail.getUpdatedBy())
                .itemId(orderDetail.getItem().getId())
                .build();
        return Header.OK(body);
    }
}
