+ Object {

	existingIn{|list|
		var obj = false;
		list.do{|element| if(element == this, {obj=true})};
		^obj
	}

}

+ Collection {

	//convenience-method to convert Arrays to Pseq-streams
	pseq{|repeats=inf,offset=0|^Pseq(this,repeats,offset).asStream}

}
