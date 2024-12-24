package com.example.mis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mis.entity.Sales;
import com.example.mis.repo.SalesRepository;

@Service
public class SalesService {
    @Autowired
    private SalesRepository salesRepository;

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    public Optional<Sales> getSalesById(Long saleId) {
        return salesRepository.findById(saleId);
    }

    public Sales createOrUpdateSales(Sales sales) {
        return salesRepository.save(sales);
    }

    public void deleteSales(Long saleId) {
        salesRepository.deleteById(saleId);
    }
}
