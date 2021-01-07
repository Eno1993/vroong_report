package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entitiy.User;
import com.example.study.model.enumclass.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepositoryTest extends StudyApplicationTests {

    //dependency Injection (DI)
    @Autowired
    private UserRepository userRepository;

    @Test
    public void create(){

        String account = "Test02";
        String password = "Test02";
        UserStatus status = UserStatus.REGISTERED;
        String email = "Test02@gmail.com";
        String phoneNumber = "010-2222-1111";
        LocalDateTime registeredAt = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setStatus(status);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setRegisteredAt(registeredAt);

//        User u = User.builder()
//                .account(account)
//                .password(password)
//                .status(status)
//                .email(email)
//                .build(); -> 이런식으로 많이 사용한다 요즘에는

        User newUser = userRepository.save(user);

        Assertions.assertNotNull(newUser);

    }

    @Test
    @Transactional
    public void read(){
        User user = userRepository.findFirstByPhoneNumberOrderByIdDesc("010-1111-1111");


        user.getOrderGroupList().stream().forEach(orderGroup -> {
            System.out.println("------주문 묶음--------");
            System.out.println("수령인 : "+orderGroup.getRevName());
            System.out.println("수령지 : "+orderGroup.getRevAddress());
            System.out.println("총금액 : "+orderGroup.getTotalPrice());
            System.out.println("총수량 : "+orderGroup.getTotalQuantity());

            System.out.println("------주문 상세--------");

            orderGroup.getOrderDetailList().forEach(orderDetail->{
                System.out.println("파트너사이름 : "+orderDetail.getItem().getPartner().getName());
                System.out.println("파트너사카테고리 : "+orderDetail.getItem().getPartner().getCategory().getTitle());
                System.out.println("주문상품 : "+orderDetail.getItem().getName());
                System.out.println("고객센터번호 : "+orderDetail.getItem().getPartner());
                System.out.println("주문상태 : "+orderDetail.getStatus());
                System.out.println("도착예정일자 : "+orderDetail.getArrivalDate());

            });
        });

        Assertions.assertNotNull(user);

    }
    @Test
    public void update(){

        Optional<User> newUser = userRepository.findById(1L);

        newUser.ifPresent(selectUser ->{
            selectUser.setAccount("pppp");
            selectUser.setUpdatedAt(LocalDateTime.now());
            selectUser.setCreatedBy("update method()");

            userRepository.save(selectUser);
        });

    }
    @Test
    @Transactional // 이거 마지막에 rollback 해줌 database 변화 안됨, 다른 crud에서 사용 가능 (쿼리는 실행되지만 db는 변하지 않음)
    public void delete(){

        Optional<User> newUser = userRepository.findById(3L);
        Assertions.assertTrue(newUser.isPresent());

        newUser.ifPresent(selectUser ->{
            userRepository.delete(selectUser);
        });

        Optional<User> deleteUser = userRepository.findById(3L);

        Assertions.assertFalse(deleteUser.isPresent());
//        if(deleteUser.isPresent()){ // 이거 대신 Assertions 사용하면 될 듯.
//            System.out.println("데이터 존재"+deleteUser.get());
//        }
//        else{
//            System.out.println("데이터 삭제 데이터 없음");
//        }
    }


}
