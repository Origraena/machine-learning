package ori.ml.memories;

import ori.ml.core.DeadMemory;
import ori.ml.core.FeaturesSpace;
import ori.ml.core.MetaExample;

import ori.ogapi.util.Log;

import java.util.Iterator;
import java.util.LinkedList;

public class ComplexDeadMemory<L> implements DeadMemory<L> {

	public static final int DEFAULT_CAPACITY = -1;

	public ComplexDeadMemory() {
		_capacity = DEFAULT_CAPACITY;
		_examples = new FeaturesSpace();
	}

	public ComplexDeadMemory(int capacity) {
		_capacity = capacity;
		_examples = new FeaturesSpace();
	}

	public int n() {
		return _examples.n();
	}

	public int capacity() {
		return _capacity;
	}

	public void setCapacity(int maxCap) {
		_capacity = maxCap;
		if (this.n() > _capacity) 
			this.callIntoQuestion();
	}

	/** {@inheritDoc} */
	@Override
	public boolean remember(MetaExample<L> e) {
		if (this.accept(e)) {
			Log.d("ComplexDeadMemory","remember(MetaExample)","add example = "+e);
			_examples.add(e);
			if ((_capacity >= 0) && (_examples.n() - _capacity >= _overCap))
				this.callIntoQuestion();
			return true;
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean forget(MetaExample<L> e) {
		return _examples.remove(e);
	}

	/** {@inheritDoc} */
	@Override
	public boolean callIntoQuestion() {
		if (_capacity < 0)
			return false;
		int q = _examples.n() - _capacity;
		Log.d("ComplexDeadMemory","callIntoQuestion()","q = "+q);
		if (q > 0)
			_examples.filter(q);
		return (q > 0);
	}

	public boolean accept(MetaExample<L> e) {
		return (e.value() >= _minValue);
	}

	@Override 
	public Iterator<MetaExample<L>> iterator() {
		return _examples.iterator();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("{\n");
		for (MetaExample<L> ve : _examples) {
			s.append(ve);
			s.append("\n");
		}
		s.append('}');
		return s.toString();
	}

	public void rememberExamples(String str) {
		// {\n \n}
		// _examples = new FeatureSpace<L>(); // do not forget 
		// old examples (better example selection...)
		String s = str.substring(2,str.length()-2);
		// one example for one line
		String sub[] = s.split("\n");
		for (String s2 : sub)
			this.remember(new MetaExample<L>(s2));
	}


	private FeaturesSpace<L>    _examples;
	private int                 _capacity;
	private int                 _overCap    = 0;
	private float               _minValue   = 1.f;

};

