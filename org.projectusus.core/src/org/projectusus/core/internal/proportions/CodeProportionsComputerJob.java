// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions;

import static org.projectusus.core.internal.proportions.sqi.IsisMetrics.CC;
import static org.projectusus.core.internal.proportions.sqi.IsisMetrics.KG;
import static org.projectusus.core.internal.proportions.sqi.IsisMetrics.ML;
import static org.projectusus.core.internal.util.CoreTexts.codeProportionsComputerJob_computing;
import static org.projectusus.core.internal.util.CoreTexts.codeProportionsComputerJob_name;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.projectusus.core.internal.proportions.checkstyledriver.CheckstyleDriver;
import org.projectusus.core.internal.proportions.sqi.IsisMetrics;
import org.projectusus.core.internal.proportions.sqi.NewWorkspaceResults;
import org.projectusus.core.internal.yellowcount.IYellowCountResult;
import org.projectusus.core.internal.yellowcount.YellowCount;

class CodeProportionsComputerJob extends Job {

    private static final Object FAMILY = new Object();
    private static final ISchedulingRule MUTEX = new MutexSchedulingRule();
    private final ICodeProportionComputationTarget target;

    CodeProportionsComputerJob( ICodeProportionComputationTarget target ) {
        super( codeProportionsComputerJob_name );
        setRule( MUTEX );
        setPriority( Job.DECORATE );
        this.target = target;
    }

    @Override
    public boolean belongsTo( Object family ) {
        return family == FAMILY;
    }

    @Override
    protected IStatus run( IProgressMonitor mo ) {
        IProgressMonitor monitor = mo == null ? new NullProgressMonitor() : mo;
        monitor.beginTask( codeProportionsComputerJob_computing, 1024 );
        return performRun( monitor );
    }

    private IStatus performRun( IProgressMonitor monitor ) {
        IStatus result = Status.OK_STATUS;
        try {
            performComputation( monitor );
            getModel().updateLastComputerRun();
        } catch( CoreException cex ) {
            getModel().updateLastComputerRun( false );
            result = cex.getStatus();
        } finally {
            monitor.done();
        }
        return result;
    }

    private void performComputation( IProgressMonitor monitor ) throws CoreException {
        computeCW( new SubProgressMonitor( monitor, 128 ) );
        computeCheckstyleBasedMetrics( new SubProgressMonitor( monitor, 512 ) );
    }

    private void computeCheckstyleBasedMetrics( IProgressMonitor monitor ) throws CoreException {
        new CheckstyleDriver( target ).run( monitor );

        NewWorkspaceResults results = NewWorkspaceResults.getInstance();

        for( IsisMetrics metric : Arrays.asList( CC, KG, ML ) ) {
            getModel().add( results.getCodeProportion( metric ) );
        }
    }

    private void computeCW( IProgressMonitor monitor ) {
        IYellowCountResult yellowCountResult = YellowCount.getInstance().count();
        getModel().add( new CodeProportion( yellowCountResult ) );
    }

    private UsusModel getModel() {
        return (UsusModel)UsusModel.getInstance();
    }

    private static final class MutexSchedulingRule implements ISchedulingRule {
        public boolean isConflicting( ISchedulingRule rule ) {
            return rule == this;
        }

        public boolean contains( ISchedulingRule rule ) {
            return rule == this;
        }
    }
}
