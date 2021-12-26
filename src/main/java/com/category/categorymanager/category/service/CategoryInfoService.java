package com.category.categorymanager.category.service;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.command.DeleteCategoryInfoCommand;
import com.category.categorymanager.category.command.UpdateCategoryInfoCommand;
import com.category.categorymanager.category.controller.response.CategoryInfoValidationEnum;
import com.category.categorymanager.category.controller.response.CreateCategoryInfoResponse;
import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryInfoService {
    private final CategoryInfoRepository categoryInfoRepository;

    @Autowired
    public CategoryInfoService(CategoryInfoRepository categoryInfoRepository) {
        this.categoryInfoRepository = categoryInfoRepository;
    }

    private boolean isExistsCategoryInfo(Integer categoryInfoSeq) {
        return this.categoryInfoRepository.findById(categoryInfoSeq).isPresent();
    }

    @Transactional
    public CreateCategoryInfoResponse createCategoryInfo(CreateCategoryInfoCommand createCommand) {
        if(createCommand.getCategoryDepth() > 1 && !isExistsCategoryInfo(createCommand.getParentSeq())) {
            return CreateCategoryInfoResponse.builder()
                .categoryInfoValidationEnum(CategoryInfoValidationEnum.CATEGORY_IS_NOT_EXISTS)
                .build();
        }

        var registeredCategoryInfoSeq = this.categoryInfoRepository.save(createCommand.toEntity()).getCategoryInfoSeq();
        return CreateCategoryInfoResponse.builder()
            .registeredCategoryInfoSeq(registeredCategoryInfoSeq)
            .categoryInfoValidationEnum(CategoryInfoValidationEnum.REGISTERED)
            .build();
    }

    @Transactional
    public void bulkInsertCategoryInfo(List<CreateCategoryInfoCommand> bulkCommand) {
        this.categoryInfoRepository.saveAll(
            bulkCommand.stream().map(
                CreateCategoryInfoCommand::toEntity).collect(Collectors.toList())
        );
    }

    @Transactional
    public CategoryInfo updateCategoryInfo(UpdateCategoryInfoCommand updateCommand) {
        if (!isExistsCategoryInfo(updateCommand.getCategoryInfoSeq())) {
            throw new IllegalStateException("not exists category");
        }
        var targetCategory = this.categoryInfoRepository.findById(
            updateCommand.getCategoryInfoSeq());

        if (targetCategory.get().getParentSeq().equals(updateCommand.getParentSeq())) {
            UpdateCategoryInfoCommand replacedCommand = UpdateCategoryInfoCommand.builder()
                .categoryDepth(targetCategory.get().getCategoryDepth())
                .categoryName(updateCommand.getCategoryName())
                .parentSeq(targetCategory.get().getParentSeq())
                .categoryDepth(targetCategory.get().getCategoryDepth())
                .build();
            targetCategory.get().updateCategory(replacedCommand);
            return categoryInfoRepository.findById(updateCommand.getCategoryInfoSeq()).get();
        }
        else {
            updateCategoryTreeInfo(updateCommand);
            return categoryInfoRepository.findById(updateCommand.getCategoryInfoSeq()).get();
        }
    }

    @Transactional
    public void deleteCategoryInfo(DeleteCategoryInfoCommand deleteCommand) {
        var deleteTarget = this.categoryInfoRepository.findById(deleteCommand.getCategoryInfoSeq());
        if (deleteTarget.isPresent()) {
            var depth = deleteTarget.get().getCategoryDepth();
            switch (depth) {
                case 3:
                    this.categoryInfoRepository.deleteById(deleteCommand.getCategoryInfoSeq());
                    break;
                case 2:
                    var depth3List = this.categoryInfoRepository.findByParentSeqAndCategoryDepth(
                        deleteCommand.getCategoryInfoSeq(), 3);
                    this.categoryInfoRepository.deleteAllByIdInBatch(depth3List.stream().map(CategoryInfo::getCategoryInfoSeq).collect(Collectors.toList()));
                    this.categoryInfoRepository.deleteById(deleteCommand.getCategoryInfoSeq());
                    break;
                case 1:
                    var depth2List = this.categoryInfoRepository.findByParentSeqAndCategoryDepth(
                        deleteCommand.getCategoryInfoSeq(), 2);
                    depth3List = this.categoryInfoRepository.findByParentSeqInAndCategoryDepth(
                        depth2List.stream().map(CategoryInfo::getCategoryInfoSeq).collect(Collectors.toList()), 3);
                    this.categoryInfoRepository.deleteAllByIdInBatch(depth3List.stream().map(CategoryInfo::getCategoryInfoSeq).collect(Collectors.toList()));
                    this.categoryInfoRepository.deleteAllByIdInBatch(depth2List.stream().map(CategoryInfo::getCategoryInfoSeq).collect(Collectors.toList()));
                    this.categoryInfoRepository.deleteById(deleteCommand.getCategoryInfoSeq());
                    break;
            }
        }
    }

    @Transactional
    public void updateCategoryTreeInfo(UpdateCategoryInfoCommand updateCommand) {
        if (!isExistsCategoryInfo(updateCommand.getCategoryInfoSeq())) {
            throw new IllegalStateException("not exists category");
        }

        var targetCategory = this.categoryInfoRepository.findById(
            updateCommand.getCategoryInfoSeq());

        var currentDepth = targetCategory.get().getCategoryDepth();
        var updateCommandDepth = updateCommand.getCategoryDepth();
        var isUpDownDepth = updateCommandDepth.compareTo(currentDepth);

        var childDepth2Category = categoryInfoRepository.findByParentSeqAndCategoryDepth(
            updateCommand.getCategoryInfoSeq(), 2);
        var childDepth3Category = categoryInfoRepository.findByParentSeqInAndCategoryDepth(
            childDepth2Category.stream().map(CategoryInfo::getCategoryInfoSeq).collect(Collectors.toList()), 3);
        if (!childDepth3Category.isEmpty() && isUpDownDepth > 0) {
            throw new IllegalStateException("can't not be moved lower category depth");
        }
        if (currentDepth.equals(2)) {
            childDepth3Category = categoryInfoRepository.findByParentSeqAndCategoryDepth(targetCategory.get().getCategoryInfoSeq(), 3);
        }

        if (!childDepth2Category.isEmpty()) {
            childDepth2Category.forEach(x -> x.updateCategory(UpdateCategoryInfoCommand.builder()
                .categoryName(x.getCategoryName())
                .isDelete(false)
                .categoryDepth(isUpDownDepth == 0 ? x.getCategoryDepth(): isUpDownDepth > 0 ? x.getCategoryDepth() + 1 : x.getCategoryDepth() -1)
                .parentSeq(targetCategory.get().getCategoryInfoSeq())
                .build()));
        }
        if (!childDepth3Category.isEmpty()) {
            childDepth3Category.forEach(x -> x.updateCategory(UpdateCategoryInfoCommand.builder()
                .categoryName(x.getCategoryName())
                .isDelete(false)
                .categoryDepth(isUpDownDepth == 0 ? x.getCategoryDepth(): isUpDownDepth > 0 ? x.getCategoryDepth() + 1 : x.getCategoryDepth() -1)
                .parentSeq(targetCategory.get().getCategoryInfoSeq())
                .build()));
        }

        targetCategory.get().updateCategory(UpdateCategoryInfoCommand.builder()
            .categoryInfoSeq(targetCategory.get().getCategoryInfoSeq())
            .parentSeq(updateCommand.getParentSeq())
            .isDelete(updateCommand.getIsDelete())
            .categoryDepth(updateCommandDepth)
            .categoryName(updateCommand.getCategoryName())
            .build());

    }
}
