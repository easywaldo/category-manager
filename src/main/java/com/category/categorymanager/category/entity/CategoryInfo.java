package com.category.categorymanager.category.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "category_info")
public class CategoryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_info_seq")
    private Integer categoryInfoSeq;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_depth")
    private Integer categoryDepth;

    @Column(name = "parent_seq")
    private Integer parentSeq;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @CreationTimestamp
    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @UpdateTimestamp
    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    @Builder
    public CategoryInfo(Integer categoryInfoSeq,
                        String categoryName,
                        Integer categoryDepth,
                        Integer parentSeq,
                        Boolean isDelete,
                        LocalDateTime createDt,
                        LocalDateTime updateDt) {
        this.categoryInfoSeq = categoryInfoSeq;
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.parentSeq = parentSeq;
        this.isDelete = isDelete;
        this.createDt = createDt;
        this.updateDt = updateDt;
    }
}
