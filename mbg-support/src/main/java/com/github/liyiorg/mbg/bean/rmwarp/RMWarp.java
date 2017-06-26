package com.github.liyiorg.mbg.bean.rmwarp;

import java.util.List;

/**
 * ResultMap 复杂结果集包装
 * 
 * @author LiYi
 *
 * @param <S>
 * @param <O>
 */
public class RMWarp<S, O> {

	private S _self;

	private O _one;

	private List<O> _many;

	private Object id, id1, id2, id3, id4, id5, id6, id7, id8, id9;

	public S get_self() {
		return _self;
	}

	public void set_self(S _self) {
		this._self = _self;
	}

	public O get_one() {
		return _one;
	}

	public void set_one(O _one) {
		this._one = _one;
	}

	public List<O> get_many() {
		return _many;
	}

	public void set_many(List<O> _many) {
		this._many = _many;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getId1() {
		return id1;
	}

	public void setId1(Object id1) {
		this.id1 = id1;
	}

	public Object getId2() {
		return id2;
	}

	public void setId2(Object id2) {
		this.id2 = id2;
	}

	public Object getId3() {
		return id3;
	}

	public void setId3(Object id3) {
		this.id3 = id3;
	}

	public Object getId4() {
		return id4;
	}

	public void setId4(Object id4) {
		this.id4 = id4;
	}

	public Object getId5() {
		return id5;
	}

	public void setId5(Object id5) {
		this.id5 = id5;
	}

	public Object getId6() {
		return id6;
	}

	public void setId6(Object id6) {
		this.id6 = id6;
	}

	public Object getId7() {
		return id7;
	}

	public void setId7(Object id7) {
		this.id7 = id7;
	}

	public Object getId8() {
		return id8;
	}

	public void setId8(Object id8) {
		this.id8 = id8;
	}

	public Object getId9() {
		return id9;
	}

	public void setId9(Object id9) {
		this.id9 = id9;
	}

}
