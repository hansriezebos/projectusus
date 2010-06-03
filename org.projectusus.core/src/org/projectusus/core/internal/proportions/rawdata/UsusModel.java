// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions.rawdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.projectusus.core.IMetricsAccessor;
import org.projectusus.core.IUsusElement;
import org.projectusus.core.IUsusModel;
import org.projectusus.core.IUsusModelListener;
import org.projectusus.core.basis.CodeProportion;
import org.projectusus.core.basis.CodeProportionKind;
import org.projectusus.core.filerelations.model.ClassDescriptor;
import org.projectusus.core.filerelations.model.ClassDescriptorCleanup;
import org.projectusus.core.filerelations.model.Packagename;
import org.projectusus.core.internal.proportions.IMetricsWriter;
import org.projectusus.core.internal.proportions.IUsusModelForAdapter;
import org.projectusus.core.internal.proportions.model.UsusModelCache;
import org.projectusus.core.internal.proportions.modelupdate.checkpoints.CheckpointHistory;

import com.mountainminds.eclemma.core.analysis.IJavaModelCoverage;

public class UsusModel implements IUsusModel, IUsusModelForAdapter {

    private final Set<IUsusModelListener> listeners;
    private final CheckpointHistory history;
    private final UsusModelCache cache;
    private final MetricsAccessor metrics;
    private boolean needsFullRecompute;

    public UsusModel() {
        cache = new UsusModelCache();
        listeners = new HashSet<IUsusModelListener>();
        history = new CheckpointHistory();
        metrics = new MetricsAccessor();
        needsFullRecompute = false;
    }

    // interface of IUsusModelWriteAccess
    // //////////////////////////////////

    public void updateAfterComputationRun( boolean computationSuccessful, IProgressMonitor monitor ) {
        needsFullRecompute = !computationSuccessful;
        metrics.cleanupRelations( monitor );
        ArrayList<CodeProportion> codeProportions = metrics.getCodeProportions();
        history.addComputationResult( codeProportions );
        cache.refreshAll( codeProportions );
        notifyListeners();
    }

    public void dropRawData( IProject project ) {
        metrics.dropRawData( project );
    }

    public void dropRawData( IFile file ) {
        metrics.dropRawData( file );
    }

    // interface of IUsusModel
    // ////////////////////////

    public void collectCoverageInfo( IJavaModelCoverage javaModelCoverage ) {
        if( javaModelCoverage.getInstrumentedProjects().length == 0 ) {
            return;
        }
        metrics.updateCoverage( javaModelCoverage );
        CodeProportion codeProportion = getCodeProportion( CodeProportionKind.TA );
        history.addTestResult( codeProportion );
        cache.refresh( codeProportion );
        notifyListeners();
    }

    public CheckpointHistory getHistory() {
        return history;
    }

    public IUsusElement[] getElements() {
        return cache.getElements().clone();
    }

    // ///////////////////////////////////////////////////////////////////////

    // Methoden, die auf WorkspaceRawData zugreifen:
    public CodeProportion getCodeProportion( CodeProportionKind metric ) {
        return cache.getCodeProportion( metric );
    }

    // //////////////////////////////////

    public void addUsusModelListener( IUsusModelListener listener ) {
        listeners.add( listener );
    }

    public void removeUsusModelListener( IUsusModelListener listener ) {
        listeners.remove( listener );
    }

    // internal
    // ////////

    private void notifyListeners() {
        for( IUsusModelListener listener : listeners ) {
            listener.ususModelChanged();
        }
    }

    public boolean needsFullRecompute() {
        return needsFullRecompute;
    }

    public IMetricsAccessor getMetricsAccessor() {
        return metrics;
    }

    public IMetricsWriter getMetricsWriter() {
        return metrics;
    }

    public static void clear() {
        ClassDescriptor.clear();
        ClassDescriptorCleanup.clear();
        Packagename.clear();
    }
}
