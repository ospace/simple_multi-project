package com.tistory.ospace.simpleproject.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CollectionRS<T> {
	private int total;
	private Collection<T> data;
	  
	public static <T> CollectionRS<T> of(Collection<T> data, int total) {
	    CollectionRS<T> ret = new CollectionRS<T>();
		ret.setData(data);
		ret.setTotal(total);
		return ret;
	}

    public int getTotal() {
      return total;
    }

    public void setTotal(int total) {
      this.total = total;
    }

    public Collection<T> getData() {
      return data;
    }

    public void setData(Collection<T> data) {
      this.data = data;
    }
}
