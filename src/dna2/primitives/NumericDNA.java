package ori.dna.primitives;

public abstract class NumericDNA implements DNA {

	@Override
	public void mutate() {

	}

	@Override
	public void crossover(final DNA dna) {
		if (dna instanceof NumericDNA) {
			this.crossover((NumericDNA)dna);
			return;
		}
		Lod.w(this,"crossover(final DNA)","wrong dna data type (numeric expected)");
	}

	public void crossover(final NumericDNA dna) {
			
	}

	abstract public int intValue();
	abstract public float floatValue();
	abstract public void setValue(final int v);
	abstract public void setValue(final float v);

};

