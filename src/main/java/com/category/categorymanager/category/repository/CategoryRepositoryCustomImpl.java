package com.category.categorymanager.category.repository;

import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.category.entity.QCategoryInfo;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRepositoryCustomImpl extends QuerydslRepositorySupport implements CategoryRepositoryCustom {
    public CategoryRepositoryCustomImpl() {
        super((CategoryInfo.class));
    }

    @Override
    public List<CategoryInfo> findByName(String name) {
        QCategoryInfo categoryInfo = QCategoryInfo.categoryInfo;

        List<CategoryInfo> categoryInfoList = from(categoryInfo)
                .where(categoryInfo.categoryName.eq(name))
                .select(categoryInfo)
                .fetch();
        return categoryInfoList;
    }
}
