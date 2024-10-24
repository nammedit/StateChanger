# StateChanger
A framework for working with iterative, cell-like, potentially chaotic systems

'''
(
var notes;
// the StateChanger's .new-method takes 4 arguments: cell, trigs (triggers), funcs (functions) and data
var statechanger = StateChanger.new(

	// the 'cell' argument takes an instance of the Event-class containing the data of the cell.
	// This data can be anything you want and is associated with a key of choice
	cell: (
		// a basic example: a \notes-key containing an array of 3 midinotes
		notes:[60,62,64]
	),

	// the 'trigs' argument also takes an Event, containing keys associated with Routines
	// that either return 0 or 1. When returning 1 on a given iteration, the function with
	// similar key-names will be evaulated. 0 will block the function with smimilar key-names
	// to evaluate on that iteration
	trigs: (
		// this sequence, [0,1], will make the StateChanger
		// rotate the midinotes every 2nd iteration
		// all other Pattern-objects can also be used
		rotate:Pseq([0,1],inf).asStream // adding .asStream to turn the Pattern into a Routine
	),

	// the 'funcs' argument also takes an Event, containing keys associated with functions that
	// will be evaluated when calling the .iterate-method(nStates)-method on the StateChanger-object.
	// These functions are passed the .cell and the .data of the StateChanger-object internally in the class
	// and have to be included in all declared functions as |cellAlias,dataAlias| for iteration to work
	funcs: (
		// this function rotates the contents of in the \notes-key in the cell by a "rationIndex"
		// coming from a different pattern in the 'data' argument below.
		// first it will rotate 1 steps, 1, 1, 2, 1, 2, 3 and then loop this pattern
		rotate:{|cell,data|
			var rotationIndex = data[\rotationIndexSequence].next;
			cell[\notes] = cell[\notes].rotate(rotationIndex);

			// small example of adding complexity:
			// this line transposes the cell by a semitone everytime the rotationIndex == 3
			if(rotationIndex == 3, {cell[\notes] = cell[\notes] + 1});
		}
	),

	// the 'data' argument also takes an Event, containing keys associated with additional data
	// used in the functions altering the data in the cell that should not be
	// part of the "surface-level" of the cell; i.e the states that are stored in
	// the .memory-attribute of the StateChanger-object
	// this data can also be altered in the functions declared above in the 'funcs' argument
	data: (
		// this pattern is used to make the 'rotate' function rotate on the next number in the sequence
		// instead of a static number
		rotationIndexSequence:Pseq([1,1,1,2,1,2,3],inf).asStream
	)
);

// calling .iterate(20) on the StateChanger-object makes it "iterate" 20 times
// applying the ruleset declared above on each iteration,
statechanger.iterate(20);

// using the .flatmemory-method to collect the flattened data from the \note-key in the cell
// .statememory can also be used to get an Array containing the data from all previous states
notes = statechanger.flatmemory(\notes).collect{|note|note};

// plotting to look at the pattern the StateChanger made; which is in this example quite basic
notes.plot;
)



'''
