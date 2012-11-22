package ori.ml.core;

import ori.ogapi.util.Log;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/// Note :
//vite très stable => liste triée pour les sommets ?
// + facile à selectionner le plus faible
/**
 * A feature space is a d-dimensional space where examples are
 * stored as points.
 * <p>
 * Whenever an example is added to the space, its nearest
 * neighbour (with different associated label)
 * is computed in terms of euclidian distance along this space.
 * </p>
 * <p>
 * Furthermore, each point p also stores its entering neighbours,
 * ie, the points whose nearest neighbour is p.
 * </p>
 * <p>
 * The filtering methods are used to limit the feature space
 * size. The method is to eliminate one of the points connected
 * to the shortest edge.
 * </p>
 */
public class FeaturesSpace<L> implements Iterable<MetaExample<L>> {

	public FeaturesSpace() { }

	public int n() {
		return _n;
	}

	public void add(MetaExample<L> e) {
		Point pe = new Point(e);
		for (Point p : _points) {
			// is pe a better neighbour for p ?
			p.add(pe);
			// is p a better neighbour for pe ?
			pe.add(p);
		}
		_points.add(pe);
		_n++;
	}

	public void computes() {
		for (Point p : _points) {
			for (Point q : _points) {
				p.add(q);
				q.add(p);
			}
		}
	}

	public boolean remove(MetaExample<L> e) {
		return this.remove(this.pointOf(e));
	}

	protected Point pointOf(MetaExample<L> e) {
		for (Point p : _points) {
			if (p.example() == e)
				return p;
		}
		return null;
	}

	protected boolean remove(Point p) {
		if (p == null)
			return false;
		Point q;
		_points.remove(p);
		_n--;
		LinkedList<Point> in = p.in();
		Log.d("in: "+in);
		while (!(in.isEmpty())) {
			q = in.pollLast();
			q.unout();
			for (Point p2 : _points)
				q.add(p2);
		}
		Log.d(this,"p.out = "+p.out());
		if (p.out() != null)
			p.out().removeIn(p);
		return true;
	}

	public void filter() {
		if (_n == 0)
			return;
		Point p = this.selectMin();
		Log.d(this,"pmin = "+p);
		this.remove(p);
	}

	public void filter(int k) {
		for (int i = 0 ; i < k ; i++)
			this.filter();
	}

	@Override
	public Iterator iterator() {
		return new Iterator(_points.iterator());
	}

	protected Point selectMin() {
		Point min = null;
		float dmin = 1.1f;
		for (Point p : _points) {
			if (p.weight() < dmin) {
				min = p;
				dmin = p.weight();
			}
		}
		return min;
	}


	private LinkedList<Point> _points = new LinkedList<Point>();
	private int               _n = 0;

	public class Iterator implements java.util.Iterator<MetaExample<L>> {
		public Iterator(java.util.Iterator<Point> iterator) {
			_iterator = iterator;
		}
		@Override
		public boolean hasNext() {
			return _iterator.hasNext();
		}
		@Override
		public MetaExample<L> next() throws NoSuchElementException {
			return _iterator.next().example();
		}
		@Override
		public void remove() throws /*NoSuchElementException,*/UnsupportedOperationException {
			throw new UnsupportedOperationException("cannot removes example from features space");
		}
		private java.util.Iterator<Point> _iterator;
	}

	private class Point {

		public Point(MetaExample<L> e) {
			_example = e;
			_in = new LinkedList<Point>();
			_out = null;
		}

		public boolean add(Point p) {
			if (p == this)
				return false;
			float d = this.computeDist(p);
			if (this.isBetter(p)) {
				p.in().add(this);
				if (this._out != null)
					this._out.removeIn(this);
				_out = p;
				_weight = d;
				return true;
			}
			return false;
		}

		protected boolean isBetterRaw(Point p) {
			return ((this._out == null) 
			     || (this.computeDist(p) < _weight));
		}

		protected boolean isBetter(Point p) {
			return ((this.example().label() == p.example().label())
			     && ((_out == null)
			      || (this.computeDist(p) < _weight)));
		}

		public void removeIn(Point p) {
			_in.remove(p);
		}

		public void unout() {
			_out = null;
			_weight = 0.f;
		}

		public float computeDist(Point p) {
			float d = 0.f;
			final int s = 
				(p.example().size() < _example.size()) ? 
				p.example().size() :
				_example.size();
			for (int i = 0 ; i < s ; i++)
				d += ((p.example().get(i) - _example.get(i))
			    	* (p.example().get(i) - _example.get(i)));
			return d;
		}

		public MetaExample<L> example() {
			return _example;
		}

		public float weight() {
			return _weight;
		}

		public Point out() {
			return _out;
		}
		public LinkedList<Point> in() {
			return _in;
		}

		@Override
		public String toString() {
			return "p:"+_example.toString()+":"+_weight;
		}

		private MetaExample<L> _example;
		private LinkedList<Point> _in;
		private Point _out;
		private float _weight = 0.f;

	};

};

