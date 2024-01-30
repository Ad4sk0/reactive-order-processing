const orderDbName = "orders";
const inventoryDbName = "inventory";
db.getSiblingDB(orderDbName);
db.getSiblingDB(inventoryDbName);

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


