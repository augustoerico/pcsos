package epusp.pcs.os.shared.client.presenter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.client.view.ArrayInput;
import epusp.pcs.os.shared.client.view.ArrayInput.WidgetFactory;
import epusp.pcs.os.shared.client.view.UploadPanel;
import epusp.pcs.os.shared.model.SystemObject;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.attribute.IAttribute;
import epusp.pcs.os.shared.model.attribute.types.BooleanAttribute;
import epusp.pcs.os.shared.model.attribute.types.DateAttribute;
import epusp.pcs.os.shared.model.attribute.types.FloatAttribute;
import epusp.pcs.os.shared.model.attribute.types.IntegerAttribute;
import epusp.pcs.os.shared.model.attribute.types.StringAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.BooleanArrayAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.DateArrayAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.FloatArrayAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.IntegerArrayAttribute;
import epusp.pcs.os.shared.model.attribute.types.arrays.StringArrayAttribute;
import epusp.pcs.os.shared.model.exception.AttributeCastException;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;

public class CreateUpdatePresenter implements Presenter{

	public interface Display{
		Widget asWidget();
		void setPictureUrl(String safeUri);
		void hideAdd();
		void showAdd();
		void addPictureAddHandler(ClickHandler handler);
		void hidePicture();
		void showPicture();
		void addMouseOverHandler(MouseOverHandler handler);
		void addMouseOutHandler(MouseOutHandler handler);
		void setSaveText(String text);
		void setCancelText(String text);
		void addSaveClickHandler(ClickHandler handler);
		void addCancelClickHandler(ClickHandler handler);
		Boolean isPictureVisible();
		void addPrimaryAttribute(String label, Boolean required, Widget w);
		void addSecondaryAttribute(String label, Boolean required, Widget w);
		void removeSecondaryAttribute(Widget w);
		void setSaveEnabled(Boolean enabled);
		void clearSecondaryAttributes();
	}

	private final IConnectionServiceAsync rpcService;
	private final Display view;
	private final CommonWorkspaceConstants constants;

	private final DecoratedPopupPanel popup = new DecoratedPopupPanel(false, false);

	private final List<AttributeInfo> customAttributes;

	private final String locale = Window.Location.getParameter("locale");

	private final HashMap<String, Widget> customWidgets = new HashMap<String, Widget>();

	private String url;

	public CreateUpdatePresenter(IConnectionServiceAsync rpcService, Display view, CommonWorkspaceConstants constants, List<AttributeInfo> customAttributes){
		this.rpcService = rpcService;
		this.view = view;
		this.constants = constants;
		this.customAttributes = customAttributes;
		popup.setGlassEnabled(true);
	}	


	@Override
	public void go(HasWidgets container) {
		container.clear();		
		container.add(view.asWidget());
		view.setSaveText(constants.save());
		view.setCancelText(constants.cancel());
		bind();
	}

