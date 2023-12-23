package com.deptManager.Dept.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Boolean status;
    @NotNull
    @PositiveOrZero
    private Double currentTotalDept;
    @NotNull
    private Long bId;
    @JsonBackReference
    @ManyToOne()
    @JoinColumn(
            name = "book_id",
            referencedColumnName = "id",
            nullable = false,
            insertable = true,
            updatable = true
    )
    private Book book;
    @JsonBackReference
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<DeptItem> deptItem;

    @JsonBackReference
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<DebtTransaction> transactions;

    @PositiveOrZero
    @NotNull
    private Double remainingTotalDept;
}
