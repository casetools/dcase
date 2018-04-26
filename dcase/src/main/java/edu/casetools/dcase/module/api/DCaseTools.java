/*
 * Copyright 2015 @author Unai Alegre 
 * 
 * This file is part of DCASE (Design for Context-Aware Systems Engineering), a module 
 * of Modelio that aids the requirements elicitation stage of a Context-Aware System (C-AS). 
 * 
 * DCASE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DCASE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DCASE.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.casetools.dcase.module.api;

/**
 * The Class DCaseTools contains the name of the tools.
 */
public final class DCaseTools {

	public static final String TOOL_CONTEXT_STATE 		      = "ContextStateTool";
	public static final String TOOL_INTERNAL_TIME_STATE 	  = "InternalTimeStateTool";
	
    public static final String TOOL_ANTECEDENT_GROUP 		  = "AntecedentGroupTool";
    public static final String TOOL_ANTECEDENT 				  = "AntecedentTool";
    public static final String TOOL_CONSEQUENT 				  = "ConsequentTool";

    public static final String TOOL_IMMEDIATE_PAST_OPERATOR   = "ImmediatePastOperatorTool";
    public static final String TOOL_ABSOLUTE_PAST_OPERATOR 	  = "AbsolutePastOperatorTool";
    
    public static final String TOOL_SPECIFICATION 			  = "SpecificationTool";
    public static final String TOOL_SPECIFICATION_SET 		  = "SpecificationSetTool";
    
    public static final String TOOL_SAME_TIME 				  = "SameTimeTool";
    public static final String TOOL_NEXT_TIME 				  = "NextTimeTool";
	public static final String TOOL_PRODUCES 				  = "ProduceTool";
	public static final String TOOL_ANDROID_MESSAGE_INTERFACE = "AndroidMessageInterfaceTool";
	public static final String TOOL_JAVA_MESSAGE_INTERFACE    = "JavaMessageInterfaceTool";
	public static final String TOOL_MESSAGE 				  = "MessageTool";
	public static final String TOOL_INFO 					  = "InfoTool";
	public static final String TOOL_OPTION_LIST 			  = "OptionListTool";
	public static final String TOOL_LIST_ITEM 				  = "ListItemTool";
	
	public static final String TOOL_DISPLAYS 				  = "DisplaysTool";
	public static final String TOOL_RECEIVES 				  = "ReceivesTool";
	
	public static final String TOOL_PREFERENCE_SENSOR    	  = "PreferenceSensorTool";
	public static final String TOOL_STATIONARY_SENSOR		  = "StationarySensorTool";
	public static final String TOOL_MOBILE_SENSOR			  = "MobileSensorTool";
	public static final String TOOL_DB_MODELLING_RULE 		  = "DBModellingRuleTool";
	public static final String TOOL_RDF_MODELLING_RULE 		  = "RDFModellingRuleTool";
	public static final String TOOL_OBSERVE 		  		  = "ObserveTool";
	public static final String TOOL_FEEDS 		  			  = "FeedsTool";
	public static final String TOOL_FEEDS_IN_WINDOW 		  = "FeedsInWindowTool";
	
    private DCaseTools() {

    }

}
