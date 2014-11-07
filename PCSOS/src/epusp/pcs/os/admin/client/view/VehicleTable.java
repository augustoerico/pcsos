package epusp.pcs.os.admin.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class VehicleTable extends Composite {

	private static VehicleTableUiBinder uiBinder = GWT
			.create(VehicleTableUiBinder.class);

	interface VehicleTableUiBinder extends UiBinder<Widget, VehicleTable> {
	}

	public VehicleTable() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
