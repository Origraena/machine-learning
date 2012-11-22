package ori.ml.core;

import ori.ml.solvers.KNNSolver;

import ori.ogapi.util.Log;

import java.util.Map;
import java.util.ArrayList;

public class Learner<L> {

	public Learner() { }

	public Learner(DeadMemory<L> dead, ViveMemory<L> vive, LabelingSolver<L> solver) {
		_dead = dead;
		_vive = vive;
		_solver = solver;
	}

	public L next(Example e, float s) {
		Log.d("Learner","next(Example,float)","labels = "+_labels);
		L label = null;
		Float f = null;
		Map<L,Float> probas;
		final int n = _labels.size();
		float p = (float)(Math.random());
		float tot = 0.f;
		int i = 0;
		_solver.setExampleSet(_dead);
		probas = _solver.solve(e);
		Log.d("Learner","next(Example,float)","probas = "+probas);
		Log.d("Learner","next(Example,float)","p = "+p+" n = "+n);
		while ((i < n) && (tot <= p)) {
			label = _labels.get(i).label;
			tot += (_labels.get(i).value * (1 - _learningCoef));
			f = probas.get(label);
			Log.d("Learner","next(Example,float)","i = "+i+" label = "+label
			      +" tot = "+tot + " f = "+f);
			if (f != null)
				tot += (f.floatValue() * _learningCoef);
			Log.d("Learner","next(Example,float)","i = "+i+" label = "+label
	     	      +" tot = "+tot + " f = "+f);
			i++;
		}

		MetaExample<L> m = _vive.next(new MetaExample<L>(e,label),s);
		if (m != null)
			_dead.remember(m);
		return label;
	}

	public void setLabels(Iterable<L> labels) {
		int cpt = 0;
		for (L l : labels)
			cpt++;
		_labels = new ArrayList<ValuedLabel>(cpt);
		int i = 0;
		for (L l : labels) {
			Log.d("Learner","setLabels(Iterable<L>)","l = "+l);
			_labels.add(new ValuedLabel(l,1.f / ((float)cpt)));
			i++;
		}
	}

	public void setLabels(Iterable<L> labels, float values[]) {
		_labels = new ArrayList<ValuedLabel>();
		int i = 0;
		for (L l : labels) {
			_labels.add(new ValuedLabel(l,values[i]));
			i++;
		}
	}

	public void setLearningCoef(float c) {
		if ((c < 0) || (c > 1)) {
			Log.w("Learner","setLearningCoef(float)","illegal value: "+c+ " (must be in [0;1])");
			return;
		}
		_learningCoef = c;
	}

	public void setSolver(LabelingSolver<L> solver) {
		_solver = solver;
	}

	public void setViveMemory(ViveMemory<L> vive) {
		_vive = vive;
	}

	public void setDeadMemory(DeadMemory<L> dead) {
		_dead = dead;
	}

	public String deadMemoryToString() {
		return _dead.toString();
	}

	// TODO
	public DeadMemory<L> deadMemory() {
		return _dead;
	}
	protected ViveMemory<L>         _vive;
	protected DeadMemory<L>         _dead;
	protected LabelingSolver<L>     _solver;
	
	private ArrayList<ValuedLabel>  _labels;
	private float                   _learningCoef = 0.9f;


	public class ValuedLabel {
		public ValuedLabel(L label) {
			this.label = label;
		}
		public ValuedLabel(L l, float p) {
			this.label = l;
			this.value = p;
		}
		@Override
		public String toString() {
			return "("+this.label+","+this.value+")";
		}
		public L label;
		public float value;
	};

};

