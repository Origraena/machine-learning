package ori.ml.solvers;

import ori.ml.core.Example;
import ori.ml.core.MetaExample;
import ori.ml.core.LabelingSolver;

import ori.ogapi.util.LinkedListMap;
import ori.ogapi.util.Log;

import java.util.Map;
import java.util.LinkedList;

/**
 * Space complexity : O(K * size) ; time complexity : 2 * K * size^2 * |E| + K * dim
 * = O (size^2 * |E|)
 */
public class KNNSolver<L> implements LabelingSolver<L> {

	public KNNSolver() { 
		_k = 1;
	}

	public KNNSolver(int k) {
		_k = k;
	}

	public void setExampleSet(Iterable<MetaExample<L>> examples) {
		_examples = examples;
	}

	public Map<L,Float> solve(Example e) {
		if (e == null)
			return null;
		List N = new List(e);
		Map<L,Float> V = new LinkedListMap<L,Float>(); // this map can be reducted into a probabilistic list
		Float v;
		MetaExample<L> el;
		// set comparator
		for (MetaExample<L> ex : _examples)
			N.add(ex);
		float t = 0.f;
		float inc = 0.f;
		for (int i = 0 ; i < _k ; i++,t+=inc) {
			if (N.isEmpty())
				break;
			el = N.pop();
			v = V.get(el.label());
			inc = this.computeValue(el,e);
			if (v == null)
				v = new Float(inc);
			else
				v = new Float(v.floatValue() + inc);
			V.put(el.label(),v);
		}
		for (Map.Entry<L,Float> entry : V.entrySet())
			entry.setValue(new Float(entry.getValue().floatValue()/t));
		return V;
	}

	public L determineLabel(Example e) {
		Map<L,Float> m = this.solve(e);
		Log.d("KNNSolver","determineLabel(Example)","solve result: "+m);
		float max = 0.f;
		L l = null;
		for (Map.Entry<L,Float> entry : m.entrySet()) {
			if (entry.getValue().floatValue() > max) {
				max = entry.getValue().floatValue();
				l = entry.getKey();
			}
		}
		return l;
	}

	public void setK(int k) {
		if (k > 0)
			_k = k;
	}

	public float computeValue(MetaExample<L> el, Example e) {
		float d = this.computeDist(el.example(),e);
		if (d < 0.000001) {       // TODO
			Log.d("KNNSolver","computeValue(Example,Example)","d < 0.000001 : loss of precision");
			return 1000001.f;
		}
		//return (1.f / d);
		return (el.value() / d); // s / d^2 ou s^2/d^2 ie (s/d)^2
	}

	public float computeDist(Example e1, Example e2) {
		float d = 0.f;
		final int sizeMin = (e1.size() < e2.size()) ? e1.size() : e2.size();
		for (int i = 0 ; i < sizeMin ; i++)
			d += ((e1.get(i) - e2.get(i)) * (e1.get(i) - e2.get(i)));
		//Log.d("KNNSolver","computeDist(Example,Example)","e1 = "+e1);
		//Log.d("KNNSolver","computeDist(Example,Example)","e2 = "+e2);
		//Log.d("KNNSolver","computeDist(Example,Example)","d = "+d);
		return d;
	}


	private int _k;
	private Iterable<MetaExample<L>> _examples;

	
	protected class List extends LinkedList<MetaExample<L>> {
		public List(Example e) {
			_example = e;
		}
		public boolean add(MetaExample<L> e) {
			int i = 0;
			for (MetaExample<L> e2 : this) {
				if (computeDist(e.example(),_example) < computeDist(e2.example(),_example))
					break;
				i++;
			}
			if (i >= _k)
				return false;
			this.add(i,e);
			return true;
		}
		private Example _example;
	}

};

