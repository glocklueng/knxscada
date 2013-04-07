package pl.marek.knx.interfaces;

import java.util.List;

public interface Dao<T> {
	long save(T object);
	void update(T object);
	void delete(T object);
	T get(Object id);
	List<T> getAll();
}