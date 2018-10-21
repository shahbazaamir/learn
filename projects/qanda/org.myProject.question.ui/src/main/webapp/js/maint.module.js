var maintModule=angular.module('maint');
maintModule.service('maintService', maintService);
var maintService=function($http){
	var maintServiceJson={"createUser":createUser};
	var createUser= function(){
		$http.post();
	}
	return maintServiceJson;
}