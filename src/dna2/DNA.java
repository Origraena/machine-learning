package ori.dna;

public interface DNA {

	public void mutate();
	public void crossover(final DNA dna);
	public DNA clone();

};

