package com.github.liyiorg.mbg.bean.rmwarp;

import java.util.List;

public class RMWarp5<S, O, O1, O2, O3, O4, O5> extends RMWarp4<S, O, O1, O2, O3, O4> {

	private O5 _one5;

	private List<O5> _many5;

	public O5 get_one5() {
		return _one5;
	}

	public void set_one5(O5 _one5) {
		this._one5 = _one5;
	}

	public List<O5> get_many5() {
		return _many5;
	}

	public void set_many5(List<O5> _many5) {
		this._many5 = _many5;
	}

}
