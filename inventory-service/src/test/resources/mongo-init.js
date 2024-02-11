const inventoryDbName = "inventory";
const inventoryDb = db.getSiblingDB(inventoryDbName);

inventoryDb.createCollection("product_entity");
