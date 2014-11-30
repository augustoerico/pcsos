package epusp.pcs.os.shared.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;

public class AttributeInfoLoader {
	
	public interface IAttributeInfoLoaded{
		public void onCustomAttributesLoaded();
	}
	
	private IConnectionServiceAsync rpcService = null;
	private HashMap<String, AttributeInfo> cache = null;

	public AttributeInfoLoader(HashMap<String, AttributeInfo> cache, IConnectionServiceAsync rpcService){
		this.rpcService = rpcService;
		this.cache = cache;
	}
	
	public void loadCustomAttributes(ICustomAttributes[] customAttributes, final IAttributeInfoLoaded attributesLoaded){
		List<ICustomAttributes> customAttributesList = new ArrayList<ICustomAttributes>();

		for(ICustomAttributes customAttribute : customAttributes)
			if(!cache.containsKey(customAttribute.getAttributeName()))
				customAttributesList.add(customAttribute);

		if(!customAttributesList.isEmpty()){
			rpcService.getCustomAttributesInfo(customAttributesList.toArray(new ICustomAttributes[customAttributesList.size()]), new AsyncCallback<List<AttributeInfo>>() {

				@Override
				public void onSuccess(List<AttributeInfo> result) {
					for(AttributeInfo attributeInfo : result)
						cache.put(attributeInfo.getAttributeName(), attributeInfo);
					
					attributesLoaded.onCustomAttributesLoaded();
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});
		}else
			attributesLoaded.onCustomAttributesLoaded();
	}

}
