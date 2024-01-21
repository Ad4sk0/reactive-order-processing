const orderDbName = "orders";
db.getSiblingDB(orderDbName);

const admin_db = db.getSiblingDB("admin");
admin_db.createUser({
  user: process.env["ORDERS_USER_USERNAME"],
  pwd: process.env["ORDERS_USER_PASSWORD"],
  roles: [{ role: "readWrite", db: orderDbName }],
});


