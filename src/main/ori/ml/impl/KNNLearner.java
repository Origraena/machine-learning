package ori.ml.impl;

import ori.ml.core.Learner;
import ori.ml.core.ViveMemory;
import ori.ml.memories.ComplexDeadMemory;
import ori.ml.solvers.KNNSolver;

import ori.ogapi.util.IntArray;
import ori.ogapi.util.FloatArray;
import ori.ogapi.util.Log;

public class KNNLearner<L> extends Learner<L> {

	public static final int DEFAULT_K   = 7;
	public static final int DEFAULT_CAP = -1;

	public KNNLearner() {
		super();
		this.init();
	}

	public KNNLearner(final int k,
	                  final int cap,
	                  final float l) {
		super();
		this.init();
		this.setUp(k,cap,l);
	}

	public KNNLearner(final int k,
	                  final int cap,
	                  final IntArray durations,
	                  final FloatArray coefs,
	                  final float l) {
		super();
		this.init();
		this.setUp(k,cap,durations,coefs,l);
	}

	protected void init() {
		super.setSolver(new KNNSolver<L>());
		super.setDeadMemory(new ComplexDeadMemory<L>());
		super.setViveMemory(new ViveMemory<L>());
	}
	
	public void setUp(final int     k,
	                  final int     cap,
	                  final float   l) {
		this.setK(k);
		this.setCapacity(cap);
		this.setLearningCoef(l);
	}


	public void setUp(final int     k,
	                  final int     cap,
	                  final IntArray     durations,
	                  final FloatArray   coefs,
	                  final float   l) {
		this.setK(k);
		this.setCapacity(cap);
		this.setSteps(durations,coefs);
		this.setLearningCoef(l);
	}



	public void setK(final int k) {
		try {
			((KNNSolver<L>)_solver).setK(k);
		}
		catch (Exception e) {
			Log.e(this,"setK(int)","cannot set up solver with k = "+k);
			Log.e(this,"setK(int)",e.getMessage());
		}
	}

	public void setCapacity(final int maxCapacity) {
		try {
			((ComplexDeadMemory<L>)_dead).setCapacity(maxCapacity);
		}
		catch (Exception e) {
			Log.e(this,"setCap(int)","cannot set up dead memory with capacity = "+maxCapacity);
			Log.e(this,"setCap(int)",e.getMessage());
		}
	}

	public int memorySize() {
		try {
			return ((ComplexDeadMemory)_dead).n();
		}
		catch (Exception e) {
			return -1;
		}
	}

	public void setSteps(final IntArray durations,
	                     final FloatArray coefs) {
		_vive.setSteps(durations,coefs);
	}

	public void addStep(final int d, final float c) {
		_vive.addStep(d,c);
	}


};

