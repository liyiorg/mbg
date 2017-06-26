package com.github.liyiorg.mbg.bean.rmwarp;

import java.util.List;

public class RMWarp1<S, O, O1> extends RMWarp<S, O> {

	private O1 _one1;

	private List<O1> _many1;

	public O1 get_one1() {
		return _one1;
	}

	public void set_one1(O1 _one1) {
		this._one1 = _one1;
	}

	public List<O1> get_many1() {
		return _many1;
	}

	public void set_many1(List<O1> _many1) {
		this._many1 = _many1;
	}

}
