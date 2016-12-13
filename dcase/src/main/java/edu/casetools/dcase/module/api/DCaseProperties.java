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

public class DCaseProperties {

    /****************************************************************************
     * Common properties
     **************************************************************************/

    public static final String PROPERTY_NAME = "Name";

    /****************************************************************************
     * Context Information Message related properties
     **************************************************************************/
    // Situational Parameter
    public static final String MESSAGE_ID = "CETagId";

    public static final String MESSAGE_RESPONSIBILITY = "TagResponsibility";

    public static final String MESSAGE_REGULARITY = "TagRegularity";

    public static final String MESSAGE_FREQUENCY = "TagFrequency";

    public static final String MESSAGE_SYNCHRONICITY = "TagSynchronicity";

    public static final String MESSAGE_SITUATIONAL_PARAMETER = "TagContextAttribute";

    public static final String STATE_ID = "TagStateId";

    public static final String STATE_NAME = "TagStateName";

    public static final String STATE_INDEPENDENT = "TagStateIndpendent";

    public static final String STATE_INITIAL_VALUE = "TagStateInitial";

    public static final String ANTECEDENT_STATE_NAME = "TagAntecedentName";

    public static final String ANTECEDENT_STATE_VALUE = "TagAntecedentValue";

    public static final String CONSEQUENT_STATE_NAME = "TagConsequentName";

    public static final String CONSEQUENT_STATE_VALUE = "TagConsequentValue";

    public static final String PAST_OPERATOR_ID = "TagOperatorId";

    public static final String PAST_OPERATOR_STATE_NAME = "TagOStateName";

    public static final String PAST_OPERATOR_STATE_VALUE = "TagOStateValue";

    public static final String PAST_OPERATOR_BOUND = "TagBound";

    public static final String PAST_OPERATOR_LOWBOUND = "TagLowBound";

    public static final String PAST_OPERATOR_UPPBOUND = "TagUppBound";

    public static final String EVENT_ID = "TagEMId";

    public static final String EVENT_STATE_NAME = "TagEMStateName";

    public static final String EVENT_STATE_VALUE = "TagEMStateValue";

    public static final String EVENT_TIME = "TagEMTime";

    public static final String STR_ID = "STId";

    public static final String NTR_ID = "NTId";

    public static final String PAST_OPERATOR_TYPE = "TagOperatorType";

    public static final String TEST_CASE_MESSAGE_ID = "TagEMId";

    public static final String TEST_CASE_MESSAGE_STATE_NAME = "TagEMStateName";

    public static final String TEST_CASE_MESSAGE_STATE_VALUE = "TagEMStateValue";

    public static final String TEST_CASE_MESSAGE_TIME = "TagEMTime";

    public static final String TEST_CASE_EXECUTION_TIME = "TestExecutionTime";

    private DCaseProperties() {

    }

}
