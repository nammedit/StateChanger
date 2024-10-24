/*

A class that alters its 'state' / data on a given set of rules, and puts each new state into .memory

*/


StateChanger {

	var <>cell,<>trigs,<>funcs,<>data;
	var <memory, origState;

	*new { | cell, trigs, funcs, data |
		var memory = Array(10000);
		var origState = cell.deepCopy;
		^super.newCopyArgs(
			cell,
			trigs,
			funcs,
			data,
			memory,
			origState
		)
	}
	*lSystem{|axiomString,rulesetEvent|
		^StateChanger(
			cell: (string:axiomString),
			trigs: (),
			funcs: (
				rewrite:{|cell,data|
					var newstring = "";
					cell[\string].do{|char|
						var outchar = char.asString;
						data[\rules].keys.do{|rulekey|
							if(char.asString == rulekey.asString, {
								outchar = data[\rules][rulekey].asString;
							});
						};
						newstring = newstring ++ outchar;
					};
					cell[\string] = newstring;
				}
			),
			data:(rules:rulesetEvent) //syntax: ('a':'bc')
		)
	}
	*cellularAutomata{|dimensions,ruleset,initialState|
		var matrix = Array.fill(dimensions.first, {|y| Array.fill(dimensions.last, {|x|
			var val = ".";
			initialState.do{| pos |
				if((y == pos[\y]) && (x == pos[\x]), {val = "X"});
			};
			val;
		})});
		^StateChanger(
			cell: (
				matrix:matrix
			),
			trigs: (),
			funcs: (
				progress:{|c,d|
					var currentMatrix = c[\matrix].deepCopy;
					currentMatrix.do{|row,y|
						row.do{|val,x|
							var nn = (x:x,y:y).neighbourhoodMoore(currentMatrix).size;
							d[\ruleset].do{|event|
								if(event[\func].value(val,nn), {c[\matrix][y][x] = event[\val]});
							};
						};
					};
				}
			),
			data: (
				ruleset:ruleset
			)
		);
	}

	iterate{|nStates,print=true|
		if(memory.size == 0, {memory.add(cell.deepCopy); origState = cell.deepCopy});
		nStates.do{
			var tempstate = ();
			this.funcs.keys.do{|funckey|
				//checks if function-key has trigger-pattern
				if(funckey.existingIn(this.trigs.keys.asArray), {
					//if it has, checks if the trigger-pattern returns 1
					var trig = this.trigs[funckey].next;
					if(trig == 1, {
						//if it returns 1, evaluates the function
						this.funcs[funckey].value(this.cell,this.data); // passing cell and data of object
					});
				}, {
					//if not, just evaluates the function
					this.funcs[funckey].value(this.cell,this.data); // passing cell and data of object
				});
			};
			this.cell.keys.do{|k|
				tempstate[k] = this.cell[k].deepCopy;
				if(print, {(k.asString + tempstate[k].asString).postln});
			};
			if(print, {" ".postln});
			//saves the state to memory
			memory.add(tempstate);
		}
	}

	reset{
		this.cell = origState;
		this.trigs.do{|trig| trig.reset};
		this.data.do{|data| data.reset};
		memory = Array(10000);
	}

	statememory{^memory}
	flatmemory{|key|^memory.collect{|state| state[key]}.flatten}

	notesFromDegrees{
		if(this.cell.keys.asArray.contains([\degrees]), {
			var notes = Array(this.flatmemory(\degrees).size);
			this.statememory.do{|event|
				event[\degrees].do{|degree| var scale = event[\scale]; notes.add(scale.degreeToFreq(degree,event[\root],0).cpsmidi.round.asInteger)};
			};
			^notes
		}, {"cell doesn't have key \"degrees\"".postln});
	}
	convertRatiosToFreqs{|startfreq|
		var current = startfreq;
		memory.do{|state,idx|
			var tempfreqs = Array(state[\intervals].size);
			tempfreqs.add(current);
			state[\intervals][0..state[\intervals].size-2].do{|interval|
				current = current * interval;
				tempfreqs.add(current);
			};
			current = current * state[\intervals].last;
			memory[idx][\f] = tempfreqs;
			memory[idx] = memory[idx].removeKeys([\intervals]);
		}
	}

}


