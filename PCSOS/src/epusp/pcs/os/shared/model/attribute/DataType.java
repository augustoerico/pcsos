package epusp.pcs.os.shared.model.attribute;

import java.io.Serializable;

public enum DataType implements Serializable {

	BOOLEAN,
	BOOLEAN_ARRAY,
	DATE,
	DATE_ARRAY,
	STRING,
	STRING_ARRAY,
	INTEGER,
	INTERGER_ARRAY,
	FLOAT,
	FLOAT_ARRAY;
	
	/*
	 * Seen by Serializable
	 */
	DataType(){
	}
	
}
