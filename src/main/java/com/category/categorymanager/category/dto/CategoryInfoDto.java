package com.category.categorymanager.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryInfoDto {

    private Integer categoryInfoSeq;
    private String categoryName;
    private Integer categoryDepth;
    private Integer parentSeq;

    private Integer depth2CategoryInfoSeq;
    private String depth2CategoryName;
    private Integer depth2ParentSeq;

    private Integer depth3CategoryInfoSeq;
    private String depth3CategoryName;
    private Integer depth3ParentSeq;


    @Builder
    public CategoryInfoDto(Integer categoryInfoSeq,
                           String categoryName,
                           Integer categoryDepth,
                           Integer parentSeq,
                           Integer depth2CategoryInfoSeq,
                           String depth2CategoryName,
                           Integer depth2ParentSeq,
                           Integer depth3CategoryInfoSeq,
                           String depth3CategoryName,
                           Integer depth3ParentSeq) {
        this.categoryInfoSeq = categoryInfoSeq;
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.parentSeq = parentSeq;
        this.depth2CategoryInfoSeq = depth2CategoryInfoSeq;
        this.depth2CategoryName = depth2CategoryName;
        this.depth2ParentSeq = depth2ParentSeq;
        this.depth3CategoryInfoSeq = depth3CategoryInfoSeq;
        this.depth3CategoryName = depth3CategoryName;
        this.depth3ParentSeq = depth3ParentSeq;
    }
}
