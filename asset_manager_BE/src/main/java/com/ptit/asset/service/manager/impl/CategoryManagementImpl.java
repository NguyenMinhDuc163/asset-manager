package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Category;
import com.ptit.asset.dto.request.CategoryCreateRequestDTO;
import com.ptit.asset.dto.request.CategoryUpdateRequestDTO;
import com.ptit.asset.dto.request.FetchPageCategoryRequestDTO;
import com.ptit.asset.repository.CategoryRepository;
import com.ptit.asset.repository.GroupRepository;
import com.ptit.asset.service.manager.CategoryManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryManagementImpl implements CategoryManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GroupRepository groupRepository;


    @Override
    public Try<Category> getOne(Long id) {
        return Try.of(() -> categoryRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get category by id: "+id)));
    }

    @Override
    public Try<Category> create(CategoryCreateRequestDTO dto) {
        val group = groupRepository.findById(dto.getEmbedded().getGroupId());
        if (!group.isPresent()){
            return Try.failure(new Exception("Failure when find group with id:"+dto.getEmbedded().getGroupId()));
        }
        val category = centralMapper.toCategory(dto,group.get());
        return Try.of(() -> categoryRepository.save(category))
            .orElse(() -> Try.failure(new Exception("Failure when save product")));
    }

    @Override
    public Try<Category> update(CategoryUpdateRequestDTO dto) {
        val categoryResult = categoryRepository.findById(dto.getId());
        if (! categoryResult.isPresent()){
            return Try.failure(new Exception("Failure when find category to update with id: "+dto.getId()));
        }

        val categoryUpdate = centralMapper.toCategoryUpdate(categoryResult.get(), dto);

        // relationship space
        if (dto.getEmbedded() != null){
            val groupId = dto.getEmbedded().getGroupId();
            if (groupId != null && !groupId.equals(categoryUpdate.getGroup().getId())){
                val group = groupRepository.findById(groupId);
                group.ifPresent(categoryUpdate::setGroup);
            }
        }
        // relationship space

        return Try.of(() -> categoryRepository.save(categoryUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update category")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            categoryRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete category with id: "+id)));
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<Category> fetchAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> fetchPage(FetchPageCategoryRequestDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(),dto.getSize());
        return categoryRepository.findAll(pageable);
    }
}
