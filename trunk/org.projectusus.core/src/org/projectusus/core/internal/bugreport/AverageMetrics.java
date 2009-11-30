// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.bugreport;

import static org.projectusus.core.internal.util.CoreTexts.AverageMetrics_overall;

import org.projectusus.core.internal.proportions.rawdata.CodeProportionKind;
import org.projectusus.core.internal.proportions.rawdata.ProjectRawData;

public class AverageMetrics implements IAverageMetrics {

    private int numberOfMethods;
    private int totalCc;
    private int totalML;
    private int numberOfClasses;

    public double getAverageCyclomaticComplexity() {
        return calculateAverage( totalCc, numberOfMethods );
    }

    private double calculateAverage( int metricValue, int metricValueBasis ) {
        if( metricValueBasis == 0 ) {
            return 0.0;
        }
        return metricValue / metricValueBasis;
    }

    public double getAverageMethodLength() {
        return calculateAverage( totalML, numberOfMethods );
    }

    public double getAverageNumberOfMethodsInClass() {
        return calculateAverage( numberOfMethods, numberOfClasses );
    }

    public void addProjectResults( ProjectRawData projectResults ) {
        numberOfMethods += projectResults.getViolationBasis( CodeProportionKind.CC );
        numberOfClasses += projectResults.getViolationBasis( CodeProportionKind.KG );

        totalCc += projectResults.getOverallMetric( CodeProportionKind.CC );
        totalML += projectResults.getOverallMetric( CodeProportionKind.ML );

    }

    public String getName() {
        return AverageMetrics_overall;
    }

}
