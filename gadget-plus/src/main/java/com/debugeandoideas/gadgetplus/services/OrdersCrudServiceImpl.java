package com.debugeandoideas.gadgetplus.services;

import com.debugeandoideas.gadgetplus.dto.BillDTO;
import com.debugeandoideas.gadgetplus.dto.OrderDTO;
import com.debugeandoideas.gadgetplus.dto.ProductsDTO;
import com.debugeandoideas.gadgetplus.entities.BillEntity;
import com.debugeandoideas.gadgetplus.entities.OrderEntity;
import com.debugeandoideas.gadgetplus.entities.ProductEntity;
import com.debugeandoideas.gadgetplus.repositories.OrderRepository;
import com.debugeandoideas.gadgetplus.repositories.ProductCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Service
@RequiredArgsConstructor// -> CREAR CONSTRUCTORES CON LOS ATRIBUTOS DE LA CLASE QUE SON FINAL
public class OrdersCrudServiceImpl implements OrdersCrudService {

    private final OrderRepository orderRepository;
    private final ProductCatalogRepository productCatalogRepository;

    @Override
    public String create(OrderDTO order) {
        final var toInsert = this.mapOrderFromDto(order);//mapear de DTO a ENTITY
        return this.orderRepository.save(toInsert).getId().toString();
    }

    @Override
    public OrderDTO read(Long id) {
        return this.mapOrderFromEntity(this.orderRepository.findById(id).orElseThrow());//aqui le paso como argumento el orderEntity
    }

    @Override
    public OrderDTO update(OrderDTO order, Long id) {
        final var toUpdate = this.orderRepository.findById(id).orElseThrow();

        toUpdate.setClientName(order.getClientName());
        toUpdate.getBill().setClientRfc(order.getBill().getClientRfc());

        return this.mapOrderFromEntity(this.orderRepository.save(toUpdate));
    }

    @Override
    public void delete(Long id) {
        if (orderRepository.existsById(id)) {// existsById - deleteById estos metodos ya vienen por defecto en JPA REPOSITORY
            orderRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Client name not found");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void delete(String clientName) {

        if (orderRepository.existsByClientName(clientName)) {
            orderRepository.deleteByClientName(clientName);
        } else {
            throw new IllegalArgumentException("Client name not found");
        }
    }


//*********************************************************************************************************************
    /*    //CLASE 80 READ PARTE1 CREAMOS EL METODO PARA MAPEAR DE ENTITY A DTO
    private OrderDTO mapOrderFromEntity(OrderEntity orderEntity) {
        final var mapper = new ModelMapper();
        return mapper.map(orderEntity, OrderDTO.class);
    }*/

    //CLASE 83 MODELMAPPER MAP CUSTOM
    private OrderDTO mapOrderFromEntity(OrderEntity orderEntity) {
        final var modelMapper = new ModelMapper();

        modelMapper
                .typeMap(ProductEntity.class, ProductsDTO.class)
                .addMappings(mapper -> mapper.map(
                        entity -> entity.getCatalog().getName(), ProductsDTO::setName
                ));

        return modelMapper.map(orderEntity, OrderDTO.class);
    }


    //CLASE 84 MAPEO DE ENTIDADES PARTE I DE DTO A ENTITY
    private BigDecimal getAndSetProductsAndTotal(List<ProductsDTO> productsDto, OrderEntity orderEntity) {

        // esto -> es nuevo para calcular el total de la factura
        var total = new AtomicReference<>(BigDecimal.ZERO);

        productsDto.forEach(product -> {
            final var productFromCatalog =
                    this.productCatalogRepository.findByName(product.getName()).orElseThrow();

            // calcular el total de la factura
            total.updateAndGet(bigDecimal -> bigDecimal.add(
                    productFromCatalog.getPrice().multiply(BigDecimal.valueOf(
                            product.getQuantity().longValue()))));

            final var productEntity = ProductEntity
                    .builder()
                    .quantity(product.getQuantity())
                    .catalog(productFromCatalog)
                    .build();
            orderEntity.addProduct(productEntity);
            productEntity.setOrder(orderEntity);
        });

        return total.get();
    }

    //CLASE 85 MAPEO DE ENTIDADES PARTE II Convierte el DTO a Entity para JPA
    private OrderEntity mapOrderFromDto(OrderDTO orderDTO) {

        final var orderResponse = new OrderEntity();
        final var modelMapper = new ModelMapper();

        //mapeo personalizado
        modelMapper
                .typeMap(BillDTO.class, BillEntity.class)
                .addMappings(mapper -> mapper.map(
                        BillDTO::getIdBill, BillEntity::setId));

        log.info("Before{}", orderResponse);
        modelMapper.map(orderDTO, orderResponse);
        log.info("After{}", orderResponse);

        //seteamos los productos a la orden
        final var total = this.getAndSetProductsAndTotal(orderDTO.getProducts(), orderResponse);

        log.info("After with products{}", orderResponse);

        orderResponse.getBill().setTotalAmount(total);

        return orderResponse;
    }
}














