/*
 * Copyright 2015 @author Unai Alegre 
 * 
 * This file is part of DCASE (Design for Context-Aware Systems Engineering), a module 
 * of Modelio that aids the design of a Context-Aware System (C-AS). 
 * 
 * DCASE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DCASE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DCASE. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.module.api;

/**
 * The Class DCaseProperties contains the name of each of the properties from
 * the property pages.
 */

public final class DCaseProperties {

    /****************************************************************************
     * Common properties
     **************************************************************************/

    public static final String PROPERTY_NAME = "Name";

    /****************************************************************************
     * Context Information Message related properties
     **************************************************************************/
    public static final String PROPERTY_MESSAGE_RESPONSIBILITY = "TagResponsibility";

    public static final String PROPERTY_MESSAGE_REGULARITY = "TagRegularity";

    public static final String PROPERTY_MESSAGE_FREQUENCY = "TagFrequency";

    public static final String PROPERTY_MESSAGE_SYNCHRONICITY = "TagSynchronicity";

    public static final String PROPERTY_MESSAGE_SITUATIONAL_PARAMETER = "TagContextAttribute";

    public static final String PROPERTY_ANTECEDENT_STATE_NAME = "TagAntecedentName";

    public static final String PROPERTY_ANTECEDENT_STATE_VALUE = "TagAntecedentValue";

    public static final String PROPERTY_CONSEQUENT_STATE_NAME = "TagConsequentName";

    public static final String PROPERTY_CONSEQUENT_STATE_VALUE = "TagConsequentValue";

    public static final String PROPERTY_PAST_OPERATOR_ID = "TagOperatorId";

    public static final String PROPERTY_PAST_OPERATOR_STATE_NAME = "TagOStateName";

    public static final String PROPERTY_PAST_OPERATOR_STATE_VALUE = "TagOStateValue";

    public static final String PROPERTY_PAST_OPERATOR_BOUND = "TagBound";

    public static final String PROPERTY_PAST_OPERATOR_LOWBOUND = "TagLowBound";

    public static final String PROPERTY_PAST_OPERATOR_UPPBOUND = "TagUppBound";

    public static final String PROPERTY_EVENT_ID = "TagEMId";

    public static final String PROPERTY_EVENT_STATE_NAME = "TagEMStateName";

    public static final String PROPERTY_EVENT_STATE_VALUE = "TagEMStateValue";

    public static final String PROPERTY_EVENT_TIME = "TagEMTime";

    public static final String PROPERTY_STR_ID = "STId";

    public static final String PROPERTY_NTR_ID = "NTId";

    public static final String PROPERTY_PAST_OPERATOR_TYPE = "TagOperatorType";
    
    
    
    public static final String PROPERTY_MESSAGE_ID      = "TagMessageId";

    public static final String PROPERTY_MESSAGE_NAME    = "TagMessageName";

    public static final String PROPERTY_MESSAGE_CONTENT = "TagMessageContent";
    
    

    /****************************************************************************
     * Rule Diagram Related Properties
     **************************************************************************/

    public static final String PROPERTY_MAX_EXECUTION_TIME = "TagExecutionTime";

    /****************************************************************************
     * Specification Related Properties
     **************************************************************************/

    public static final String PROPERTY_SPECIFICATION_TYPE = "TagSpecType";
    public static final String PROPERTY_SPECIFICATION = "TagSpec";

    /****************************************************************************
     * Generic Context Related Properties
     **************************************************************************/

    public static final String PROPERTY_GENERIC_CONTEXT_TYPE = "TagContextType";

    /****************************************************************************
     * ACL Context Related Properties
     **************************************************************************/

    public static final String PROPERTY_CONTEXT_ID = "ACLTagId";

    public static final String PROPERTY_CONTEXT_RESPONSIBILITY = "TagResponsibility";

    public static final String PROPERTY_CONTEXT_FREQUENCY = "TagFrequency";

    public static final String PROPERTY_CONTEXT_LIBTYPE = "TagLibType";

    /****************************************************************************
     * M State Context Related Properties
     **************************************************************************/

    public static final String PROPERTY_STATE_ID = "TagStateId";

    public static final String PROPERTY_STATE_NAME = "TagStateName";

    public static final String PROPERTY_STATE_INDEPENDENT = "TagStateIndpendent";

    public static final String PROPERTY_STATE_INITIAL_VALUE = "TagStateInitial";

