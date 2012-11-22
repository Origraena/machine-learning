package ori.ml;

import ori.ogapi.util.Log;
import ori.ml.core.*;
import ori.ml.memories.*;
import ori.ml.solvers.*;
import ori.ml.impl.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.io.PrintWriter;

public class Main {
	public static int K = 1;        // # neighbours in features space
	public static int dim = 5;      // problem dimension
	public static float L = 0.9f;   // learning coef
	public static int C = -1;       // capacity
	public static int n = 1;       // # series
	public static int m = 50;      // # tests by serie

	public static float s(Example e, Integer l) {
		float lastMax = 0.f;
		int index = -1;
		for (int i = 0 ; i < e.size() ; i++) {
			if (e.get(i) >= lastMax) {
				lastMax = e.get(i);
				index = i;
			}
		}
		float result;
		int li = l.intValue();
		int ecart = (li > index) ? li - index : index - li;
		result = ((float)ecart) / ((float)(e.size()));
		//System.out.println("result of e = "+e+" with l = "+l+" is "+result);
		return 1.f - result;
	}

	public void l() {
		Log.l(Log.DEBUG,this,"method","msg");
		Log.l(Log.DEBUG,this,"msg2");
	}

	public static String printMemory(Iterable<MetaExample<Integer>> examples) {
		StringBuilder s = new StringBuilder();
		String t;
		for (MetaExample e : examples) {
			s.append(""+e.get(0) + " "+e.get(1));
			s.append("\n");
		}
		return s.toString();
	}

	public static void printTo(Iterable<MetaExample<Integer>> dead, int j) {
		try {
			PrintWriter printer = new PrintWriter("mem"+(j+1)+".res");
			printer.println(printMemory(dead));
			printer.close();
		}
		catch (Exception excepo) {
			System.err.println("error file");
		}
	}

	public static void main(String args[]) {

		// Displays meta infos
		System.out.println("k = "+K);
		System.out.println("d = "+dim);
		System.out.println("l = "+L);
		
		// uncomment to disable logging
		Log.disable();

		// labels generation
		LinkedList<Integer> labels = new LinkedList<Integer>();
		for (int i = 0 ; i < dim ; i++)
			labels.add(new Integer(i));

		// learner instantiation
		KNNLearner<Integer> learner = 
			new KNNLearner<Integer>(K,C,L);
		learner.setLabels(labels);
		learner.addStep(1,1.f);

		Integer l = null;           // label
		Example e = null;           // example
		int pts;                    // points
		float v = 0.f;              // satisfaction value
		int total = 0;              // total points
		long time = 0;              // time of a serie in ms
		long tottime = 0;           // total time if ms
		System.out.println("nb tests = "+n+" series of "+m);
		for (int j = 0 ; j < n ; j++) {
			System.out.println("serie: "+j);
			pts = 0;
			time = System.currentTimeMillis();
			for (int nb = 0 ; nb < m ; nb++) {
				e = new Example(dim);
				for (int k = 0 ; k < dim ; k++)
					e.set(k,(float)(Math.random()));
				l = learner.next(e,v);
				v = s(e,l);
				if (v == 1.f)
					pts++;
			}
			time = System.currentTimeMillis() - time;
			System.out.println("\ttime = "+ time);
	   	   	System.out.println("\tpoints = "+pts+" / " + m);
			float ratio = (float)pts / (float)m;
			System.out.println("\tratio = "+ratio);
			System.out.println("\tmemory size = "+learner.memorySize());
			total += pts;
			tottime += time;
	//		printTo(dead,j);        // 2D
		}

		float r2 = (float)total / (float) (m * n);
		System.out.println("Total:");
		System.out.println("k = "+K);
		System.out.println("d = "+dim);
		System.out.println("l = "+L);
		System.out.println("\ttotal points = " + total + " / " + (m * n));
		System.out.println("\tmemory size = "+learner.memorySize());
		System.out.println("\ttotal ratio = "+r2);
		System.out.println("\ttotal time = "+tottime);
		System.out.println("\taverage time = "+(tottime/n));
		System.out.println("\taverage unit time = "+(tottime/(n*m)));
		System.out.println("\tDead Memory:\n"+learner.deadMemoryToString());


		((ComplexDeadMemory<Integer>)(learner.deadMemory())).rememberExamples(learner.deadMemoryToString());
		System.out.println("\tAfter:\n"+learner.deadMemoryToString());
/*
		Log.d("test 1 debug");
		Log.i("test 2 info");
		Log.w("test 3 warning");
		Log.e("test 4 error");
		Log.r("test 5 result");

		Main m= new Main();
		m.l();*/
	}

};

