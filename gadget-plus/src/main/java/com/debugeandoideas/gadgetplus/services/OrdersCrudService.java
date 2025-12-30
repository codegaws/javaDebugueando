package com.debugeandoideas.gadgetplus.services;

import com.debugeandoideas.gadgetplus.dto.OrderDTO;

public interface OrdersCrudService {

    String create(OrderDTO order);

    OrderDTO read(Long id);

    OrderDTO update(OrderDTO order, Long id);

    void delete(Long id);
}
