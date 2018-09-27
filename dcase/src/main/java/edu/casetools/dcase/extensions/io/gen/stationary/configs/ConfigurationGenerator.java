package edu.casetools.dcase.extensions.io.gen.stationary.configs;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.smkernel.mapi.MObject;

import edu.casetools.dcase.module.api.DCaseProperties;
import edu.casetools.dcase.module.i18n.I18nMessageService;
import edu.casetools.dcase.module.impl.DCasePeerModule;
import edu.casetools.dcase.extensions.io.AbstractModelWriter;

public class ConfigurationGenerator extends AbstractModelWriter {

	private MObject mReasoner;
	private MObject mDatabase;
	private StringBuilder result;
	private enum REASONER_FIELDS {USE_FIXED_ITERATION_TIME,FIXED_ITERATION_TIME,EXECUTION_TIME,USE_MAX_EXECUTION_TIME};
	private enum DATABASE_FIELDS {TYPE,DRIVER,IP,PORT,USER,PASSWORD,NAME};
	private final String POSTGRES_DRIVER = "org.postgresql.Driver";
	private final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	
	public ConfigurationGenerator(MObject mReasoner, MObject mDatabase){
		this.mReasoner = mReasoner;
		this.mDatabase = mDatabase;
		this.result = new StringBuilder();
	}
	

	@Override
	public void write(Package model) {
		
	}

	@Override
	public String writeToString() {
		result = new StringBuilder();
		header();
		mReasonerConfigs();
		body();
		mDatabaseConfigs();
		return result.toString();
	}
	
	private void header(){

		append("<USE_STRATIFICATION>");
		append(" false");
		append("</USE_STRATIFICATION>");
		append("<EXECUTION_MODE>");
		append(" REAL_ENVIRONMENT");
		append("</EXECUTION_MODE>");
	}


	private void append(String string) {
		result.append(string);
		separator();
	}


	private void mReasonerConfigs() {
		mReasonerConfig(true,REASONER_FIELDS.USE_FIXED_ITERATION_TIME);
		appendModelioValue(mReasoner,DCaseProperties.PROPERTY_M_REASONER_FIXED_ITERATION_TIME);
		mReasonerConfig(false,REASONER_FIELDS.USE_FIXED_ITERATION_TIME);
		
		mReasonerConfig(true,REASONER_FIELDS.FIXED_ITERATION_TIME);
		appendModelioValue(mReasoner,DCaseProperties.PROPERTY_M_REASONER_ITERATION_TIME);
		mReasonerConfig(false,REASONER_FIELDS.FIXED_ITERATION_TIME);

		mReasonerConfig(true,REASONER_FIELDS.EXECUTION_TIME);
		appendModelioValue(mReasoner,DCaseProperties.PROPERTY_M_REASONER_MAX_EXECUTION_TIME);
		mReasonerConfig(false,REASONER_FIELDS.EXECUTION_TIME);
		
		mReasonerConfig(true,REASONER_FIELDS.USE_MAX_EXECUTION_TIME);
		appendModelioValue(mReasoner,DCaseProperties.PROPERTY_M_REASONER_HAS_MAX_EXECUTION_TIME);		
		mReasonerConfig(false,REASONER_FIELDS.USE_MAX_EXECUTION_TIME);
	}
	
	private void mReasonerConfig(boolean open, REASONER_FIELDS field) {
		if(open) open();
		else openBar();
		switch(field){
			case USE_FIXED_ITERATION_TIME:
				result.append("USE_FIXED_ITERATION_TIME");
				break;
			case FIXED_ITERATION_TIME:
				result.append("FIXED_ITERATION_TIME");
				break;
			case EXECUTION_TIME:
				result.append("EXECUTION_TIME");
				break;
			case USE_MAX_EXECUTION_TIME:
				result.append("USE_MAX_EXECUTION_TIME");
				break;
		}
		close();
		separator();
	}

	private void separator() {
		result.append(System.lineSeparator());
	}


