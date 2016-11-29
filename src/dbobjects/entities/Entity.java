package dbobjects.entities;

import dbobjects.DbObject;

import java.math.BigInteger;

public interface Entity extends DbObject {
    BigInteger getId();
}