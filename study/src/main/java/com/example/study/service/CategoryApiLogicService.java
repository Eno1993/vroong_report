package com.example.study.service;

import com.example.study.model.entitiy.Category;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.CategoryApiRequest;
import com.example.study.model.network.response.CategoryApiResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryApiLogicService extends BaseService<CategoryApiRequest, CategoryApiResponse, Category> {

    @Override
    public Header<CategoryApiResponse> create(Header<CategoryApiRequest> request) {

        CategoryApiRequest body = request.getData();

        Category category = new Category().builder()
                .type(body.getType())
                .title(body.getTitle())
                .createdAt(LocalDateTime.now())
                .createdBy("AdminServer")
                .build();

        Category newCategory = baseRepository.save(category);

        return response(newCategory);
    }
    @Override
    public Header<CategoryApiResponse> read(Long id) {

        return baseRepository.findById(id)
                .map(category -> response(category))
                .orElseGet(()->Header.ERROR("데이터 없음"));

        }

    @Override
    public Header<CategoryApiResponse> update(Header<CategoryApiRequest> request) {

        CategoryApiRequest body = request.getData();

        return baseRepository.findById(body.getId())
                .map(entityCategory ->{
                    entityCategory
                            .setType(body.getType())
                            .setTitle(body.getTitle())
                            .setCreatedAt(body.getCreatedAt())
                            .setCreatedBy(body.getCreatedBy())
                            .setUpdatedAt(LocalDateTime.now())
                            .setUpdatedBy(body.getUpdatedBy());
                    return entityCategory;
                })
                .map(newEntityCategory -> baseRepository.save(newEntityCategory))
                .map(category -> response(category))
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(category -> {
                    baseRepository.delete(category);
                    return Header.OK();
                })
                .orElseGet(()->Header.ERROR("데이터 없음"));

    }

    private Header<CategoryApiResponse> response(Category category){
        CategoryApiResponse body = CategoryApiResponse.builder()
                .id(category.getId())
                .type(category.getType())
                .title(category.getTitle())
                .createdAt(category.getCreatedAt())
                .createdBy(category.getCreatedBy())
                .build();
        return Header.OK(body);
    }

}
