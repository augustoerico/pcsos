package epusp.pcs.os.server;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import epusp.pcs.os.server.login.AuthenticationManager;
import epusp.pcs.os.server.workflow.EmergencyCallWorkflow;
import epusp.pcs.os.shared.client.rpc.IConnectionService;
import epusp.pcs.os.shared.exception.CannotLogoutExeception;
import epusp.pcs.os.shared.exception.LoginException;
import epusp.pcs.os.shared.general.MoveCursor;
import epusp.pcs.os.shared.model.ICustomAttributes;
import epusp.pcs.os.shared.model.attribute.AttributeInfo;
import epusp.pcs.os.shared.model.attribute.Category;
import epusp.pcs.os.shared.model.attribute.DataType;
import epusp.pcs.os.shared.model.attribute.types.IntegerAttribute;
import epusp.pcs.os.shared.model.attribute.types.StringAttribute;
import epusp.pcs.os.shared.model.exception.AttributeCastException;
import epusp.pcs.os.shared.model.licence.DrivingCategories;
import epusp.pcs.os.shared.model.licence.DrivingLicense;
import epusp.pcs.os.shared.model.oncall.Position;
import epusp.pcs.os.shared.model.person.user.AvailableLanguages;
import epusp.pcs.os.shared.model.person.user.User;
import epusp.pcs.os.shared.model.person.user.admin.Admin;
import epusp.pcs.os.shared.model.person.user.agent.Agent;
import epusp.pcs.os.shared.model.person.user.monitor.Monitor;
import epusp.pcs.os.shared.model.person.user.superuser.SuperUser;
import epusp.pcs.os.shared.model.person.victim.Victim;
import epusp.pcs.os.shared.model.vehicle.Priority;
import epusp.pcs.os.shared.model.vehicle.Vehicle;
import epusp.pcs.os.shared.model.vehicle.car.Car;
import epusp.pcs.os.shared.model.vehicle.helicopter.Helicopter;

public class Connection extends RemoteServiceServlet implements IConnectionService{

	private static final long serialVersionUID = 1L;

	protected EmergencyCallWorkflow workflow = EmergencyCallWorkflow.getInstance();

	protected AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

	protected String userSessionAttribute = "userSessionAttribute";

	private static final HashMap<String, AttributeInfo> attributesInfo = new HashMap<String, AttributeInfo>();

	public void init() throws ServletException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		System.out.println("Initializing with test data");
		Monitor monitor = new Monitor("Giovanni", "Gatti Pinheiro", "giovanni.gatti.pinheiro@gmail.com");
		monitor.setIsActive(true);
		monitor.setGoogleUserId("115057125698280242918");
		monitor.setPictureURL("https://lh5.googleusercontent.com/--PBV1HBWVsc/AAAAAAAAAAI/AAAAAAAAAFQ/O57isBLRtRA/photo.jpg");
		monitor.setPreferedLanguage(AvailableLanguages.ENGLISH);

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(monitor);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		Admin admin = new Admin("Giovanni", "Gatti Pinheiro", "giovanni.pinheiro@vilt-group.com");
		admin.setIsActive(true);
		admin.setGoogleUserId("112302574104571853734");
		admin.setPictureURL("https://lh6.googleusercontent.com/-XqK5qQExPEI/U2tpiPFfp9I/AAAAAAAAAB4/sKELQ1P8JMk/w901-h903-no/2014-05-06%2B13.17.58.jpg");
		admin.setPreferedLanguage(AvailableLanguages.ENGLISH);

		pm = PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(admin);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		//		System.out.println("Create Monitor...");
		//		Monitor monitor = new Monitor("Monitor", "da Silva", "monitor.silva@gmail.com");
		//		monitor.setIsActive(true);
		//		monitor.setGoogleUserId("0000001");
		//		
		pm = PMF.get().getPersistenceManager();
		System.out.println("Create Cars...");
		Car car = new Car("TAG001", "PCS-0505");
		car.setPrioraty(Priority.PRIMARY);
		car.setImageURL("http://www.landmarkhire.com/images/starsky-and-hutch-car.jpg");
		//
		//		Car supportCar = new Car("PCS-0506");
		//		supportCar.setPrioraty(Priority.SUPPORT);
		//		
		System.out.println("Create Agents...");
		Agent agent1 = new Agent("Maria", "Emilia Midori", "Hirami", "stmidori@gmail.com");
		agent1.setIsActive(true);
		agent1.setGoogleUserId("0000002");
		agent1.setPictureURL("https://lh5.googleusercontent.com/Z59ZqJ392vBnwHtcgrm-h_c8ZPZGl-CXrmzgRmoW-_E=s207-p-no");

