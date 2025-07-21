package com.example.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.inventory.model.Inventory;

import java.util.List;

@RestController
@RequestMapping("/api/v2/inventory")
public class InventoryController {

    @GetMapping("/assets")
    public ResponseEntity<List<Inventory>> getEmployees() {
        List<Inventory> inventories = List.of(
                new Inventory(1l,"M1","Macbook",2,"Bangalore",null),
                new Inventory(2l,"M2","Macbook",2,"Bangalore",null),
                new Inventory(3l,"M3","Macbook",2,"Bangalore",null)
        );
        return ResponseEntity.status(200).body( inventories );
    }

}
