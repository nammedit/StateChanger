(
var statechanger = StateChanger.lSystem(
	axiomString:"a", // sets statechanger.cell[\string] = axiomString
	rulesetEvent:('a':'ba','b':'caab','c':'b.','.':'a.a') // sets statechanger.data[\ruleset] = rulesetEvent
);

// you can also add custom functions / 'rules' to the statechanger after its declaration like so;
// this function cuts out a random window from the current string with a size of 10:
statechanger.funcs[\cutString] = {|c,d|
	var windowSize = 10;
	var windowLeft = rrand(0,c[\string].size-windowSize);
	var windowRight = windowLeft + windowSize;
	c[\string] = c[\string][windowLeft..windowRight];
};
// and add a trigger to it, making it do 'cutString' on a pattern
statechanger.trigs[\cutString] = Pseq([0,0,1,0,0,1,0,0,0,1],inf).asStream;

statechanger.iterate(20);
)