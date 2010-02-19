// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions.rawdata;

import java.util.List;

import org.projectusus.core.internal.proportions.model.IHotspot;

interface IRawData {

    int getViolationCount( CodeProportionKind metric );

    int getNumberOf( CodeProportionUnit unit );

    void addToHotspots( CodeProportionKind metric, List<IHotspot> hotspots );

    int getOverallMetric( CodeProportionKind metric );

    void resetRawData();
}
