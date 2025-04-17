package com.departement.fichedevoeux.config;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.engine.jdbc.env.spi.NameQualifierSupport;
import org.hibernate.type.SqlTypes;

import java.sql.Types;
public class SQLiteDialect extends Dialect {
	
	@SuppressWarnings("deprecation")
	public SQLiteDialect() {
        super();
        registerColumnTypes(Types.BIT, "boolean");
        registerColumnTypes(Types.TINYINT, "tinyint");
        registerColumnTypes(Types.SMALLINT, "smallint");
        registerColumnTypes(Types.INTEGER, "integer");
        registerColumnTypes(Types.BIGINT, "bigint");
        registerColumnTypes(Types.FLOAT, "float");
        registerColumnTypes(Types.REAL, "real");
        registerColumnTypes(Types.DOUBLE, "double");
        registerColumnTypes(Types.NUMERIC, "numeric");
        registerColumnTypes(Types.DECIMAL, "decimal");
        registerColumnTypes(Types.CHAR, "char");
        registerColumnTypes(Types.VARCHAR, "varchar");
        registerColumnTypes(Types.LONGVARCHAR, "longvarchar");
        registerColumnTypes(Types.DATE, "date");
        registerColumnTypes(Types.TIME, "time");
        registerColumnTypes(Types.TIMESTAMP, "timestamp");
        registerColumnTypes(Types.BINARY, "blob");
        registerColumnTypes(Types.VARBINARY, "blob");
        registerColumnTypes(Types.LONGVARBINARY, "blob");
        registerColumnTypes(Types.BLOB, "blob");
        registerColumnTypes(Types.CLOB, "clob");
        registerColumnTypes(Types.BOOLEAN, "boolean");
    }

    private void registerColumnTypes(int bigint, String string) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new SQLiteIdentityColumnSupport();
    }

    public boolean supportsIdentityColumns() {
        return true;
    }

    
    public String getIdentitySelectString(String table, String column, int type) {
        return "select last_insert_rowid()";
    }

    
    public String getIdentityColumnString(int type) throws MappingException {
        return "integer";
    }

    @Override
    public boolean hasAlterTable() {
        return false;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public String getDropForeignKeyString() {
        return "";
    }

    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable,
                                                   String[] primaryKey, boolean referencesPrimaryKey) {
        return "";
    }

    @Override
    public String getAddPrimaryKeyConstraintString(String constraintName) {
        return "";
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }

    @Override
    public NameQualifierSupport getNameQualifierSupport() {
        return NameQualifierSupport.NONE;
    }

}
