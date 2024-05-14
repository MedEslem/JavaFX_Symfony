package services;

import java.sql.SQLException;
import java.util.Set;

public interface IService2<T> {

    void add(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(int id) throws SQLException;

    Set<T> getAll() throws SQLException;

    T getById(int id) throws  SQLException;
}