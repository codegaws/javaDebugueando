package com.debugeandoideas.gadgetplus.repositories;

import com.debugeandoideas.gadgetplus.entities.BillEntity;
import org.springframework.data.repository.CrudRepository;

public interface BillRepository extends CrudRepository<BillEntity, String> {
}
