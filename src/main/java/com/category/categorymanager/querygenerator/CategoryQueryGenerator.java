package com.category.categorymanager.querygenerator;

import com.category.categorymanager.category.command.QueryCategoryInfoCommand;
import com.category.categorymanager.category.dto.CategoryInfoDto;
import com.category.categorymanager.category.entity.QCategoryInfo;
import com.google.common.base.Strings;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.category.categorymanager.category.entity.QCategoryInfo.categoryInfo;

@Repository(value = "category")
public class CategoryQueryGenerator {
    @PersistenceContext
    protected EntityManager entityManager;

    protected JPAQueryFactory jpaQueryFactory;

    public CategoryQueryGenerator(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public List<CategoryInfoDto> selectCategoryInfo(Integer categoryInfoSeq) {
        QCategoryInfo categoryInfoDepth2 = new QCategoryInfo("qci2");
        QCategoryInfo categoryInfoDepth3 = new QCategoryInfo("qci3");
        QCategoryInfo categoryInfoTable = new QCategoryInfo("categoryInfoTable");

        SQLTemplates templates = H2Templates.builder().build();
        JPASQLQuery<?> mainQuery = new JPASQLQuery<Void>(this.entityManager, templates);
        BooleanBuilder whereClause = new BooleanBuilder();
        if (!categoryInfoSeq.equals(0)) {
            whereClause.and(categoryInfo.categoryInfoSeq.eq(categoryInfoSeq));
        }

        var mediumCategoryQuery = SQLExpressions
            .select(
                categoryInfo.categoryInfoSeq.as("categoryInfoSeq"),
                categoryInfoDepth2.categoryInfoSeq.as("categoryInfoSeqDepth2"),
                categoryInfo.categoryName.as("categoryName"),
                categoryInfo.parentSeq,
                categoryInfoDepth2.categoryName.as("depth2CategoryName"),
                categoryInfoDepth2.parentSeq.as("depth2ParentSeq")
            )
            .from(categoryInfo)
            .leftJoin(categoryInfoDepth2).on(categoryInfoDepth2.parentSeq.eq(categoryInfo.categoryInfoSeq))
            .where(whereClause);

        return mainQuery.from(mediumCategoryQuery, Expressions.stringPath("mediumCategoryView"))
            .innerJoin(categoryInfoTable).on(
                categoryInfoTable.categoryInfoSeq.eq(CustomBeanPath.getIntegerPath("mediumCategoryView", "categoryInfoSeq")))
            .leftJoin(categoryInfoDepth3).on(categoryInfoDepth3.parentSeq.eq(CustomBeanPath.getIntegerPath("mediumCategoryView", "categoryInfoSeqDepth2")))
            .select(Projections.fields(CategoryInfoDto.class,
                categoryInfoTable.categoryDepth,
                categoryInfoTable.parentSeq,
                CustomBeanPath.getIntegerPath("mediumCategoryView", "categoryInfoSeq").as("categoryInfoSeq"),
                CustomBeanPath.getIntegerPath("mediumCategoryView", "categoryInfoSeqDepth2").as("depth2CategoryInfoSeq"),
                CustomBeanPath.getIntegerPath("mediumCategoryView", "depth2ParentSeq").as("depth2ParentSeq"),
                categoryInfoDepth3.categoryInfoSeq.as("depth3CategoryInfoSeq"),
                categoryInfoDepth3.parentSeq.as("depth3ParentSeq"),
                CustomBeanPath.getStringPath("mediumCategoryView", "categoryName").as("categoryName"),
                CustomBeanPath.getStringPath("mediumCategoryView", "depth2CategoryName").as("depth2CategoryName"),
                categoryInfoDepth3.categoryName.as("depth3CategoryName")))
            .fetch();
    }

    public List<CategoryInfoDto> queryCategoryInfoList(QueryCategoryInfoCommand command) {
        QCategoryInfo categoryInfoDepth2 = new QCategoryInfo("qci2");
        QCategoryInfo categoryInfoDepth3 = new QCategoryInfo("qci3");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(categoryInfo.isDelete.eq(false));
        whereClause.and(categoryInfoDepth2.isDelete.eq(false));
        whereClause.and(categoryInfoDepth3.isDelete.eq(false));

        BooleanBuilder likeClause = new BooleanBuilder();
        if (!Strings.isNullOrEmpty(command.getCategoryName())) {
            likeClause.or(categoryInfo.categoryName.toLowerCase().like("%" + command.getCategoryName().toLowerCase() + "%"));
            likeClause.or(categoryInfoDepth2.categoryName.toLowerCase().like("%" + command.getCategoryName().toLowerCase() + "%"));
            likeClause.or(categoryInfoDepth3.categoryName.toLowerCase().like("%" + command.getCategoryName().toLowerCase() + "%"));
        }

        if (command.getCategoryParentSeq() > 0) {
            likeClause.or(categoryInfo.parentSeq.eq(command.getCategoryParentSeq()));
            likeClause.or(categoryInfoDepth2.parentSeq.eq(command.getCategoryParentSeq()));
            likeClause.or(categoryInfoDepth3.parentSeq.eq(command.getCategoryParentSeq()));
        }
        whereClause.and(likeClause);

        if (Strings.isNullOrEmpty(command.getCategoryName()) && command.getCategoryParentSeq().equals(0)) {
            whereClause = new BooleanBuilder();
        }

        return this.jpaQueryFactory.from(categoryInfo)
            .leftJoin(categoryInfoDepth2).on(categoryInfoDepth2.parentSeq.eq(categoryInfo.categoryInfoSeq))
            .leftJoin(categoryInfoDepth3).on(categoryInfoDepth3.parentSeq.eq(categoryInfoDepth2.categoryInfoSeq))
            .where(whereClause)
            .select(Projections.fields(CategoryInfoDto.class,
                categoryInfo.categoryInfoSeq,
                categoryInfo.categoryName,
                categoryInfo.categoryDepth,
                categoryInfo.parentSeq,
                categoryInfoDepth2.categoryInfoSeq.as("depth2CategoryInfoSeq"),
                categoryInfoDepth2.categoryName.as("depth2CategoryName"),
                categoryInfoDepth2.parentSeq.as("depth2ParentSeq"),
                categoryInfoDepth3.categoryInfoSeq.as("depth3CategoryInfoSeq"),
                categoryInfoDepth3.categoryName.as("depth3CategoryName"),
                categoryInfoDepth3.parentSeq.as("depth3ParentSeq")))
            .fetch();
    }

}
