
- Basic Commands :
```bash

curl http://localhost:8087/api/v2/inventory/assets
```
Output should be :
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