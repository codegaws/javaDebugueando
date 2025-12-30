package com.debugeandoideas.gadgetplus.services;

import com.debugeandoideas.gadgetplus.dto.OrderDTO;
import com.debugeandoideas.gadgetplus.entities.OrderEntity;
import com.debugeandoideas.gadgetplus.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersCrudServiceImpl implements OrdersCrudService {

    private final OrderRepository orderRepository;

    @Override
    public String create(OrderDTO order) {
        return "";
    }

    @Override
    public OrderDTO read(Long id) {
        return this.mapOrderFromEntity(this.orderRepository.findById(id).orElseThrow());
    }

    @Override
    public OrderDTO update(OrderDTO order, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    //CLASE 80 READ PARTE1
    private OrderDTO mapOrderFromEntity(OrderEntity orderEntity) {
        final var mapper = new ModelMapper();

        return mapper.map(orderEntity, OrderDTO.class);
    }
}
