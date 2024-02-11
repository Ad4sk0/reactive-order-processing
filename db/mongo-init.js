const orderDbName = "orders";
const inventoryDbName = "inventory";
const orderDb = db.getSiblingDB(orderDbName);
const inventoryDb = db.getSiblingDB(inventoryDbName);

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

inventoryDb.createCollection("product_entity");
inventoryDb.product_entity.createIndex( { name: 1 }, { unique: true } )


