package com.debugeandoideas.gadgetplus.controllers;

import com.debugeandoideas.gadgetplus.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Map<String, String>> startTransaction(@RequestParam Long id) {
        this.transactionService.executeTransaction(id);
        return ResponseEntity.ok(Map.of("Transaction", "ok"));
    }
}
