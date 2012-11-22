package ori.dna.primitives;

public class IntDNA implements DNA {

	public IntDNA() {
		_value = 0;
	}

	public IntDNA(final int value) {
		_value = value;
	}

	@Override
	public IntDNA mutate() {
			
	}

	@Override
	public IntDNA crossover(final DNA dna) {
		if (dna instanceof NumberDNA) 
	}

	@Override
	public IntDNA clone() {
		return new IntDNA(_value);
	}

	private int _value;

};

