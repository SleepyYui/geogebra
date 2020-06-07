package org.geogebra.common.properties.impl.objects;

import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLine;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoVec3D;
import org.geogebra.common.kernel.kernelND.GeoConicND;
import org.geogebra.common.main.App;
import org.geogebra.common.properties.EnumerableProperty;

/**
 * Equation form
 */
public class EquationFormProperty extends AbstractGeoElementProperty implements EnumerableProperty {

    private static final String[] equationFormNames = {
            "ImplicitLineEquation",
            "ExplicitLineEquation",
            "ParametricForm",
            "GeneralLineEquation",
            "InputForm"
    };

    public EquationFormProperty(GeoElement geoElement) throws NotApplicablePropertyException {
        super("Equation", geoElement);
    }

    @Override
    public String[] getValues() {
        return equationFormNames;
    }

    @Override
    public int getIndex() {
        return getElement().getToStringMode();
    }

    @Override
    public void setIndex(int equationForm) {
        if (getElement() instanceof GeoVec3D) {
            GeoVec3D vec3d = (GeoVec3D) getElement();
            vec3d.setMode(equationForm);
            vec3d.updateRepaint();
        }
    }

    @Override
    boolean isApplicableTo(GeoElement element) {
        if (isTextOrInput(element)) {
            return false;
        }
        if (element instanceof GeoList) {
            return isApplicableToGeoList((GeoList) element);
        }
        return hasEquationModeSetting(element);
    }

    /**
     * Returns true, if the equation mode setting
     * should be shown.
     * @return true if setting should be shown.
     */
    private boolean hasEquationModeSetting(GeoElement element) {
        return !isEnforcedEquationForm(element)
                && element instanceof GeoLine
                && !element.isNumberValue()
                && element.getDefinition() == null;
    }

    private boolean isEnforcedEquationForm(GeoElement element) {
        App app = element.getApp();
        boolean isEnforcedLineEquationForm = element instanceof GeoLine
                && app.getConfig().getEnforcedLineEquationForm() != -1;
        boolean isEnforcedConicEquationForm = element instanceof GeoConicND
                && app.getConfig().getEnforcedConicEquationForm() != -1;
        return isEnforcedLineEquationForm || isEnforcedConicEquationForm;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
