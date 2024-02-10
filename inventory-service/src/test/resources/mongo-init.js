const inventoryDbName = "inventory";
const inventoryDb = db.getSiblingDB(inventoryDbName);

inventoryDb.createCollection("product_entity");

inventoryDb.product_entity.insert({
  _id: ObjectId('65c77c64c0df697183d064b5'),
  name: 'test',
  productType: 'PIZZA'
})

inventoryDb.createCollection("product_status_entity");
inventoryDb.product_status_entity.insert({
  _id: ObjectId('65c77c64c0df697183d064b5'),
  quantity: 1,
})
