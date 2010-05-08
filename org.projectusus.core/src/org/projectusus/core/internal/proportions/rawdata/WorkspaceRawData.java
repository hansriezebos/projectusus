// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions.rawdata;

import static org.projectusus.core.basis.CodeProportionKind.TA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.projectusus.core.basis.CodeProportion;
import org.projectusus.core.basis.CodeProportionKind;
import org.projectusus.core.basis.CodeProportionUnit;
import org.projectusus.core.basis.CodeStatistic;
import org.projectusus.core.basis.IHotspot;
import org.projectusus.core.internal.UsusCorePlugin;
import org.projectusus.core.internal.coverage.TestCoverage;

class WorkspaceRawData extends RawData<IProject, ProjectRawData> {

    public WorkspaceRawData() {
        super();
    }

    ProjectRawData getProjectRawData( IProject project ) {
        ProjectRawData rawData = super.getRawData( project );
        if( rawData == null ) {
            rawData = new ProjectRawData();
            super.addRawData( project, rawData );
        }
        return rawData;
    }

    public void dropRawData( IProject project ) {
        for( IFile file : getProjectRawData( project ).getAllKeys() ) {
            UsusCorePlugin.getUsusModelMetricsWriter().getFileRelationMetrics().handleFileRemoval( file );
        }
        remove( project );
    }

    public void dropRawData( IFile file ) {
        getProjectRawData( file.getProject() ).dropRawData( file );
    }

    public CodeProportion getCodeProportion( CodeProportionKind metric ) {
        int violations;
        CodeStatistic basis;
        List<IHotspot> hotspots;
        if( metric == TA ) { // for the time being
            int total = getInstructionCoverage().getTotalCount();
            violations = total - getInstructionCoverage().getCoveredCount();
            basis = new CodeStatistic( TA.getUnit(), total );
            hotspots = new ArrayList<IHotspot>();
            // TODO lf add hotspots
        } else {
            violations = getViolationCount( metric );
            basis = getCodeStatistic( metric.getUnit() );
            hotspots = computeHotspots( metric );
        }
        return new CodeProportion( metric, violations, basis, hotspots );
    }

    public CodeStatistic getCodeStatistic( CodeProportionUnit unit ) {
        int basis = getNumberOf( unit );
        return new CodeStatistic( unit, basis );
    }

    private List<IHotspot> computeHotspots( CodeProportionKind metric ) {
        List<IHotspot> hotspots = new ArrayList<IHotspot>();
        addToHotspots( metric, hotspots );
        return hotspots;
    }

    public Set<ClassRawData> getAllClassRawData() {
        Set<ClassRawData> allClassRawData = new HashSet<ClassRawData>();
        for( ProjectRawData projectRD : getAllRawDataElements() ) {
            for( FileRawData fileRD : projectRD.getAllRawDataElements() ) {
                allClassRawData.addAll( fileRD.getAllRawDataElements() );
            }
        }
        return allClassRawData;
    }

    TestCoverage getInstructionCoverage() {
        TestCoverage result = new TestCoverage( 0, 0 );
        for( ProjectRawData projectRD : getAllRawDataElements() ) {
            result = result.add( projectRD.getInstructionCoverage() );
        }
        return result;
    }

    public void resetInstructionCoverage() {
        for( ProjectRawData projectRD : getAllRawDataElements() ) {
            projectRD.setInstructionCoverage( null );
        }
    }
}
