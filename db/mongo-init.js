const orderDbName = "orders";
const inventoryDbName = "inventory";
const deliveryDbName = "delivery";
const orderDb = db.getSiblingDB(orderDbName);
const inventoryDb = db.getSiblingDB(inventoryDbName);
const deliveryDb = db.getSiblingDB(deliveryDbName);

inventoryDb.createCollection("product_entity");
inventoryDb.product_entity.createIndex( { name: 1 }, { unique: true } )
inventoryDb.createCollection("product_order_cancellation_entity");
inventoryDb.product_order_cancellation_entity.createIndex( { productOrderId: 1 }, { unique: true } )

deliveryDb.createCollection("vehicle_entity");
deliveryDb.vehicle_entity.insertMany([
    {
        "name": "Vehicle 1",
        "status": "FREE"
    },
    {
        "name": "Vehicle 2",
        "status": "FREE"
    },
    {
        "name": "Vehicle 3",
        "status": "FREE"
    }
]);

deliveryDb.createCollection("driver_entity");
deliveryDb.driver_entity.insertMany([
    {
        "name": "Driver 1",
        "status": "FREE"
    },
    {
        "name": "Driver 2",
        "status": "FREE"
    },
    {
        "name": "Driver 3",
        "status": "FREE"
    }
]);
