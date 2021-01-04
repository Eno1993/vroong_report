package com.example.study.service;

import com.example.study.model.entitiy.Item;
import com.example.study.model.entitiy.OrderGroup;
import com.example.study.model.entitiy.User;
import com.example.study.model.enumclass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.model.network.response.UserOrderInfoApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse,User> {

    @Autowired
    private OrderGroupApiLogicService orderGroupApiLogicService;

    @Autowired
    private ItemApiLogicService itemApiLogicService;

    //1 데이터 요청 2. user 생성 3 생성된 데이터를 ->UserApiResponse return

    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {
        //1
        UserApiRequest userApiRequest = request.getData();
        //2
        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();

        User newUser = baseRepository.save(user);
        return Header.OK(response(newUser));
    }

    @Override
    public Header<UserApiResponse> read(Long id) {

        //id 로부터 user 받아오기
        Optional<User> optional = baseRepository.findById(id);

        // user 를 return 으로 넘겨주기
        return optional
                .map(user ->response(user))// 있음
                .map(userApiResponse -> Header.OK(userApiResponse))
                .orElseGet( ()->Header.ERROR("데이터 없음")// 없음
                );
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {

        //데이터 가져오기
        UserApiRequest userApiRequest = request.getData();

        //데이터 받아오기
        Optional<User> optional = baseRepository.findById(userApiRequest.getId());

        //업데이트 과정
        return optional.map(user-> {
            //user 업데이트
            user.setAccount(userApiRequest.getAccount())
                    .setPassword(userApiRequest.getPassword())
                    .setStatus(userApiRequest.getStatus())
                    .setPhoneNumber(userApiRequest.getPhoneNumber())
                    .setEmail(userApiRequest.getEmail())
                    .setRegisteredAt(userApiRequest.getRegisteredAt())
                    .setUnregisteredAt(userApiRequest.getUnregisteredAt());
                    return user;
                })
                .map(user -> baseRepository.save(user))//해당 id에 대해서 업데이트 발생
                .map(user -> Header.OK(response(user))) //있음
                .orElseGet(()->Header.ERROR("데이터 없음")//없음
                );

    }

    @Override
    public Header delete(Long id) {

        Optional<User> optional = baseRepository.findById(id);

        return optional.map( user ->{
            baseRepository.delete(user);

            return Header.OK();
        })
                .orElseGet(()->Header.ERROR("데이터 없음"));


    }

    private UserApiResponse response(User user){

        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword()) // 암호화
                .email(user.getEmail())
                .password(user.getPassword())
                .status(user.getStatus())
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .build();

        return userApiResponse;
    }

    public Header<List<UserApiResponse>> search(Pageable pageable) {

        Page<User> users = baseRepository.findAll(pageable);
        List<UserApiResponse> userApiResponsesList = users.stream()
                .map(user -> response(user))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPage(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .currentPage(users.getNumber())
                .currentElements(users.getNumberOfElements())
                .build();

        return Header.OK(userApiResponsesList,pagination);

    }

    public Header<UserOrderInfoApiResponse> orderInfo(Long id){

        //사용자 찾기
        User user = baseRepository.getOne(id);
        UserApiResponse userApiResponse = response(user);

        //orderGroup 리스트 찾고
        List<OrderGroup> orderGroupList = user.getOrderGroupList();

        //orderGroup을 순차적으로 실행
        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroupList.stream()
                .map(orderGroup -> {
                    OrderGroupApiResponse orderGroupApiResponse = orderGroupApiLogicService.response(orderGroup).getData();

                    //해당 orderGroup 의 아이템들을 찾아옴
                    List<ItemApiResponse> itemApiResponseList = orderGroup.getOrderDetailList().stream()
                    .map(detail -> detail.getItem())
                    .map(item -> itemApiLogicService.response(item).getData())
                            .collect(Collectors.toList());

                    orderGroupApiResponse.setItemApiResponseList(itemApiResponseList);
                    return orderGroupApiResponse;
                })
                .collect(Collectors.toList());

        userApiResponse.setOrderGroupApiResponsesList(orderGroupApiResponseList);
        UserOrderInfoApiResponse userOrderInfoApiResponse = UserOrderInfoApiResponse.builder()
                .userApiResponse(userApiResponse)
                .build();

        return Header.OK(userOrderInfoApiResponse);
    }
}
