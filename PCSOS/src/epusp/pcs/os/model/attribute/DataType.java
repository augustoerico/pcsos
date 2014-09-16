package epusp.pcs.os.model.attribute;

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
	FLOAT_ARRAY,
	TIPO_SANGUINEO;
	
	/*
	 * Seen by Serializable
	 */
	DataType(){
	}
	
}
