package com.github.liyiorg.mbg.bean.rmwarp;

import java.util.List;

public class RMWarp3<S, O, O1, O2, O3> extends RMWarp2<S, O, O1, O2> {

	private O3 _one3;

	private List<O3> _many3;

	public O3 get_one3() {
		return _one3;
	}

	public void set_one3(O3 _one3) {
		this._one3 = _one3;
	}

	public List<O3> get_many3() {
		return _many3;
	}

	public void set_many3(List<O3> _many3) {
		this._many3 = _many3;
	}

}