	private void body(){
		append("<SYSTEM_SPECIFICATION_FILE_PATH>");
		append("</SYSTEM_SPECIFICATION_FILE_PATH>");
		append("<RESULTS_FILE_PATH>");
		append("</RESULTS_FILE_PATH>");
		append("<LFPUBS_OUTPUT_FILE_PATH>");
		append("</LFPUBS_OUTPUT_FILE_PATH>");
		append("<SESSION_FILE_PATH>");
		append("</SESSION_FILE_PATH>");
		append("<SSH_CONFIGS_FILE_PATH>");
		append("</SSH_CONFIGS_FILE_PATH>");
	}
	
	private void mDatabaseConfigs() {

		mDatabaseConfig(true,DATABASE_FIELDS.TYPE);
		appendModelioValue(mDatabase,DCaseProperties.PROPERTY_M_DATABASE_TYPE);
		mDatabaseConfig(false,DATABASE_FIELDS.TYPE);
		
		mDatabaseConfig(true,DATABASE_FIELDS.DRIVER);
		getDriver(DCaseProperties.PROPERTY_M_DATABASE_TYPE);
		mDatabaseConfig(false,DATABASE_FIELDS.DRIVER);
		
		mDatabaseConfig(true,DATABASE_FIELDS.IP);
		appendModelioValue(mDatabase,DCaseProperties.PROPERTY_M_DATABASE_HOSTNAME);
		mDatabaseConfig(false,DATABASE_FIELDS.IP);
		
		mDatabaseConfig(true,DATABASE_FIELDS.PORT);
		appendModelioValue(mDatabase,DCaseProperties.PROPERTY_M_DATABASE_PORT);
		mDatabaseConfig(false,DATABASE_FIELDS.PORT);
		
		mDatabaseConfig(true,DATABASE_FIELDS.USER);
		appendModelioValue(mDatabase,DCaseProperties.PROPERTY_M_DATABASE_USERNAME);
		mDatabaseConfig(false,DATABASE_FIELDS.USER);
		
		mDatabaseConfig(true,DATABASE_FIELDS.PASSWORD);
		appendModelioValue(mDatabase,DCaseProperties.PROPERTY_M_DATABASE_PASSWORD);
		mDatabaseConfig(false,DATABASE_FIELDS.PASSWORD);
		
		mDatabaseConfig(true,DATABASE_FIELDS.NAME);
		append(mDatabase.getName().replaceAll("\\s", "_").toLowerCase());
		mDatabaseConfig(false,DATABASE_FIELDS.NAME);
		
	}
	
	private void getDriver(String propertyMDatabaseType) {
		String type = ((ModelElement) mDatabase).getTagValue(DCasePeerModule.MODULE_NAME,
			    DCaseProperties.PROPERTY_M_DATABASE_TYPE);
		
		if(type.equals(I18nMessageService.getString("Ui.MDatabase.Property.Type.MYSQL")))
			type = MYSQL_DRIVER;
		else if(type.equals(I18nMessageService.getString("Ui.MDatabase.Property.Type.PostgreSQL")))
			type = POSTGRES_DRIVER;
		else if(type.equals(I18nMessageService.getString("Ui.MDatabase.Property.Type.Other")))
			type = "";

	}


	private void mDatabaseConfig(boolean open, DATABASE_FIELDS field) {
		String db = "DATABASE_";
		if(open) open();
		else openBar();
		switch(field){
			case TYPE:
				result.append(db+"TYPE");
				break;
			case DRIVER:
				result.append(db+"DRIVER");
				break;
			case IP:
				result.append(db+"IP");
				break;
			case PORT:
				result.append(db+"PORT");
				break;
			case USER:
				result.append(db+"USER");
				break;
			case PASSWORD:
				result.append(db+"PASSWORD");
				break;
			case NAME:
				result.append(db+"NAME");
				break;
		}
		close();
		separator();
	}
	
	private void appendModelioValue(MObject element, String property) {
		append(((ModelElement) element).getTagValue(DCasePeerModule.MODULE_NAME,
					    property));
	}
		
	private void open(){
		result.append("<");
	}
	
	private void close(){
		result.append(">");
	}
	
	private void openBar(){
		result.append("</");
	}
	
}
