package edu.casetools.dcase.extensions.io.gen.mobile.areasoner.reasoner;

import edu.casetools.dcase.m2nusmv.data.elements.BoundedOperator;
import edu.casetools.dcase.m2nusmv.data.elements.Rule;

public class MobileReasoner {

	public static StringBuilder generateReasoningRuleQuery(Rule rule) {
		StringBuilder result = new StringBuilder("");
		if(!rule.getAntecedents().isEmpty()){
			result.append(rule.getAntecedents().get(0).getName().toUpperCase().replaceAll("\\s", "_"));
			for(int i=1;i<rule.getAntecedents().size();i++){
				result.append(" and " + rule.getAntecedents().get(i).getName().toUpperCase().replaceAll("\\s", "_"));
				
			}
		}
		if(!rule.getAntecedents().isEmpty() && rule.getBops().isEmpty()) result.append(" and ");
		
		if(!rule.getBops().isEmpty()){
			result = appendBOP(rule.getBops().get(0), result);
			
			for(int i=1;i<rule.getAntecedents().size();i++){
				result = appendBOP(rule.getBops().get(i), result);
			}
		}
		result.append(" iff ");
		result.append(rule.getConsequent().getName().toUpperCase().replaceAll("\\s", "_"));
		return result;
	}

	private static StringBuilder appendBOP(BoundedOperator boundedOperator, StringBuilder result) {
		switch(boundedOperator.getType()){
		case STRONG_IMMEDIATE_PAST:
			result.append("[#"+boundedOperator.getLowBound()+"]"+boundedOperator.getStateName().toUpperCase().replaceAll("\\s", "_"));
			break;
		case WEAK_IMMEDIATE_PAST:
			result.append("["+boundedOperator.getLowBound()+"]"+boundedOperator.getStateName().toUpperCase().replaceAll("\\s", "_"));
			break;
		case STRONG_ABSOLUTE_PAST:
			result.append("[#"+boundedOperator.getLowBound()+"-"+boundedOperator.getUppBound()+"]"+boundedOperator.getStateName().toUpperCase().replaceAll("\\s", "_"));
		break;
		case WEAK_ABSOLUTE_PAST:
			result.append("["+boundedOperator.getLowBound()+"-"+boundedOperator.getUppBound()+"]"+boundedOperator.getStateName().toUpperCase().replaceAll("\\s", "_"));
			break;
		default:
			break;
		}
		return result;
	}

}
