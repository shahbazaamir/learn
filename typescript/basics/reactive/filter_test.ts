console.log('hello');
data   = [
    { "name" : "Shahbaz" , "age" : "22" ,"subjects" :["maths","physics"]},
    { "name" : "Saba" , "age" : "21" ,"subjects" :["physics","bio"] }, 
	{ "name" : "rahan" , "age" : "10" ,"subjects" :["chemistry","maths"] }, 
	{ "name" : "farhan" , "age" : "9" ,"subjects" :["maths","bio"] }
];
console.log(
	data.filter( e => {
			
			
			for (f in e.subjects   ){
				console.log(f);				
				if (e.subjects[f]=="bio"){
					return true;
				}
			}
			return false;
		}
	)
);