package com.tistory.ospace.core.data;

import java.util.Objects;

import com.tistory.ospace.core.util.CmmUtils;

public class Pair<F, S> {
	private F first;
	private S second;
	
	public static <F,S> Pair<F,S> of(F f, S s) {
		return new Pair<F,S>(f,s);
	}
	
	public Pair() {}
	
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public F getFirst() {
		return first;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public S getSecond() {
		return second;
	}

	public void setSecond(S second) {
		this.second = second;
	}
	
	@Override
	public boolean equals(final Object other) {
		if(this == other) return true;
		
		if (!(other instanceof Pair)) return false;
		if (hashCode() != other.hashCode()) return false;
		
		final Pair<?, ?> pair = Pair.class.cast(other);
		
		return Objects.equals(first, pair.first)
		    && Objects.equals(second, pair.second);
	}
	 
	@Override
	public int hashCode() {
		return CmmUtils.hashCode(first, second);
	}
	
	@Override
	public String toString() {
		return CmmUtils.toString(this);
	}
}
