var mongo = require('mongodb');
var url = "mongodb://localhost:27017/qanda";
mongo.MongoClient.connect(url, function(err, db) {
  if (err) throw err;
  console.log("Database created!");
  db.close();
});
