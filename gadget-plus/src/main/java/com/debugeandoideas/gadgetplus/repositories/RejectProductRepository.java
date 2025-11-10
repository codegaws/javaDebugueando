package com.debugeandoideas.gadgetplus.repositories;

import com.debugeandoideas.gadgetplus.entities.RejectProductEntity;
import com.debugeandoideas.gadgetplus.entities.RejectProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RejectProductRepository extends JpaRepository<RejectProductEntity, RejectProductId> {
}