		DrivingLicense drivingLicence =  new DrivingLicense(agent1, "123123321", new Date());
		drivingLicence.setCategory(DrivingCategories.B);
		drivingLicence.setHasAcategory(true);
		agent1.addLicense(drivingLicence);

		//		
		//		Agent agent2 = new Agent("Ken", "Hutchinson", "ken.hutchinson@gmail.com");
		//		agent2.setIsActive(true);
		//		agent2.setGoogleUserId("0000003");
		//		
		//		Agent agent3 = new Agent("Alex", "J. Murphy", "ken.hutchinson@gmail.com");
		//		agent3.setIsActive(true);
		//		agent3.setGoogleUserId("0000004");
		//		
		//		System.out.println("Create Victim");
		Victim victim = new Victim("Erico", "da Silva", "augusto.ericosilva@gmail.com");
		victim.setIsActive(true);
		victim.setPictureURL("https://lh5.googleusercontent.com/-bz0jlqC47wI/VCxuAL0oCdI/AAAAAAAAGo0/wtMnqqoCOFc/s903-no/IMG_20130504_151332.jpg");
		victim.setGoogleUserId("0000004");
		//		
		//		Victim victim2 = new Victim("Luizinho", "da Silva", "luizinho.silva@gmail.com");
		//		victim2.setIsActive(true);
		//		victim2.setGoogleUserId("0000005");
		//		
		//		Car carX = new Car("PCS-0507");
		//		carX.setPrioraty(Priority.PRIMARY);
		//		
		//		Agent agentX = new Agent("Mr. Satan", "Jr.", "satan.junior@gmail.com");
		//		agentX.setIsActive(true);
		//		agentX.setGoogleUserId("0000006");
		//		
		//		System.out.println("Persisting and detaching objects");
		Agent detachedAgent1 = null, detachedAgent2 = null, detachedAgent3 = null;
		Car detachedCar = null, detachedSupportCar = null;
		//		Monitor detachedMonitor = null;
		Victim detachedVictim = null, detachedVictim2 = null;


