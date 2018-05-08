/*
 * Copyright 2015 The ContextModeller Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.casetools.dcase.utils;

import java.util.ArrayList;
import java.util.List;

import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;


public class ModelUtils {


	public static boolean hasTaggedValue(String tagtype, ModelElement _element) {

		for (TaggedValue tag : _element.getTag()) {
			TagType type = tag.getDefinition();
			String tagname = type.getName();
			if (tagname.equals(tagtype)) {
				return true;
			}
		}

		return false;

	}




	public static ArrayList<RDFTriple> getRDFTriples(String rdf) {
		ArrayList<RDFTriple> triples = new ArrayList<>();

		String[] rdfs = rdf.split("\\.");

		for (String triple : rdfs) {
			triples.add(new RDFTriple(triple));
		}

		return triples;

	}

	public static RDFTriple getRDFTripleForVar(String rdf, String var) {

		return getRDFTripleForVar(getRDFTriples(rdf), var);

	}

	public static RDFTriple getRDFTripleForVar(List<RDFTriple> triples, String var) {

		RDFTriple result = null;

		for (RDFTriple triple : triples) {
			if (triple.containsVariable(var)) {
				result = triple;
			}
		}

		return result;

	}

	public static RDFTriple getRDFTripleForVars(String rdf, String[] var) {

		return getRDFTripleForVars(getRDFTriples(rdf), var);

	}

	public static RDFTriple getRDFTripleForVars(ArrayList<RDFTriple> triples, String[] var) {

		RDFTriple result = null;

		for (RDFTriple triple : triples) {
			if (triple.containsVariables(var)) {
				result = triple;
			}
		}

		return result;

	}
	
	public static List<Stereotype> computePropertyList(ModelElement element) {
		List<Stereotype> result = new ArrayList<Stereotype>();
		int i = 0;

		for (Stereotype ster : element.getExtension()) {
			if ((ster.getOwner().getOwnerModule().getName()
					.equals("ContextModeller")) && (!result.contains(ster))) {
				result.add(ster);

				Stereotype parent = ster.getParent();
				while ((parent != null) && (!result.contains(parent))) {
					result.add(i, parent);
					ster = parent;
					parent = ster.getParent();
				}
				i = result.size();
			}

		}

		return result;
	}


}
