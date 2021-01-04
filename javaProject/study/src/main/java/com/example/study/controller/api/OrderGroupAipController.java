package com.example.study.controller.api;

import com.example.study.controller.CrudController;
import com.example.study.model.entitiy.OrderGroup;
import com.example.study.model.network.request.OrderGroupApiRequest;
import com.example.study.model.network.response.OrderGroupApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orderGroup")
public class OrderGroupAipController extends CrudController<OrderGroupApiRequest, OrderGroupApiResponse, OrderGroup> {


}
