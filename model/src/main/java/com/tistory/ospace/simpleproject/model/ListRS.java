package com.tistory.ospace.simpleproject.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ListRS<T> {
	private int total;
	private List<T> data;
	  
	public static <T> ListRS<T> of(List<T> data, int total) {
		ListRS<T> ret = new ListRS<T>();
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

    public List<T> getData() {
      return data;
    }

    public void setData(List<T> data) {
      this.data = data;
    }
}
