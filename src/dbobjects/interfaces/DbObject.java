package dbobjects.interfaces;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Vergir on 05/11/2016.
 */
public interface DbObject extends Serializable {
    BigInteger getId();
}