		AttributeInfo info = new AttributeInfo("cor_dos_olhos", Category.PhysicalProfile, DataType.STRING);
		info.addLocale("pt", "Cor dos Olhos");
		info.addLocale("en", "Eye Color");


		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(info);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		info = new AttributeInfo("idade", Category.PhysicalProfile, DataType.INTEGER);
		info.addLocale("pt", "Idade");
		info.addLocale("en", "Age");

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(info);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}
		
		info = new AttributeInfo("test", Category.PhysicalProfile, DataType.DATE_ARRAY);
		info.addLocale("pt", "Teste");
		info.addLocale("en", "Test");

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(info);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}
		
		info = new AttributeInfo("test2", Category.PhysicalProfile, DataType.INTERGER_ARRAY);
		info.addLocale("pt", "Teste2");
		info.addLocale("en", "Test2");

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(info);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		StringAttribute corDosOlhos = new StringAttribute("marrom", "cor_dos_olhos"); 
		IntegerAttribute idade = new IntegerAttribute(23, "idade");

		try {
			victim.addAttribute(corDosOlhos);
			victim.addAttribute(idade);
		} catch (AttributeCastException e) {
			e.printStackTrace();
		}

		info = new AttributeInfo("cor", Category.PersonalVehicle, DataType.STRING);
		info.addLocale("pt", "Cor");
		info.addLocale("en", "Color");

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(info);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		StringAttribute cor = new StringAttribute("vermelho", "cor");

		try {
			car.addAttribute(cor);
		} catch (AttributeCastException e) {
			e.printStackTrace();
		}

		pm =  PMF.get().getPersistenceManager();
		//		
		try{
			pm.currentTransaction().begin();
			//			
			////			pm.makePersistent(admin);
			//			pm.makePersistent(monitor);
			//			detachedMonitor = pm.detachCopy(monitor);
			pm.makePersistent(car);
			detachedCar = pm.detachCopy(car);
			pm.makePersistent(agent1);
			detachedAgent1 = pm.detachCopy(agent1);
			//			pm.makePersistent(agent2);
			//			detachedAgent2 = pm.detachCopy(agent2);
			//			pm.makePersistent(agent3);
			//			detachedAgent3 = pm.detachCopy(agent3);
			pm.makePersistent(victim);
			detachedVictim = pm.detachCopy(victim);
			//			
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		Helicopter h = new Helicopter("TAG00X");
		h.setPrioraty(Priority.SUPPORT);
		h.setImageURL("http://3.bp.blogspot.com/-nPMJ8lbBR-Q/UVc42PBUfbI/AAAAAAAAAIc/r4pyz4NTEPI/s320/Helicoptero+Aguia+11+PMESP1.jpg");
		Helicopter detachedH = null;

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(h);
			detachedH = pm.detachCopy(h);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		Agent agent = new Agent("David", "Starsky", "david.starsk@gmail.com");
		agent.setIsActive(true);
		agent.setPictureURL("http://cdn.buzznet.com/assets/users16/pattygopez/default/david-starsky-starsky-hutch--large-msg-132259952209.jpg");

		Agent detachedAgent = null;

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(agent);
			detachedAgent = pm.detachCopy(agent);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		//-----------------------------------------------------------------------------------//

		Car cX = new Car("TAG003", "XPT-0002");
		cX.setPrioraty(Priority.SUPPORT);
		cX.setImageURL("http://ecx.images-amazon.com/images/I/81cOTyQZbEL._SL1500_.jpg");
		Car detachedCX = null;

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(cX);
			detachedCX = pm.detachCopy(cX);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		Agent chuck = new Agent("Chuck", "Norris", "chuck.norris@gmail.com");
		chuck.setIsActive(true);
		chuck.setPictureURL("http://img2.wikia.nocookie.net/__cb20081118030612/tesfanon/images/5/5c/Chuck_Norris.jpg");
		try {
			chuck.addAttribute(new StringAttribute("preto", "cor_dos_olhos"));
		} catch (AttributeCastException e1) {
			e1.printStackTrace();
		}
		try {
			chuck.addAttribute(new IntegerAttribute(74, "idade"));
		} catch (AttributeCastException e1) {
			e1.printStackTrace();
		}
		Agent detachedChuck = null; 

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(chuck);
			detachedChuck = pm.detachCopy(chuck);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}
		//-------------------------------------------------------------//
		Car c1 = new Car("TAG004", "XPT-0003");
		c1.setPrioraty(Priority.SUPPORT);
		c1.setImageURL("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQOuABFcPm1W7stgmlpAwb9EqUHqIImJJvhSm6SKikl6WJdO1dr");
		Car detachedC1 = null;

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(c1);
			detachedC1 = pm.detachCopy(c1);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		Agent robocop = new Agent("Alex", "Murphy", "alex.murphy@gmail.com");
		robocop.setIsActive(true);
		robocop.setPictureURL("http://1.bp.blogspot.com/-33CQjwC7Wws/UKUA_ZXEDrI/AAAAAAAAT28/k7DS77DD0vI/s1600/robocop_peter-weller.jpg");
		Agent detachedRobocop = null; 

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(robocop);
			detachedRobocop = pm.detachCopy(robocop);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}
		//-------------------------------------------------------------//
		Helicopter c2 = new Helicopter("TAG005");
		c2.setPrioraty(Priority.SUPPORT);
		c2.setImageURL("http://upload.wikimedia.org/wikipedia/commons/0/01/Blackhawk.jpg");
		Helicopter detachedC2 = null;

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(c2);
			detachedC2 = pm.detachCopy(c2);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		Agent arrow = new Agent("Oliver", "Queen", "oliver.queen@gmail.com");
		arrow.setIsActive(true);
		arrow.setPictureURL("http://media.dcentertainment.com/sites/default/files/TV_Gallery_Arrow01_53712d787bb741.46013658.jpg");
		Agent detachedArrow = null; 

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(arrow);
			detachedArrow = pm.detachCopy(arrow);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		//-----------------------------

		Victim v1 = new Victim("Eike", "Batista", "eike.batista@gmail.com");
		v1.setIsActive(true);
		v1.setPictureURL("http://cdn1.valuewalk.com/wp-content/uploads/2013/09/Eike-Batista.jpg");
		v1.setGoogleUserId("0000005");

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(v1);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}

		Victim v2 = new Victim("Gato", "de Botas", "el.gato@gmail.com");
		v2.setIsActive(true);
		v2.setPictureURL("http://4.bp.blogspot.com/-8MbMe--7J4o/Uc05PZLUZ0I/AAAAAAAACv0/FB_K0kbo2OQ/s1600/Gato-com-cara-de-coitado.jpg");
		v2.setGoogleUserId("0000005");

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(v2);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}


		//-----------------------------
		SuperUser superUser = new SuperUser("SuperUser", "PCS.O.S", "superuser.pcsos@gmail.com");
		superUser.setGoogleUserId("114093975308550875649");
		superUser.setPreferedLanguage(AvailableLanguages.ENGLISH);
		superUser.setIsActive(true);

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(superUser);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}



		//-----------------------------
		Admin adm = new Admin("Admin", "PCS.O.S", "kimjongun.pcsos@gmail.com");
		adm.setGoogleUserId("110537040925689244070");
		adm.setPreferedLanguage(AvailableLanguages.ENGLISH);
		adm.setIsActive(true);

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(adm);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}



		//-----------------------------
		Monitor mon = new Monitor("Monitor", "PCS.O.S", "monitor.pcsos@gmail.com");
		mon.setGoogleUserId("101441169719979331358");
		mon.setPreferedLanguage(AvailableLanguages.ENGLISH);
		mon.setIsActive(true);

		pm =  PMF.get().getPersistenceManager();

		try{
			pm.currentTransaction().begin();
			pm.makePersistent(mon);
			pm.currentTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			if(pm.currentTransaction().isActive())
				pm.currentTransaction().rollback();
		}finally{
			pm.close();
		}


		/*************************************************************************************************************************/
		Position ATLANTA = new Position(33.7814790, -84.3880580);
		Position STONE_MOUNTAIN_PARK = new Position(33.80653802509606, -84.15252685546875);
		Position CYCLORAMA = new Position(33.741185330333956, -84.35834884643555);
		Position GEORGIA_AQUARIUM = new Position(33.761443931868925, -84.39432263374329);
		Position UNDERGROUND_ATLANTA = new Position(33.75134645137294, -84.39026713371277);
		Position PIEDMONT_HOSPITAL = new Position(33.8086907, -84.4690846);

		List<Agent> l = new ArrayList<Agent>();
		l.clear();
		l.add(detachedAgent1);
		workflow.addFreeVehicle(detachedCar.getIdTag(), l,CYCLORAMA);
		workflow.addWaitingCall(detachedVictim.getEmail(), GEORGIA_AQUARIUM);
		
		l.clear();
		l.add(detachedAgent);
		workflow.addFreeVehicle(detachedH.getIdTag(), l, ATLANTA);

		l.clear();
		l.add(detachedChuck);
		workflow.addFreeVehicle(detachedCX.getIdTag(), l,UNDERGROUND_ATLANTA);

		l.clear();
		l.add(detachedArrow);
		workflow.addFreeVehicle(detachedC2.getIdTag(), l, STONE_MOUNTAIN_PARK);

		l.clear();
		l.add(detachedRobocop);
		workflow.addFreeVehicle(detachedC1.getIdTag(), l, PIEDMONT_HOSPITAL);
		/*************************************************************************************************************************/


		//		
		//		pm = PMF.get().getPersistenceManager();
		//		
		//		try{
		//			pm.currentTransaction().begin();
		//			pm.makePersistent(supportCar);
		//			detachedSupportCar = pm.detachCopy(supportCar);
		//			pm.makePersistent(victim2);
		//			detachedVictim2 = pm.detachCopy(victim2);
		//			pm.makePersistent(carX);
		//			pm.makePersistent(agentX);
		//			pm.currentTransaction().commit();
		//		}catch (Exception e){
		//			e.printStackTrace();
		//			if(pm.currentTransaction().isActive())
		//				pm.currentTransaction().rollback();
		//		}finally{
		//			pm.close();
		//		}
		//		
		//		System.out.println("Victim starts call");
		//		workflow.addWaitingCall(detachedVictim.getEmail());
		//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
		//		
		//		workflow.addVictimPosition(detachedVictim.getEmail(), new Position(3082.109,-313.1312));
		//		
		//		System.out.println("Adding free suport vehicle to workflow");
		//		List<Agent> l = new ArrayList<Agent>();
		//		l.add(detachedAgent3);
		//		workflow.addFreeVehicle(detachedSupportCar.getId(), l);
		//		System.out.println("Support car state: " + workflow.isVehicleOnCall(detachedSupportCar.getId()));
		//		
		//		workflow.addVehiclePosition(detachedSupportCar.getId(), new Position(22222.22222, 33333.33333));
		//		
		//		System.out.println("Adding monitor to workflow");
		//		workflow.addFreeMonitor(detachedMonitor.getId());
		//		System.out.println("Support monitor state: " + workflow.isMonitorOnCall(detachedMonitor.getId()));
		//		
		//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
		//		
		//		System.out.println("Adding vehicle to workflow");
		//		
		//		l.clear();
		//		l.add(detachedAgent1);
		//		l.add(detachedAgent2);
		//		workflow.addFreeVehicle(detachedCar.getId(), l);
		//		System.out.println("Support monitor state: " + workflow.isVehicleOnCall(detachedCar.getId()));
		//		
		//		System.out.println("Adding vehicle position");
		//		workflow.addVehiclePosition(detachedCar.getId(), new Position(12312.1231, 213213.331231));
		//		
		//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
		//		
		//		System.out.println("--------------------------Association Phase Completed--------------------------");
		//		
		//		workflow.addFreeMonitor(detachedMonitor.getId());
		//		System.out.println("Support monitor state: " + workflow.isMonitorOnCall(detachedMonitor.getId()));
		//		
		//	
		//		System.out.println("Support car state: " + workflow.isVehicleOnCall(detachedSupportCar.getId()));
		//		
		//		workflow.monitorOnCallAcknowledgment(detachedMonitor.getId());
		//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
		//		
		//		workflow.vehicleOnCallAcknowledgment(detachedCar.getId());
		//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
		//		
		//		System.out.println("--------------------------Acknoledment Phase Completed--------------------------");
		//		EmergencyCall monitorEmergencyCall = workflow.getMonitorEmergencyCall(detachedMonitor.getId());
		//		EmergencyCall vehicleEmergencyCall = workflow.getVehicleEmergencyCall(detachedCar.getId());
		//		
		//		Victim v = workflow.getVictim(monitorEmergencyCall.getVictimEmail());
		//		System.out.println("Monitor view:");
		//		System.out.println("Victim name: " + v.getName() + " " + v.getSurname() +", id:" +  v.getId());
		//		System.out.println("Victim Position: " + monitorEmergencyCall.getLastVictimPosition().toString());
		//		for(VehicleOnCall vehicle : monitorEmergencyCall.getVehicles()){
		//			Vehicle vhc = workflow.getVehicle(vehicle.getVehicleId());
		//			Position vehiclePosition = vehicleEmergencyCall.getLastVehiclePosition(vehicle.getVehicleId());
		//			if(!vehiclePosition.isEmpty())
		//				System.out.println("Vehicle id: " + vehicle.getVehicleId() + ", position: " + vehiclePosition.toString());
		//			else
		//				System.out.println("Vehicle id: " + vehicle.getVehicleId());
		//			for(Agent agent : vhc.getAgents()){
		//				System.out.println("   - Agent: " + agent.getName() +  " " + agent.getSurname());
		//			}
		//		}
		//		
		//		System.out.println("Vehicle View:");
		//		Position victimPosition = vehicleEmergencyCall.getLastVictimPosition();
		//		v = workflow.getVictim(vehicleEmergencyCall.getVictimEmail());
		//		Monitor m = workflow.getMonitor(monitor.getId());
		//		if(!victimPosition.isEmpty())
		//			System.out.println("Victim name: " + v.getName() + " " + v.getSurname() + ", position: " + victimPosition.toString());
		//		else
		//			System.out.println("Victim name: " + v.getName() + " " + v.getSurname());
		//		System.out.println("Monitor name: " + m.getName() + " " + m.getSurname()+
		//							", id: " + m.getId());
		//		
		//		
		//		System.out.println("Available support vehicles: " + workflow.getAvailableSupportVehicles());
		//		
		//		//Adding some more positions...
		//		workflow.addVictimPosition(detachedVictim.getEmail(), new Position(721.390128, -32871.3123));
		//		workflow.addVehiclePosition(detachedCar.getId(), new Position(38129.312231, 312321.312));
		//		
		//		monitorEmergencyCall = workflow.getMonitorEmergencyCall(detachedMonitor.getId());
		//		vehicleEmergencyCall = workflow.getVehicleEmergencyCall(detachedCar.getId());
		//
		//		System.out.println("Monitor view:");
		//		System.out.println("Victim Position: " + monitorEmergencyCall.getLastVictimPosition().toString());
		//		for(VehicleOnCall vehicle : monitorEmergencyCall.getVehicles()){
		//			Vehicle vhc = workflow.getVehicle(vehicle.getVehicleId());
		//			Position vehiclePosition = vehicleEmergencyCall.getLastVehiclePosition(vehicle.getVehicleId());
		//			if(!vehiclePosition.isEmpty())
		//				System.out.println("Vehicle id: " + vehicle.getVehicleId() + ", position: " + vehiclePosition.toString());
		//			else
		//				System.out.println("Vehicle id: " + vehicle.getVehicleId());
		//			for(Agent agent : vhc.getAgents()){
		//				System.out.println("   - Agent: " + agent.getName() +  " " + agent.getSurname());
		//			}
		//		}
		//		
		//		System.out.println("Vehicle View:");
		//		victimPosition = vehicleEmergencyCall.getLastVictimPosition();
		//		victimPosition = vehicleEmergencyCall.getLastVictimPosition();
		//		v = workflow.getVictim(vehicleEmergencyCall.getVictimEmail());
		//		m = workflow.getMonitor(monitor.getId());
		//		if(!victimPosition.isEmpty())
		//			System.out.println("Victim name: " + v.getName() + " " + v.getSurname() + ", position: " + victimPosition.toString());
		//		else
		//			System.out.println("Victim name: " + v.getName() + " " + v.getSurname());
		//		System.out.println("Monitor name: " + m.getName() + " " + m.getSurname()+
		//							", id: " + m.getId());
		//		
		//		//Adding new vehicle to Call
		//		v = workflow.getVictim(monitorEmergencyCall.getVictimEmail());
		//		workflow.addVehicleToCall(v.getEmail(), detachedSupportCar.getId());
		//		
		//		System.out.println("Available support vehicles: " + workflow.getAvailableSupportVehicles());
		//		
		//		workflow.addVictimPosition(detachedVictim.getEmail(), new Position(312231.123132, -321321.31312));
		//		workflow.addVehiclePosition(detachedCar.getId(), new Position(379132.31231, 312231.312321));
		//		workflow.addVehiclePosition(detachedSupportCar.getId(), new Position(31.21, 22.22));
		//		
		//		System.out.println("Monitor view:");
		//		System.out.println("Victim Position: " + monitorEmergencyCall.getLastVictimPosition().toString());
		//		for(VehicleOnCall vehicle : monitorEmergencyCall.getVehicles()){
		//			Vehicle vhc = workflow.getVehicle(vehicle.getVehicleId());
		//			Position vehiclePosition = vehicleEmergencyCall.getLastVehiclePosition(vehicle.getVehicleId());
		//			if(!vehiclePosition.isEmpty())
		//				System.out.println("Vehicle id: " + vehicle.getVehicleId() + ", position: " + vehiclePosition.toString());
		//			else
		//				System.out.println("Vehicle id: " + vehicle.getVehicleId());
		//			for(Agent agent : vhc.getAgents()){
		//				System.out.println("   - Agent: " + agent.getName() +  " " + agent.getSurname());
		//			}
		//		}
		//
		//		System.out.println("Vehicle View:");
		//		victimPosition = vehicleEmergencyCall.getLastVictimPosition();
		//		v = workflow.getVictim(vehicleEmergencyCall.getVictimEmail());
		//		m = workflow.getMonitor(vehicleEmergencyCall.getMonitor());
		//		System.out.println("Victim name: " + v.getName() + " " + v.getSurname() + ", position: " + victimPosition.toString());
		//		System.out.println("Monitor name: " + m.getName() + " " + m.getSurname());
		//		for(VehicleOnCall vehicle : vehicleEmergencyCall.getVehicles()){
		//			Vehicle vhc = workflow.getVehicle(vehicle.getVehicleId());
		//			Position vehiclePosition = vehicleEmergencyCall.getLastVehiclePosition(vehicle.getVehicleId());
		//			if(!vehiclePosition.isEmpty())
		//				System.out.println("Vehicle id: " + vehicle.getVehicleId() + ", position: " + vehiclePosition.toString());
		//			else
		//				System.out.println("Vehicle id: " + vehicle.getVehicleId());
		//			for(Agent agent : vhc.getAgents()){
		//				System.out.println("   - Agent: " + agent.getName() +  " " + agent.getSurname());
		//			}
		//		}
		//		
		//		System.out.println("--------------------------Call Phase Completed--------------------------");
		//		workflow.finishCall(detachedVictim.getEmail());
		//		
		//		System.out.println(workflow.getEmergencyCallLifecycle(detachedVictim.getEmail()));
		//		
		//		workflow.addWaitingCall(detachedVictim2.getEmail());
		//		
		//		List<Agent> agents = new ArrayList<Agent>();
		//		agents.add(agentX);
		//		workflow.addFreeVehicle(carX.getId(), agents);
		//		
		//		workflow.monitorFinishedCallAcknowledgment(detachedMonitor.getId());
		//		System.out.println(workflow.isMonitorOnCall(detachedMonitor.getId()));
		//		
		//		workflow.vehicleFinishedCallAcknowledgment(detachedSupportCar.getId());
		//		System.out.println(workflow.isVehicleOnCall(detachedSupportCar.getId()));
		//		
		//		workflow.vehicleFinishedCallAcknowledgment(detachedCar.getId());
		//		System.out.println(workflow.isVehicleOnCall(detachedCar.getId()));
		//		
		//		System.out.println("--------------------------Finished Phase Completed--------------------------");
		//		
		//		System.out.println(workflow.isMonitorOnCall(detachedMonitor.getId()));
		//		
		//		System.out.println(workflow.isVehicleOnCall(detachedSupportCar.getId()));
		//		
		//		System.out.println(workflow.isVehicleOnCall(detachedCar.getId()));
		//		
		//		System.out.println("--------------------------Restart Phase Completed--------------------------");
		//		workflow.vehicleLeaving(detachedSupportCar.getId());
		//		System.out.println(workflow.getAvailableSupportVehicles());
	}

	@Override
	public void identifySession(String key) throws LoginException{
		if(!isLoggedIn()){
			if(authenticationManager.isLoggedIn(key)){
				User user = authenticationManager.getUser(key);
				setSessionAttribute(userSessionAttribute, user);
			}else
				throw new LoginException();
		}
	}

	@Override
	public User getUserInfo(){
		if(isLoggedIn()){
			return (User) getSessionAttibute(userSessionAttribute);
		}
		return null;
	}

	@Override
	public void logout() throws CannotLogoutExeception{
		if(isLoggedIn()){
			HttpSession session = this.getThreadLocalRequest().getSession(false);
			if (session == null)
				return;

			System.out.println(getUserInfo().getEmail() + " has logout @ " + DateFormat.getDateInstance().format(new Date()));
			session.invalidate();
		}
	}

	@Override
	public void setPreferredLanguage(AvailableLanguages language){
		if(isLoggedIn()){
			User user = getUserInfo();
			user.setPreferedLanguage(language);

			PersistenceManager pm = PMF.get().getPersistenceManager();
			System.out.println("Changing preferred language for " + user.getEmail() + " to " + user.getPreferedLanguage().name());
			
			pm = PMF.get().getPersistenceManager();
			try{
				pm.currentTransaction().begin();
				user = pm.getObjectById(user.getClass(), user.getEmail());
				user.setPreferedLanguage(language);
				pm.makePersistent(user);
				pm.currentTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				if(pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{				
				pm.close();
			}
		}
	}

	@Override
	public AvailableLanguages getUserLanguage(){
		if(isLoggedIn()){
			return getUserInfo().getPreferedLanguage();
		}
		return null;
	}

	@Override
	public Vehicle getVehicle(String vehicleId){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm = PMF.get().getPersistenceManager();
			Vehicle vehicle = null;
			try{
				vehicle = pm.getObjectById(Car.class, vehicleId);
			}catch(Exception e){
			}finally{				
				pm.close();
			}
			
			if(vehicle == null){
				pm = PMF.get().getPersistenceManager();
				try{
					vehicle = pm.getObjectById(Helicopter.class, vehicleId);
				}catch(Exception e){
				}finally{
					pm.close();
				}
			}
			return vehicle;
		}

		return null;
	}

	@Override
	public Victim getVictim(String email){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			Victim victim = null;
			try{
				victim = pm.getObjectById(Victim.class, email);
			}catch(Exception e){
			}finally{				
				pm.close();
			}

			return victim;
		}
		return null;
	}

	@Override
	public Victim getFullVictim(String email){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm.getFetchPlan().addGroup("all_system_object_attributes");
			pm.getFetchPlan().setMaxFetchDepth(-1);

			Victim victim = null, detached = null;
			try{
				pm.currentTransaction().begin();
				victim = pm.getObjectById(Victim.class, email);
				detached = pm.detachCopy(victim);
				pm.currentTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				if (pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{
				pm.close();
			}

			return detached;
		}
		return null;
	}

	@Override
	public Agent getAgent(String id){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			Agent agent = null;
			try{
				agent = pm.getObjectById(Agent.class, id);
			}catch(Exception e){
			}finally{				
				pm.close();
			}

			return agent;
		}
		return null;
	}

	@Override
	public Agent getFullAgent(String id){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm.getFetchPlan().addGroup("all_system_object_attributes");
			pm.getFetchPlan().setMaxFetchDepth(-1);

			Agent agent = null, detached = null;
			try{
				pm.currentTransaction().begin();
				agent = pm.getObjectById(Agent.class, id);
				detached = pm.detachCopy(agent);
				pm.currentTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				if (pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{
				pm.close();
			}

			return detached;
		}
		return null;
	}

	@Override
	public AttributeInfo getAttributeInfo(String attributeName){
		if(isLoggedIn()){
			PersistenceManager pm = PMF.get().getPersistenceManager();

			pm = PMF.get().getPersistenceManager();
			AttributeInfo attributeInfo = null, detached = null;
			try{
				pm.currentTransaction().begin();
				attributeInfo = pm.getObjectById(AttributeInfo.class, attributeName);
				detached = pm.detachCopy(attributeInfo);
				pm.currentTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				if (pm.currentTransaction().isActive())
					pm.currentTransaction().rollback();
			}finally{
				pm.close();
			}

			return detached;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T> Collection<T> getData(Class<T> t, MoveCursor move, int range, String cursorPositionSessionAttribute, String cursorsListSessionAttribute){
		Integer cursorPosition = (Integer) getSessionAttibute(cursorPositionSessionAttribute);
		List<Cursor> cursors = (List<Cursor>) getSessionAttibute(cursorsListSessionAttribute);

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(t);
		q.setRange(0, range);

		//Initializing
		if(cursorPosition == null && cursors == null){
			cursorPosition = -1;
			cursors = new ArrayList<Cursor>();
		}

		switch(move){
		case BACKWARD:
			if(cursorPosition > 0){
				cursorPosition--;
			}
			break;
		case FORWARD:
			cursorPosition++;
			break;
		case FIRST:
			cursorPosition = -1;
			break;
		default:
			break;
		}

		if(!move.equals(MoveCursor.FIRST)){
			Cursor cursor = cursors.get(cursorPosition);
			Map<String, Object> extensionMap = new HashMap<String, Object>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			q.setExtensions(extensionMap);
		}

		List<T> data = null;
		Collection<T> detachedData = null;

		try {
			data = (List<T>) q.execute();
			if(data != null)
				detachedData = pm.detachCopyAll(data);
		} finally {
			q.closeAll();
			pm.close();
		}

		if(detachedData != null && !detachedData.isEmpty()){
			Cursor cursor = JDOCursorHelper.getCursor(data);
			if((move.equals(MoveCursor.FORWARD) && cursorPosition == cursors.size()-1) ||
					(cursors.isEmpty() && cursorPosition == -1)){
				cursors.add(cursor);
			}else{
				cursors.set(cursorPosition+1, cursor);
			}
		}

		setSessionAttribute(cursorPositionSessionAttribute, cursorPosition);
		setSessionAttribute(cursorsListSessionAttribute, cursors);

		if(detachedData == null)
			return new ArrayList<T>();

		return detachedData;
	}

	@Override
	public List<AttributeInfo> getCustomAttributesInfo(ICustomAttributes[] customAttributes){
		if(isLoggedIn()){
			List<AttributeInfo> attributesInfo = new ArrayList<AttributeInfo>();

			for(ICustomAttributes customAttribute : customAttributes){
				AttributeInfo attrInfo = Connection.attributesInfo.get(customAttribute.getAttributeName());
				if(attrInfo == null){
					PersistenceManager pm = PMF.get().getPersistenceManager();
					

					AttributeInfo attributeInfo = null, detached = null;
					try{
						pm.currentTransaction().begin();
						attributeInfo = pm.getObjectById(AttributeInfo.class, customAttribute.getAttributeName());
						detached = pm.detachCopy(attributeInfo);
						pm.currentTransaction().commit();
					}catch(Exception e){
						e.printStackTrace();
						if (pm.currentTransaction().isActive())
							pm.currentTransaction().rollback();
					}finally{
						pm.close();
					}

					Connection.attributesInfo.put(detached.getAttributeName(), detached);
					attrInfo = detached;
				}
				attributesInfo.add(attrInfo);
			}
			return attributesInfo;
		}
		
		return null;
	}

	protected Boolean isLoggedIn(){
		return getSessionAttibute(userSessionAttribute) != null;
	}

	protected void setSessionAttribute(String attribute, Object value){
		this.getThreadLocalRequest().getSession(true).setAttribute(attribute, value);
	}

	protected Object getSessionAttibute(String attribute){
		return this.getThreadLocalRequest().getSession(true).getAttribute(attribute);
	}

}
