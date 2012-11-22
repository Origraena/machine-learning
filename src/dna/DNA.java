package ori.dna;

public class DNA {

	public void mutate() {
		for (Gene g : _genes)
			//if (this.willMutate(g))
				g.mutate();
	}
	
	public void crossover(final DNA dna) {
		Iterator<Gene> g1 = _genes.iterator();
		Iterator<Gene> g2 = _
		for (Gene g : _genes)
			//if (this.willCross
			g.crossover
	}

	private ArrayList<Gene> _genes = new ArrayList<Gene>();

};