	public static final String PROPERTY_INFO_ID = "TagInfoId";
	
	public static final String PROPERTY_INFO_CONTEXT_ATTRIBUTE = "TagInfoContextAttribute";
	
	
    /****************************************************************************
     * Acquisition and Modelling Diagram Related Properties
     **************************************************************************/
	
	
	public static final String PROPERTY_SENSOR_ID 			  = "TagSensorId";
	public static final String PROPERTY_SENSOR_VALUE_TYPE 	  = "TagSensorValueType";
	public static final String PROPERTY_SENSOR_TYPE 		  = "TagSensorType";
	public static final String PROPERTY_SENSOR_REGULARITY 	  = "TagSensorRegularity";
	public static final String PROPERTY_SENSOR_RESPONSIBILITY = "TagSensorResponsibility";
	
	public static final String PROPERTY_PREFERENCE_SENSOR_MAX_VALUE 	  = "TagPreferenceSensorMaxValue";
	public static final String PROPERTY_PREFERENCE_SENSOR_MIN_VALUE 	  = "TagPreferenceSensorMinValue";
	public static final String PROPERTY_PREFERENCE_SENSOR_IS_BOOLEAN 	  = "TagPreferenceSensorIsBoolean";
	
	public static final String PROPERTY_STATIONARY_SENSOR_VERA_ID 	 	  = "TagStationarySensorVeraId";
	public static final String PROPERTY_STATIONARY_SENSOR_MAX_VALUE	      = "TagStationarySensorMaxValue";
	public static final String PROPERTY_STATIONARY_SENSOR_MIN_VALUE 	  = "TagStationarySensorMinValue";
	public static final String PROPERTY_STATIONARY_SENSOR_IS_BOOLEAN 	  = "TagStationarySensorIsBoolean";

	public static final String PROPERTY_MOBILE_SENSOR_LIBRARY 	 	  = "TagStationarySensorVeraId";
	public static final String PROPERTY_MOBILE_SENSOR_FREQUENCY       = "TagStationarySensorMaxValue";
	public static final String PROPERTY_MOBILE_SENSOR_ONTOLOGY 	  	  = "TagStationarySensorMinValue";
	public static final String PROPERTY_MOBILE_SENSOR_DATA 		  	  = "TagStationarySensorIsBoolean";

	public static final String PROPERTY_MODELLING_RULE_ID		  = "TagModellingRuleId";
	
	public static final String PROPERTY_DB_MODELLING_RULE_RULE 	  = "TagDBModellingRuleRule";
	
	
	public static final String PROPERTY_RDF_MODELLING_RULE_LOGICAL_EVALUATIONS 		= "TagRDFModellingRuleLogicalEvaluations";
	public static final String PROPERTY_RDF_MODELLING_RULE_METHOD 					= "TagRDFModellingRuleMethod";	
	public static final String PROPERTY_RDF_MODELLING_RULE_METHOD_TRIPLE_VAR 		= "TagRDFModellingRuleMethodTripleVar";	
	public static final String PROPERTY_RDF_MODELLING_RULE_METHOD_RESULT_EXPR 		= "TagRDFModellingRuleMethodResultExpr";		

	public static final String PROPERTY_FEEDS_IN_WINDOW_STREAM		= "SR_stream";
	public static final String PROPERTY_FEEDS_IN_WINDOW_EVERY		= "SR_every";
	public static final String PROPERTY_FEEDS_IN_WINDOW_FOR			= "SR_for";
	////////////////////////////
	
	public static final String PROPERTY_ACTUATOR_CONFIGS			= "TagActuatorConfigs";
	
	public static final String PROPERTY_VERA_ACTUATOR_SERVICE_ID	 = "TagVeraActuatorServiceId";
	public static final String PROPERTY_VERA_ACTUATOR_ACTION_COMMAND = "TagVeraActuatorActionCommand";	
	
	public static final String PROPERTY_VERA_ROUTER_HOSTNAME			= "TagVeraRouterHostname";
	public static final String PROPERTY_VERA_ROUTER_PORT				= "TagVeraRouterPort";
	public static final String PROPERTY_VERA_ROUTER_USERNAME			= "TagVeraRouterUsername";
	public static final String PROPERTY_VERA_ROUTER_PASSWORD			= "TagVeraRouterPassword";
	
    private DCaseProperties() {

    }

}
