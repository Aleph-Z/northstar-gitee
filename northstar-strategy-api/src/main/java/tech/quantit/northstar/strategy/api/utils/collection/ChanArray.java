package tech.quantit.northstar.strategy.api.utils.collection;

import cn.hutool.core.convert.Convert;

import java.util.Optional;

public class ChanArray<T> {

	private Object[] array;
	
	private int size;
	
	private int cursor;

	public ChanArray(int size) {
		this.array = new Object[size];
		this.size = size;
	}

	public static ChanArray of(int size) {
		return new ChanArray(size);
	}

	public static <T> ChanArray ofInit(int size, T t) {
		ChanArray chanArray = new ChanArray(size);
		for(int i=0; i<size; i++) {
			chanArray.update(t);
		}
		return chanArray;
	}
	
	public T get() {
		return get(0);
	}

/**
  * @Description: 由原来的传负数向前取值 改为传正数向前取值
  * @Author: lzy
  * Date: 2022/8/15 11:06
  */
	@SuppressWarnings("unchecked")
	public T get(int index) {
		return (T) array[getIndex(-index)];
	}
	
	/**
	 * 更新值，且光标增加1
	 * @param obj	返回旧值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Optional<T> update(T obj) {
		cursor = getIndex(1);
		T oldVal = (T) array[cursor]; 
		array[cursor] = obj;
		return Optional.ofNullable(oldVal);
	}

	/**
	 * 更新值，且光标不增加，覆盖当下原值
	 * @param obj	返回旧值
	 * @return
	 */
	public Optional<T> set(T obj) {
		cursor = getIndex(0);
		T oldVal = (T) array[cursor];
		array[cursor] = obj;
		return Optional.ofNullable(oldVal);
	}

	private int getIndex(int i) {
		return (cursor + size + i) % size;
	}
	
	public Object[] toArray() {
		Object[] result = new Object[size];
		for(int i=0; i<size; i++) {
			result[i] = get(-(i+1));
		}
		return result;
	}
	public double[] toDoubleArray() {
		double[] result = new double[size];
		for(int i=0; i<size; i++) {
			result[i] = Convert.toDouble(get(-(i+1)));
		}
		return result;
	}

	public int size() {
		return size;
	}
}
