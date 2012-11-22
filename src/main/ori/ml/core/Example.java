package ori.ml.core;

/**
 * An example is represented by a vector of floats, each
 * one corresponding to a feature.
 */
public class Example {

	public Example(int size) {
		_features = new float[size];
	}

	public Example(String str) {
		this.fromString(str);
	}

	public int size() {
		return _features.length;
	}

	public void set(int f, float v) {
		_features[f] = v;
	}

	public float get(int f) {
		return _features[f];
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		final int length = _features.length;
		s.append('(');
		// TODO round example string ?
		for (int i = 0 ; i < length -1 ; i++) {
			s.append(_features[i]);
			s.append(',');
		}
		if (length > 0)
			s.append(_features[length-1]);
		s.append(')');
		return s.toString();
	}

	public void fromString(String str) {
		_features = null;
		// ( )
		String s = str.substring(1,str.length()-1);
		// ,
		String sub[] = s.split(",");
		if ((sub == null) || (sub.length == 0))
			return;
		_features = new float[sub.length];
		int i = 0;
		for (String s2 : sub) {
			try { _features[i] = Float.parseFloat(s2); }
			catch (Exception e) { _features[i] = 0.f; }
			i++;
		}
	}

	private float _features[];

};

