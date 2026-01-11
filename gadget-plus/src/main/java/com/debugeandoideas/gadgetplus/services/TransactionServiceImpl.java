package com.debugeandoideas.gadgetplus.services;

import com.debugeandoideas.gadgetplus.repositories.BillRepository;
import com.debugeandoideas.gadgetplus.repositories.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final OrderRepository orderRepository;
    private final BillRepository billRepository;

    @Transactional
    @Override
    public void executeTransaction(Long id) {
        log.info("TRANSACTION ACTIVE 1{}", TransactionSynchronizationManager.isActualTransactionActive());
        log.info("TRANSACTION NAME 1{}", TransactionSynchronizationManager.getCurrentTransactionName());

        this.updateOrder(id);

        // this.updateBill("b-5");//lo metemos de manera harcodeada para ver el comportamiento de la transaccion
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateOrder(Long id) {
        log.info("TRANSACTION ACTIVE 2{}", TransactionSynchronizationManager.isActualTransactionActive());
        log.info("TRANSACTION NAME 2{}", TransactionSynchronizationManager.getCurrentTransactionName());
        final var order = orderRepository.findById(id).orElseThrow();
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        this.validProducts(id);
        this.updateBill(order.getBill().getId());
    }

    //CREAMOS ESTOS METODOS PARA SER LLAMADOS DENTRO DE LA TRANSACCION DE UPDATEORDER
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateBill(String id) {
        log.info("TRANSACTION ACTIVE 4{}", TransactionSynchronizationManager.isActualTransactionActive());
        log.info("TRANSACTION NAME 4{}", TransactionSynchronizationManager.getCurrentTransactionName());
        final var bill = billRepository.findById(id).orElseThrow();
        bill.setClientRfc("123");
        billRepository.save(bill);
    }

   /* @PersistenceContext
    private EntityManager entityManager;*/

    @Transactional(propagation = Propagation.NOT_SUPPORTED)//LO MEJOR ES NOT SUPPORTED PARA ESTOS METODOS DE VALIDACION - IGUAL SE HARA EL ROLLBACK CUANDO HAYA UN ERROR
    @Override
    public void validProducts(Long id) {
        log.info("TRANSACTION ACTIVE 3{}", TransactionSynchronizationManager.isActualTransactionActive());
        log.info("TRANSACTION NAME 3{}", TransactionSynchronizationManager.getCurrentTransactionName());
        // entityManager.clear();
        final var order = orderRepository.findById(id).orElseThrow();
        if (order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("there are no products in the order");
        }
    }

}