	private void bind(){		
		view.addPictureAddHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {				
				UploadPanel uploadPanel = new UploadPanel();
				uploadPanel.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						popup.clear();
						popup.hide();
					}
				});
				uploadPanel.addOnFinishUploaderHandler(onFinishUploaderHandler);
				uploadPanel.setButtonText(constants.cancel());
				popup.add(uploadPanel);

				popup.center();
			}
		});

		view.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				if(view.isPictureVisible())
					view.showAdd();
			}
		});

		view.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if(view.isPictureVisible())
					view.hideAdd();
			}
		});

		view.addCancelClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EventBus.get().fireEvent(new ClosePopupEvent());
			}
		});		
	}

	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {
				url = uploader.getServletPath() + "?blob-key=" + uploader.getServerMessage().getMessage();

				popup.clear();
				popup.hide();
				view.setPictureUrl(url);
				view.showPicture();
				view.hideAdd();
			}
		}
	};

	protected List<AttributeInfo> getCustomAttributes(){
		return customAttributes;
	}

	protected void clearCustomAtttributes(){
		customAttributes.clear();
	}
	
	protected void addCustomAttribute(AttributeInfo attributeInfo){
		customAttributes.add(attributeInfo);
	}
	
	protected void addCustomAttributesToView(){
		for(AttributeInfo attributeInfo : customAttributes){
			if(attributeInfo.isVisable(CreateUpdatePresenter.class.getName())){
				Widget arrayInputBox = null;
				switch(attributeInfo.getDataType()){
				case BOOLEAN:
					CheckBox checkBox = new CheckBox();
					view.addSecondaryAttribute(attributeInfo.getLabel(locale), attributeInfo.isRequired(), checkBox);
					customWidgets.put(attributeInfo.getAttributeName(), checkBox);
					break;
				case DATE:
					DateBox dateBox = new DateBox();
					view.addSecondaryAttribute(attributeInfo.getLabel(locale), attributeInfo.isRequired(), dateBox);
					customWidgets.put(attributeInfo.getAttributeName(), dateBox);
					break;
				case FLOAT:
				case STRING:
				case INTEGER:
					TextBox textBoxFloat = new TextBox();
					view.addSecondaryAttribute(attributeInfo.getLabel(locale), attributeInfo.isRequired(), textBoxFloat);
					customWidgets.put(attributeInfo.getAttributeName(), textBoxFloat);
					break;
				case BOOLEAN_ARRAY:
					arrayInputBox = new ArrayInput<Boolean>(new WidgetFactory<Boolean>() {
						@Override
						public Widget newInstace() {
							return new CheckBox();
						}

						@Override
						public Widget newInstace(Boolean value) {
							CheckBox checkBox = new CheckBox();
							checkBox.setValue(value);
							return checkBox;
						}

						@Override
						public Boolean getValueFrom(Widget w) {
							return ((CheckBox) w).getValue();
						}
					});
					view.addSecondaryAttribute(attributeInfo.getLabel(locale), attributeInfo.isRequired(), arrayInputBox);
					customWidgets.put(attributeInfo.getAttributeName(), arrayInputBox);
					break;
				case DATE_ARRAY:
					arrayInputBox = new ArrayInput<Date>(new WidgetFactory<Date>() {
						
						@Override
						public Widget newInstace() {
							return new DateBox();
						}

						@Override
						public Widget newInstace(Date value) {
							DateBox dateBox = new DateBox();
							dateBox.setValue(value);
							return dateBox;
						}

						@Override
						public Date getValueFrom(Widget w) {
							return ((DateBox) w).getValue();
						}
					});
					view.addSecondaryAttribute(attributeInfo.getLabel(locale), attributeInfo.isRequired(), arrayInputBox);
					customWidgets.put(attributeInfo.getAttributeName(), arrayInputBox);	
					break;
				case FLOAT_ARRAY:
					arrayInputBox = new ArrayInput<Float>(new WidgetFactory<Float>() {
						@Override
						public Widget newInstace() {
							return new TextBox();
						}

						@Override
						public Widget newInstace(Float value) {
							TextBox textBox = new TextBox();
							textBox.setText(String.valueOf(value));
							return textBox;
						}

						@Override
						public Float getValueFrom(Widget w) {
							String value = ((TextBox) w).getValue();
							if(!value.equals(""))
								return Float.valueOf(value);
							else
								return null;
						}
					});
					view.addSecondaryAttribute(attributeInfo.getLabel(locale), attributeInfo.isRequired(), arrayInputBox);
					customWidgets.put(attributeInfo.getAttributeName(), arrayInputBox);
					break;
				case INTERGER_ARRAY:
					arrayInputBox = new ArrayInput<Integer>(new WidgetFactory<Integer>() {
						@Override
						public Widget newInstace() {
							return new TextBox();
						}

						@Override
						public Widget newInstace(Integer value) {
							TextBox textBox = new TextBox();
							textBox.setText(String.valueOf(value));
							return textBox;
						}

						@Override
						public Integer getValueFrom(Widget w) {
							String value = ((TextBox) w).getValue();
							if(!value.equals(""))
								return Integer.valueOf(value);
							else
								return null;
						}
					});
					view.addSecondaryAttribute(attributeInfo.getLabel(locale), attributeInfo.isRequired(), arrayInputBox);
					customWidgets.put(attributeInfo.getAttributeName(), arrayInputBox);
					break;
				case STRING_ARRAY:
					arrayInputBox = new ArrayInput<String>(new WidgetFactory<String>() {
						@Override
						public Widget newInstace() {
							return new TextBox();
						}

						@Override
						public Widget newInstace(String value) {
							TextBox textBox = new TextBox();
							textBox.setText(value);
							return textBox;
						}

						@Override
						public String getValueFrom(Widget w) {
							return ((TextBox) w).getValue();
						}
					});
					view.addSecondaryAttribute(attributeInfo.getLabel(locale), attributeInfo.isRequired(), arrayInputBox);
					customWidgets.put(attributeInfo.getAttributeName(), arrayInputBox);
					break;
				default:
					break;
				}
			}
		}
	}
	
	
	protected void addValuesToCustomWidgets(SystemObject sysObject){
		for(AttributeInfo attributeInfo : getCustomAttributes()){
			if(attributeInfo.isVisable(CreateUpdatePresenter.class.getName())){
				String attributeName = attributeInfo.getAttributeName();
				IAttribute attribute = null;
				switch (attributeInfo.getDataType()) {
				case BOOLEAN:
					CheckBox checkBox = (CheckBox) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						BooleanAttribute booleanAttribute = (BooleanAttribute) attribute;
						checkBox.setValue(booleanAttribute.getValue());
					}
					checkBox.setEnabled(attributeInfo.isEditable());
					break;
				case DATE:
					DateBox dateBox = (DateBox) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						DateAttribute dateAttribute = (DateAttribute) attribute;
						dateBox.setValue(dateAttribute.getValue());
					}
					dateBox.setEnabled(attributeInfo.isEditable());
					break;
				case STRING:
					TextBox textBox = (TextBox) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						StringAttribute stringAttribute = (StringAttribute) attribute;
						textBox.setValue(stringAttribute.getValue());
					}
					textBox.setReadOnly(attributeInfo.isEditable());
					break;
				case FLOAT:
					TextBox floatTextBox = (TextBox) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						FloatAttribute floatAttribute = (FloatAttribute) attribute;
						floatTextBox.setValue(String.valueOf(floatAttribute.getValue()));
					}
					floatTextBox.setReadOnly(attributeInfo.isEditable());
					break;
				case INTEGER:
					TextBox integerTextBox = (TextBox) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						IntegerAttribute integerAttribute = (IntegerAttribute) attribute;
						integerTextBox.setValue(String.valueOf(integerAttribute.getValue()));
					}
					integerTextBox.setReadOnly(attributeInfo.isEditable());
					break;
				case BOOLEAN_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<Boolean> booleanArrayInput = (ArrayInput<Boolean>) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						booleanArrayInput.clear();
						BooleanArrayAttribute booleanArrayAttribute = (BooleanArrayAttribute) attribute;
						booleanArrayInput.setValues(booleanArrayAttribute.getValue());
					}
					booleanArrayInput.setEnabled(attributeInfo.isEditable());
					break;
				case DATE_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<Date> dateArrayInput = (ArrayInput<Date>) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						dateArrayInput.clear();
						DateArrayAttribute dateArrayAttribute = (DateArrayAttribute) attribute;
						dateArrayInput.setValues(dateArrayAttribute.getValue());
					}
					dateArrayInput.setEnabled(attributeInfo.isEditable());
					break;
				case FLOAT_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<Float> floatArrayInput = (ArrayInput<Float>) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						floatArrayInput.clear();
						FloatArrayAttribute floatArrayAttribute = (FloatArrayAttribute) attribute;
						floatArrayInput.setValues(floatArrayAttribute.getValue());
					}
					floatArrayInput.setEnabled(attributeInfo.isEditable());
					break;
				case INTERGER_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<Integer> integerArrayInput = (ArrayInput<Integer>) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						integerArrayInput.clear();
						IntegerArrayAttribute integerArrayAttribute = (IntegerArrayAttribute) attribute;
						integerArrayInput.setValues(integerArrayAttribute.getValue());
					}
					integerArrayInput.setEnabled(attributeInfo.isEditable());
					break;
				case STRING_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<String> stringArrayInput = (ArrayInput<String>) getWidget(attributeName);
					attribute = sysObject.getAttribute(attributeName);
					if(attribute != null){
						stringArrayInput.clear();
						StringArrayAttribute stringArrayAttribute = (StringArrayAttribute) attribute;
						stringArrayInput.setValues(stringArrayAttribute.getValue());
					}
					stringArrayInput.setEnabled(attributeInfo.isEditable());
					break;
				default:
					break;
				}
			}
		}
	}
	
	protected void readValuesAndSaveOnObject(SystemObject sysObject){
		for(AttributeInfo attributeInfo : getCustomAttributes()){
			if(attributeInfo.isVisable(CreateUpdatePresenter.class.getName())){
				String attributeName = attributeInfo.getAttributeName();
				switch (attributeInfo.getDataType()) {
				case BOOLEAN:
					CheckBox checkBox = (CheckBox) getWidget(attributeName);
					try {
						sysObject.addAttribute(new BooleanAttribute(checkBox.getValue(), attributeName));
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				case DATE:
					DateBox dateBox = (DateBox) getWidget(attributeName);
					try {
						if(dateBox.getValue() != null)
							sysObject.addAttribute(new DateAttribute(dateBox.getValue(), attributeName));
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				case STRING:
					TextBox textBox = (TextBox) getWidget(attributeName);
					try {
						if(!textBox.getValue().equals(""))
							sysObject.addAttribute(new StringAttribute(textBox.getValue(), attributeName));
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				case FLOAT:
					TextBox floatTextBox = (TextBox) getWidget(attributeName);
					try {
						if(!floatTextBox.getValue().equals(""))
							sysObject.addAttribute(new FloatAttribute(Float.valueOf(floatTextBox.getValue()), attributeName));
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				case INTEGER:
					TextBox integerTextBox = (TextBox) getWidget(attributeName);
					try {
						if(!integerTextBox.getValue().equals(""))
							sysObject.addAttribute(new IntegerAttribute(Integer.valueOf(integerTextBox.getValue()), attributeName));
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				case BOOLEAN_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<Boolean> booleanArrayInput = (ArrayInput<Boolean>) getWidget(attributeName);
					try {
						if(!booleanArrayInput.getValues().isEmpty())
							sysObject.addAttribute(new BooleanArrayAttribute(booleanArrayInput.getValues(), attributeName));
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				case DATE_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<Date> dateArrayInput = (ArrayInput<Date>) getWidget(attributeName);
					try {
						if(!dateArrayInput.getValues().isEmpty())
							sysObject.addAttribute(new DateArrayAttribute(dateArrayInput.getValues(), attributeName));
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				case FLOAT_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<Float> floatArrayInput = (ArrayInput<Float>) getWidget(attributeName);
					try {
						if(!floatArrayInput.getValues().isEmpty())
							sysObject.addAttribute(new FloatArrayAttribute(floatArrayInput.getValues(), attributeName));
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				case INTERGER_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<Integer> integerArrayInput = (ArrayInput<Integer>) getWidget(attributeName);
					try {
						if(!integerArrayInput.getValues().isEmpty()){
							sysObject.addAttribute(new IntegerArrayAttribute(integerArrayInput.getValues(), attributeName));
						}
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				case STRING_ARRAY:
					@SuppressWarnings("unchecked")
					ArrayInput<String> stringArrayInput = (ArrayInput<String>) getWidget(attributeName);
					try {
						if(!stringArrayInput.getValues().isEmpty())
							sysObject.addAttribute(new StringArrayAttribute(stringArrayInput.getValues(), attributeName));
					} catch (AttributeCastException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		}
	}
	
	protected Widget getWidget(String attributeName){
		return customWidgets.get(attributeName);
	}

	protected IConnectionServiceAsync getRpcService(){
		return rpcService;
	}

	protected Display getView(){
		return view;
	}

	protected CommonWorkspaceConstants getConstants(){
		return constants;
	}

	protected String getPictureUrl(){
		return url;
	}

	protected int getItemIndex(ListBox listBox, String item){;
		int indexToFind = -1;
		for (int i=0; i<listBox.getItemCount(); i++) {
			if (listBox.getValue(i).equals(item)) {
				indexToFind = i;
				break;
			}
		}
		return indexToFind;
	}

	protected void setPictureUrl(String url){
		this.url = url;
	}
}
