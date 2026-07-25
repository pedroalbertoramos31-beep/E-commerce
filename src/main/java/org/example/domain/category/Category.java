package org.example.domain.category;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private CategoryStatus status;


    public static Category create(String name, CategoryStatus status){

        Category category = new Category();

        category.name = name;
        category.status = status;

        return category;
    }

    public void changeState(CategoryStatus state){
        this.status = state;
    }

}