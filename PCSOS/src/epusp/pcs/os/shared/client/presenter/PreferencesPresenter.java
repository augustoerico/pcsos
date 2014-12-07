package epusp.pcs.os.shared.client.presenter;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import epusp.pcs.os.shared.client.constants.CommonWorkspaceConstants;
import epusp.pcs.os.shared.client.rpc.IConnectionServiceAsync;
import epusp.pcs.os.shared.model.person.user.AvailableLanguages;

public class PreferencesPresenter implements Presenter{

	private final IConnectionServiceAsync rpcService;
	private final Display view;
	private final CommonWorkspaceConstants constants;

	public interface Display{
		void setLanguageLabel(String text);
		Widget asWidget();
		Button getCancelButton();
		Button getOkButton();
		ListBox getLanguages();
	}

	public PreferencesPresenter(IConnectionServiceAsync rpcService, Display view, CommonWorkspaceConstants constants){
		this.rpcService = rpcService;
		this.view = view;
		this.constants = constants;
	}

	private AvailableLanguages userLanguage;

	@Override
	public void go(HasWidgets container) {
		container.clear();

		for(AvailableLanguages language : AvailableLanguages.values()){
			switch (language) {
			case ENGLISH:
				view.getLanguages().addItem("English", language.name());
				break;
			case PORTUGUES:
				view.getLanguages().addItem("Português", language.name());
				break;
			default:
				break;
			}
		}

		rpcService.getUserLanguage(new AsyncCallback<AvailableLanguages>() {

			@Override
			public void onSuccess(AvailableLanguages result) {
				userLanguage = result;
				view.getLanguages().setItemSelected(getItemIndex(result.name()), true);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});

		view.getCancelButton().setText(constants.cancel());
		view.getOkButton().setText(constants.save());
		view.setLanguageLabel(constants.preferredLanguage());
		view.getOkButton().setEnabled(false);
		
		container.add(view.asWidget());
		bind();
	}

	private void bind(){
		view.getLanguages().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				AvailableLanguages language = AvailableLanguages.valueOf(view.getLanguages().getValue(view.getLanguages().getSelectedIndex()));
				if(!language.equals(userLanguage))
					view.getOkButton().setEnabled(true);
				else
					view.getOkButton().setEnabled(false);
			}
		});
		
		view.getOkButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final AvailableLanguages language = AvailableLanguages.valueOf(view.getLanguages().getValue(view.getLanguages().getSelectedIndex()));
				if(!language.equals(userLanguage)){
					userLanguage = language;
					rpcService.setPreferredLanguage(language, new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							String query = Window.Location.getQueryString();
							query = query.replaceAll("&locale=[^&]+", "");
							String path = Window.Location.getProtocol().concat("//").concat(Window.Location.getHost())
									.concat(Window.Location.getPath()).concat(query);
							String locale = "&locale=";
							switch (language) {
							case ENGLISH:
								locale = locale.concat("en");
								break;
							case PORTUGUES:
								locale = locale.concat("pt");
								break;
							default:
								locale = locale.concat("en");
								break;
							}
							path = path.concat(locale);

							Window.Location.replace(path);
						}						

						@Override
						public void onFailure(Throwable caught) {
						}
					});
				}
			}
		});
	}

	public Button getCancelButton(){
		return view.getCancelButton();
	}
	
	public Button getOkButton(){
		return view.getOkButton();
	}

	private int getItemIndex(String text){
		for(int i = 0; i < view.getLanguages().getItemCount(); i++){
			if(view.getLanguages().getValue(i).equals(text))
				return i;
		}

		return 0;
	}
}
