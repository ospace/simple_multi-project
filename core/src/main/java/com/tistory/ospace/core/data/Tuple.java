package com.tistory.ospace.core.data;

import com.tistory.ospace.core.util.CmmUtils;

public class Tuple<L,R> {
	public final L left;
	public final R right;
	
	public static <T,U> Tuple<T,U> of(T left, U right) {
		return new Tuple<>(left, right);
	}
	
	public Tuple(L left, R right) {
		this.left = left;
		this.right = right;
	}
	
	@SuppressWarnings("unchecked")
	public boolean equls(Object other) {
		if (other == this) return true;
		if (!(other instanceof Tuple)) return false;
		Tuple<L,R> tuple = (Tuple<L,R>) other;
		return (tuple.left.equals(this.left) && tuple.right.equals(this.right));
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (null==left?0:left.hashCode());
		result = prime * result + (null==right?0:right.hashCode());
		return result;
	}
	
	public String toString() {
		return CmmUtils.toString(this);
	}
}
