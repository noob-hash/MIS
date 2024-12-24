package com.example.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mis.entity.Sales;
import com.example.mis.service.SalesService;

@RestController
@RequestMapping("/mis/sales")
public class SalesController {
    @Autowired
    private SalesService salesService;

    @GetMapping
    public List<Sales> getAllSales() {
        return salesService.getAllSales();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sales> getSalesById(@PathVariable Long id) {
        return salesService.getSalesById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sales createSales(@RequestBody Sales sales) {
        return salesService.createOrUpdateSales(sales);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sales> updateSales(@PathVariable Long id, @RequestBody Sales sales) {
        return salesService.getSalesById(id)
                .map(existingSales -> {
                    sales.setSaleId(existingSales.getSaleId());
                    Sales updatedSales = salesService.createOrUpdateSales(sales);
                    return ResponseEntity.ok(updatedSales);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSales(@PathVariable Long id) {
        if (salesService.getSalesById(id).isPresent()) {
            salesService.deleteSales(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
