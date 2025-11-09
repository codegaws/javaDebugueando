package com.debugeandoideas.gadgetplus.repositories;

import com.debugeandoideas.gadgetplus.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {

}
