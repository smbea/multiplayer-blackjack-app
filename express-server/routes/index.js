var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/get_socket_link', function(req,res){
  res.json({link: "ws://localhost:8080"})
})

module.exports = router;
