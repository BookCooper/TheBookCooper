package com.thebookcooper.util;

import java.sql.*;

public abstract class DataAccessObject <T extends DataTransferObject> {

    protected final Connection connection;


    public DataAccessObject(Connection connection) {
        super();
        this.connection = connection;
    }

    public abstract T findById(long id);
    public abstract T create(T dto);
}
