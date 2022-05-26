package com.example.demo.exception;

import java.sql.SQLException;

public class ForeignKeyConstraintSqlException extends SQLException {
    public ForeignKeyConstraintSqlException(String message) {
        super(message);
    }
}
