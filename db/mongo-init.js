const orderDbName = "orders";
const inventoryDbName = "inventory";
const deliveryDbName = "delivery";
const orderDb = db.getSiblingDB(orderDbName);
const inventoryDb = db.getSiblingDB(inventoryDbName);
const deliveryDb = db.getSiblingDB(deliveryDbName);

const admin_db = db.getSiblingDB("admin");
admin_db.createUser({
  user: process.env["ORDERS_USER_USERNAME"],
  pwd: process.env["ORDERS_USER_PASSWORD"],
  roles: [{ role: "readWrite", db: orderDbName }],
});
admin_db.createUser({
  user: process.env["INVENTORY_USER_USERNAME"],
  pwd: process.env["INVENTORY_USER_PASSWORD"],
  roles: [{ role: "readWrite", db: inventoryDbName }],
});
admin_db.createUser({
  user: process.env["DELIVERY_USER_USERNAME"],
  pwd: process.env["DELIVERY_USER_PASSWORD"],
  roles: [{ role: "readWrite", db: deliveryDbName }],
});

inventoryDb.createCollection("product_entity");
inventoryDb.product_entity.createIndex( { name: 1 }, { unique: true } )

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
deliveryDb.vehicle_entity.insertMany([
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
