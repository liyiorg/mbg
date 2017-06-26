package com.github.liyiorg.mbg.bean.rmwarp;

import java.util.List;

public class RMWarp2<S, O, O1, O2> extends RMWarp1<S, O, O1> {

	private O2 _one2;

	private List<O2> _many2;

	public O2 get_one2() {
		return _one2;
	}

	public void set_one2(O2 _one2) {
		this._one2 = _one2;
	}

	public List<O2> get_many2() {
		return _many2;
	}

	public void set_many2(List<O2> _many2) {
		this._many2 = _many2;
	}

}
