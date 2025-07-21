*** Steps to run the application 
- Run the Eureka Server
- Run the Inventory Service
- Start the Admin Service
```shell 
mvn spring-boot:run
```

- Test the service :
```shell
curl http://localhost:8077/api/v1/admin/inventory/assets 
```

And the response should be as follows :
```json
[
  {
    "id": 1,
    "productCode": "M1",
    "productName": "Macbook",
    "quantity": 2,
    "warehouseLocation": "Bangalore",
    "lastUpdated": null
  },
  {
    "id": 2,
    "productCode": "M2",
    "productName": "Macbook",
    "quantity": 2,
    "warehouseLocation": "Bangalore",
    "lastUpdated": null
  },
  {
    "id": 3,
    "productCode": "M3",
    "productName": "Macbook",
    "quantity": 2,
    "warehouseLocation": "Bangalore",
    "lastUpdated": null
  }
]
```

