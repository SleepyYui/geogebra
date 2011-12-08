/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

*/

package geogebra.kernel.algos;

import geogebra.common.euclidian.EuclidianConstants;
import geogebra.common.kernel.Region;
import geogebra.common.kernel.algos.AlgoElement;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoPoint2;
import geogebra.kernel.Construction;
import geogebra.main.Application;


/**
 * Point in region algorithm
 * @author mathieu
 *
 */
public class AlgoPointInRegion extends AlgoElement {

	private Region region; // input
    private GeoPoint2 P; // output       

    public AlgoPointInRegion(
        Construction cons,
        String label,
        Region region,
        double x,
        double y) {
        super(cons);
        this.region = region;
        P = new GeoPoint2(cons, region);
        P.setCoords(x, y, 1.0);

        setInputOutput(); // for AlgoElement

        
        compute();
        P.setLabel(label);
    }

    @Override
	public String getClassName() {
        return "AlgoPointInRegion";
    }
    
    @Override
	public int getRelatedModeID() {
    	return EuclidianConstants.MODE_POINT_ON_OBJECT;
    }

    // for AlgoElement
    @Override
	protected void setInputOutput() {
        input = new GeoElement[1];
        input[0] = (GeoElement)region.toGeoElement();

        setOutputLength(1);
        setOutput(0,P);
        setDependencies(); // done by AlgoElement
    }

    /** returns the point 
     * @return resulting point 
     */
    public GeoPoint2 getP() {
        return P;        
    }
    /**
     * Returns the region
     * @return region
     */
    Region getRegion() {
        return region;
    }

    @Override
	public final void compute() {
    	
    	if (input[0].isDefined()) {	    	
	        region.regionChanged(P);
	        P.updateCoords();
    	} else {
    		P.setUndefined();
    	}
    }

    @Override
	final public String toString() {
        // Michael Borcherds 2008-03-30
        // simplified to allow better Chinese translation
        return app.getPlain("PointInA",input[0].getLabel());

    }
}
