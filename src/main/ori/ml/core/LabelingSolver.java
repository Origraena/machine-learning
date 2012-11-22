package ori.ml.core;

import java.util.Map;

/**
 * Solver for example-based machine learning.
 * @param L The labels data type.
 */
public interface LabelingSolver<L> {

	/**
	 * Selects the example set to considere.
	 * @param examples The base examples.
	 */
	public void setExampleSet(Iterable<MetaExample<L>> examples);

	/**
	 * Solves the machine learning by returning a stochastic map
	 * with an entry for each not null valued label.
	 */
	public Map<L,Float> solve(Example e);

	/**
	 * Really solves the problem by selecting the "probably"
	 * correct label.
	 */
	public L determineLabel(Example e);

}

