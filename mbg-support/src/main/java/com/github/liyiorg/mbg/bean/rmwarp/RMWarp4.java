package com.github.liyiorg.mbg.bean.rmwarp;

import java.util.List;

public class RMWarp4<S, O, O1, O2, O3, O4> extends RMWarp3<S, O, O1, O2, O3> {

	private O4 _one4;

	private List<O4> _many4;

	public O4 get_one4() {
		return _one4;
	}

	public void set_one4(O4 _one4) {
		this._one4 = _one4;
	}

	public List<O4> get_many4() {
		return _many4;
	}

	public void set_many4(List<O4> _many4) {
		this._many4 = _many4;
	}

}
