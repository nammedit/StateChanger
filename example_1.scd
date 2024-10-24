(
//EXAMPLE 1: simple cell-based tonerow
var iter = 30;
var o = StateChanger(
	cell: (
		notes:[60,65,70,64]
	),
	trigs: (
		rot:[0,1].pseq,
		shift:Pseq([0,0,0,1],inf).asStream
	),
	funcs: (
		rot:{|c,d|c[\notes] = c[\notes].rotate(1)},
		shift:{|c,d|c[\notes] = c[\notes] + rrand(-5,5)},
		//starts scrambling the notes after some iterations, and also increases the threshold for when it scrambles
		scrambleonCount:{|c,d|
			d[\count] = d[\count] + 1;
			if(d[\count] >= d[\roof], {
				c[\notes] = c[\notes].scramble;
				d[\count] = 0;
				d[\roof] = d[\roof] + 1;
			});
		}
	),
	data: (
		count:0,
		roof:5
	)
).iterate(iter);


// this line collects the sequence of notes generated from
// the StateChanger by using the .flatmemory-method that goes through the .memory-attribute of the StateChanger,
// collects the data in the key specified for each state and flattens them if the cell contains Arrays
var notes = o.flatmemory(\notes).collect{|note|note};
notes.plot;
)