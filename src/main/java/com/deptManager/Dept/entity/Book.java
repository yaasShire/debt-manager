package com.deptManager.Dept.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String ownerName;
    @NotNull
    @NotBlank
    private String ownerNumber;
    @NotNull
    @NotBlank
    private String backUpNumber;
    private String createDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private AppUser user;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Page> pages;
}