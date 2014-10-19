package epusp.pcs.os.shared.model.attribute;

public interface IAttribute {
	
	public String getAttributeName();
	
	public Object getValue();
	
	public DataType getDataType();
	
	@Override
	public boolean equals(Object obj);
}
