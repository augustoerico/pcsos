package epusp.pcs.os.shared.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.event.ClosePopupEvent;
import epusp.pcs.os.shared.client.event.EventBus;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.client.view.UploadPanel;
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
	}

	private final IConnectionServiceAsync rpcService;
	private final Display view;
	private final Boolean create;
	private final CommonWorkspaceConstants constants;

	private final DecoratedPopupPanel popup = new DecoratedPopupPanel(false, false);
	
	private String url;

	public CreateUpdatePresenter(IConnectionServiceAsync rpcService, Display view, CommonWorkspaceConstants constants, Boolean create){
		this.rpcService = rpcService;
		this.view = view;
		this.constants = constants;
		this.create = create;
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

	public Boolean isCreate(){
		return create;
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
}
