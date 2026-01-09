package com.debugeandoideas.gadgetplus.repositories;

import com.debugeandoideas.gadgetplus.entities.OrderEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    // haciendo un Query Method que borre por el nombre
    @Modifying
    void deleteByClientName(String clientName);// sin ALL por que el nombre es unico solo habra una sola coincidencia no varias para eso es mejor ALL

    Boolean existsByClientName(String clientName);// este metodo me sirve para validar si existe un nombre de cliente ya registrado
}

