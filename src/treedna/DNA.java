package ori.dna;

public class DNA {

	
	public void mutate() {
		for (DNA dna : _next) {
			dna.mutate();
		}
		this.doMutate();
	}

	protected void doMutate() {
	}

};

