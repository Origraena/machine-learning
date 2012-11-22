package ori.ml.core;

import ori.ogapi.util.FloatArray;
import ori.ogapi.util.IntArray;
import ori.ogapi.util.Log;

import java.util.NoSuchElementException;
import java.util.LinkedList;

/**
 * The vive memory may be seend as the waiting list of the memory.
 * <p>
 * This memory may be seen as a list with different markers.<br />
 * A marker notes a place of the list where examples change of state.
 * Thus an example is added in the first place of the list. It will be
 * pushed through the full list, and its value (meaning its satisfaction value)
 * is modified during this process.
 * Each different state (= step) changes the weight of the current tick satisfaction
 * value.
 * </p>
 * <p>
 * If the learning reinforcement must optimise long term consequences, then 
 * there will be one marker at the end of the short term duration.
 * Thus there will be two different coefs, the first one will be valued to <code>0.f</code>
 * (since short term consequences does not matter), and the second to <code>1.f</code>
 * (hence long term consequences not be reduced).
 * </p>
 * @param L The labels data type.
 */
public class ViveMemory<L> {

	public ViveMemory() {
		_examples = new LinkedList<LinkedList<MetaExample<L>>>();
		_durations = new IntArray();
		_coefs = new FloatArray();
		_valid = false;
	}

	/**
	 * Adds a step of specified duration (the modifier will be set to <code>1.f</code>.
	 * @see #addStep(int,float)
	 */
	public void addStep(int duration) {
		this.addStep(duration,1.f);
	}

	/**
	 * Adds a step of specified duration and satisfaction modifier.
	 * <p>
	 * In details, a new step is added at the end of the list. All examples which
	 * already are in the list will go through this new one to go to the dead memory.
	 * It means, that after this operation, there will be <code>d</code> ticks (ie, d
	 * calls to <code>next(...)</code> method), before the next method will return other
	 * value than <code>null</code>.
	 * </p>
	 * @param d The duration of the step (in number of ticks).
	 * @param coef The satisfaction modifier (which should be in [0;1]).
	 */
	public void addStep(int d, float coef) {
		_durations.add(d);
		_coefs.add(coef);
		_examples.add(new LinkedList<MetaExample<L>>());
		_valid = false;
	}

	public boolean removeStep(int index) {
		try {
			_durations.removeAt(index);
			_coefs.removeAt(index);
			_examples.remove(index);
			return true;
		}
		catch (NoSuchElementException e) {
			return false;
		}
	}

	public void removeAllSteps() {
		_durations.removeAll();
		_coefs.removeAll();
		_examples = new LinkedList<LinkedList<MetaExample<L>>>();
	}

	public void setSteps(IntArray d, FloatArray c) {
		_durations = d;
		_coefs = c;
		_examples = new LinkedList<LinkedList<MetaExample<L>>>();
		final int length = d.size();
		for (int i = 0 ; i < length ; i++)
			_examples.add(new LinkedList<MetaExample<L>>());
	}

	/**
	 * Changes the modifier of a step.
	 * @param step The number of the step.
	 * @param coef The new modifier to set.
	 */
	public void setStepCoef(int step, float coef) {
		_coefs.set(step,coef);
	}

	/**
	 * Injects a new example into the memory and pop the one which have gone through
	 * each step list.
	 * @param e The new "lived" example.
	 * @param s The satisfaction value at the current moment.
	 * @return The oldest example if it have been through all steps, <code>null</code>
	 * otherwise.
	 */
	public MetaExample<L> next(MetaExample<L> e, float s) {
		this.computeValues(s);
		return this.processNext(e);
	}

	/**
	 * Computes the next example.
	 * <p>
	 * Adds the new example at the start of the waiting list, and make all examples to
	 * "walk" to the next place (which may be on the next step).
	 * If the oldest example is thrown away from the list, then it is returned.
	 * </p>
	 * @param e The new "lived" example.
	 * @return The oldest example if it have been through all steps, <code>null</code>
	 * otherwise.
	 */
	protected MetaExample<L> processNext(MetaExample<L> e) {
		MetaExample<L> res = e;
		if (!_valid) {
			int step = 0;
			for (LinkedList<MetaExample<L>> list : _examples) {
				list.addFirst(res);
				if (list.size() > _durations.get(step))
					res = list.pollLast();
				else {
					res = null;
					break;
				}
			}
		}
		else {
			for (LinkedList<MetaExample<L>> list : _examples) {
				list.addFirst(res);
				res = list.pollLast();
			}
		}
		return res;
	}
	
	/**
	 * Computes the new values of examples from the current satisfaction value.
	 * <p>
	 * This method just iterates over all examples and add to their value the current
	 * satisfaction value modifed by the corresponding step coefficient.
	 * </p>
	 * @param s The satisfaction value at the current moment.
	 */
	protected void computeValues(float s) {
		int step = 0;
		for (LinkedList<MetaExample<L>> stepExamples : _examples) {
			for (MetaExample<L> e : stepExamples)
				e.incValue(_coefs.get(step)*s);
			step++;
		}
	}
	
	private LinkedList<LinkedList<MetaExample<L>>> _examples;
	private IntArray _durations;
	private FloatArray _coefs;
	private boolean _valid;

};

