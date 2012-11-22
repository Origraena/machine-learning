package ori.ml.core;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * The dead part of a memory is the one which actually stores
 * examples to take into account while solving.
 * <p>
 * Note that even if this memory is dead, a <i>calling into
 * question</i> process may also be used to limit its capacity.
 * </p>
 */
public interface DeadMemory<L> extends Iterable<MetaExample<L>> {

	/**
	 * Adds a memory under a valued example format.
	 * @param r The example to remember.
	 * @return <code>true</code> if the the collection has been modified, 
	 * <code>false</code> otherwise.
	 */
	public boolean remember(MetaExample<L> e);

	/**
	 * Removes an example from the memory.
	 * @param e The example to forget.
	 * @return <code>true</code> if the the collection has been modified, 
	 * <code>false</code> otherwise.
	 */
	public boolean forget(MetaExample<L> e);

	/**
	 * Calls into question the memory.
	 * <p>
	 * This method should filter examples from the memory.
	 * Each implementation can use different ways to provide a limited number of examples.
	 * </p>
	 * <p>
	 * If the example set size is not bounded, then the memory may have a space complexity really
	 * important, thus the solvers which iterates over it will have an unbounded theorical time
	 * complexity.
	 * </p>
	 * @return <code>true</code> if the the collection has been modified, 
	 * <code>false</code> otherwise.
	 */
	public boolean callIntoQuestion();

	/**
	 * Gets an iterator over the examples stored in the memory.
	 * @return An iterator.
	 */
	@Override public Iterator<MetaExample<L>> iterator();

};

