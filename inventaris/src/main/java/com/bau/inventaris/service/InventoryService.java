package com.bau.inventaris.service;

import com.bau.inventaris.entity.Inventory;
import com.bau.inventaris.entity.UsageRecord;
import com.bau.inventaris.exception.InsufficientStockException;
import com.bau.inventaris.repository.InventoryRepository;
import com.bau.inventaris.repository.UsageRecordRepository;
import com.bau.inventaris.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final UsageRecordRepository usageRecordRepository;

    public InventoryService(InventoryRepository inventoryRepository, UserRepository userRepository,
                            UsageRecordRepository usageRecordRepository) {
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.usageRecordRepository = usageRecordRepository;
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public String useInventory(Long userId, Long inventoryId, int quantity) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        var inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getStock() < quantity) {
        throw new InsufficientStockException("Insufficient stock for inventory ID: " + inventoryId);
}


        inventory.setStock(inventory.getStock() - quantity);
        inventoryRepository.save(inventory);

        UsageRecord usageRecord = new UsageRecord();
        usageRecord.setUser(user);
        usageRecord.setInventory(inventory);
        usageRecord.setQuantity(quantity);
        usageRecordRepository.save(usageRecord);

        return "Inventory used successfully";
    }
}
