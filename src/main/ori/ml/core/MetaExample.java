package ori.ml.core;

import ori.ogapi.util.Log;

/**
 * A metaexample is a meta structure defined by an example and its
 * associated label and value.
 * @param L The labels data type.
 */
public class MetaExample<L> {

	public MetaExample(String str) {
		this.fromString(str);
	}

	public MetaExample(Example e) {
		_example = e;
		_label = null;
		_value = 0.f;
	}

	public MetaExample(Example e, L l) {
		_example = e;
		_label = l;
		_value = 0.f;
	}

	public MetaExample(Example e, float value) {
		_example = e;
		_label = null;
		_value = value;
	}

	public MetaExample(Example e, L l, float value) {
		_example = e;
		_label = l;
		_value = value;
	}

	public Example example() {
		return _example;
	}

	public int size() {
		return _example.size();
	}

	public void set(int f, float v) {
		_example.set(f,v);
	}

	public float get(int f) {
		return _example.get(f);
	}

	public L label() {
		return _label;
	}

	public void setLabel(L l) {
		_label = l;
	}

	public float value() {
		return _value;
	}

	public void setValue(float v) {
		_value = v;
	}

	public void incValue(float v) {
		_value += v;
	}

	@Override
	public String toString() {
		return "("+_example+";"+_label+";"+_value+")";
	}

	// TODO
	// remplacer fromString par fromStream
	// _label = (L)readObject()
	public void fromString(String str) {
		_example = null; _label = null; _value = 0.f;
		String s = str.substring(1,str.length()-1);
		String sub[] = s.split(";"); 
		if (sub == null) 
			return;
		if (sub.length >= 1)
			_example = new Example(sub[0]);
		if (sub.length >= 2) {
			//_label = null;// TODO
			try { _label = (L)new Integer(Integer.parseInt(sub[1])); }
			catch (Exception e) { _label = null; Log.e(this,"fromString(String)",""+e); }
		}
		if (sub.length >= 3) {
			try { _value = Float.parseFloat(sub[2]); }
			catch (Exception e) { _value = 0.f; }
		}
	}


	private Example _example;
	private L       _label;
	private float   _value;      // in [0;1]

}; 
