/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package geogebra.kernel.cas;

import geogebra.common.euclidian.EuclidianConstants;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoFunction;
import geogebra.common.kernel.geos.GeoPoint2;
import geogebra.kernel.Construction;
import geogebra.kernel.algos.AlgoPointOnPath;
import geogebra.kernel.geos.GeoLine;


public class AlgoTangentFunctionPoint extends AlgoUsingTempCASalgo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GeoPoint2 P; // input
	private GeoLine tangent; // output  
	private GeoFunction f;
	private GeoPoint2 T;
	private boolean pointOnFunction;
	private GeoFunction deriv;

	public AlgoTangentFunctionPoint(
			Construction cons,
			String label,
			GeoPoint2 P,
			GeoFunction f) {
		this(cons, P, f);
		tangent.setLabel(label);
	}

	public AlgoTangentFunctionPoint(
			Construction cons,
			GeoPoint2 P,
			GeoFunction f) {
		super(cons);
		this.P = P;
		this.f = f;

		tangent = new GeoLine(cons);

		// check if P is defined as a point of the function's graph
		pointOnFunction = false;
		if (P.getParentAlgorithm() instanceof AlgoPointOnPath) {
			AlgoPointOnPath algo = (AlgoPointOnPath) P.getParentAlgorithm();
			pointOnFunction = algo.getPath() == f;
		}        

		if (pointOnFunction)
			T = P;
		else
			T = new GeoPoint2(cons);
		tangent.setStartPoint(T);

		// derivative of f
		algoCAS = new AlgoDerivative(cons, f);       
		deriv = (GeoFunction) ((AlgoDerivative)algoCAS).getResult();
		cons.removeFromConstructionList(algoCAS);

		setInputOutput(); // for AlgoElement                
		compute();
	}

	public String getClassName() {
		return "AlgoTangentFunctionPoint";
	}

	public int getRelatedModeID() {
		return EuclidianConstants.MODE_TANGENTS;
	}


	// for AlgoElement
	protected void setInputOutput() {
		input = new GeoElement[2];
		input[0] = P;
		input[1] = f;

		output = new GeoElement[1];
		output[0] = tangent;
		setDependencies(); // done by AlgoElement
	}

	public GeoLine getTangent() {
		return tangent;
	}
	GeoFunction getFunction() {
		return f;
	}
	GeoPoint2 getPoint() {
		return P;
	}
	GeoPoint2 getTangentPoint() {
		return T;
	}

	// calc tangent at x=a
	public final void compute() {
		if (!(f.isDefined() && P.isDefined() && deriv.isDefined())) {
			tangent.setUndefined();
			return;
		}      

		// calc the tangent;
		double a = P.inhomX;
		double fa = f.evaluate(a);
		double slope = deriv.evaluate(a);
		tangent.setCoords(-slope, 1.0, a * slope - fa);

		if (!pointOnFunction)
			T.setCoords(a, fa, 1.0);
	}

	public final String toString() {
		// Michael Borcherds 2008-03-30
		// simplified to allow better Chinese translation
		return app.getPlain("TangentToAatB",f.getLabel(),"x = x("+P.getLabel()+")");

	}

}
