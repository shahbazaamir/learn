var mongo = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/";

mongo.connect(url, function(err, db) {
  if (err) throw err;
  var dbo = db.db("questionDb");
  dbo.collection("question").findOne({}, function(err, result) {
    if (err) throw err;
    console.log(result);
    db.close();
  });
});
