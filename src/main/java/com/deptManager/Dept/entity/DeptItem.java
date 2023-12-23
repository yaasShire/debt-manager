package com.deptManager.Dept.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeptItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String productName;
    @NotNull
    @Positive
    private Long quantity;
    @NotNull
    @Positive
    private Double price;
    @NotNull
    private Date date;
    @ManyToOne()
    @JoinColumn(
            name = "page_id",
            referencedColumnName = "id",
            nullable = false,
            insertable = true,
            updatable = true
    )
    @JsonBackReference
    private Page page;
}
