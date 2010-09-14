// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.adapter;

import static org.projectusus.adapter.TracingOption.SQI;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.projectusus.core.IUsusModelForAdapter;
import org.projectusus.core.UsusModelProvider;
import org.projectusus.core.basis.YellowCountCache;
import org.projectusus.core.metrics.MetricsCollector;
import org.projectusus.core.metrics.RegisteredMetricsExtensions;
import org.projectusus.core.proportions.rawdata.jdtdriver.JavaFileDriver;
import org.projectusus.core.util.FileSupport;

public class JDTDriver {

    private final IUsusModelForAdapter model;
    private final ICodeProportionComputationTarget target;

    public JDTDriver( ICodeProportionComputationTarget target ) {
        model = UsusModelProvider.ususModelForAdapter();
        this.target = target;
    }

    public void run( IProgressMonitor monitor ) throws CoreException {
        for( IProject removedProject : target.getRemovedProjects() ) {
            model.dropRawData( removedProject );
            YellowCountCache.yellowCountCache().clear( removedProject );
        }
        monitor.beginTask( null, countTicks( target.getProjects() ) );
        Set<MetricsCollector> metricsExtensions = RegisteredMetricsExtensions.allExtensions(); // TODO improve!
        for( IProject project : target.getProjects() ) {
            monitor.subTask( project.getName() );
            for( IFile removedFile : target.getRemovedFiles( project ) ) {
                model.dropRawData( removedFile );
            }
            YellowCountCache.yellowCountCache().add( project );
            computeChangedFiles( metricsExtensions, project, monitor );
        }
        monitor.done();
    }

    private int countTicks( Collection<IProject> projects ) throws CoreException {
        int result = 0;
        for( IProject project : projects ) {
            result += target.getFiles( project ).size();
        }
        return result;
    }

    private void computeChangedFiles( Set<MetricsCollector> metricsExtensions, IProject project, IProgressMonitor monitor ) throws CoreException {
        Collection<IFile> files = target.getFiles( project );
        if( !files.isEmpty() ) {
            StatusCollector statusCollector = new StatusCollector();
            runDriver( metricsExtensions, project, files, statusCollector, monitor );
            statusCollector.finish();
        }
    }

    private void runDriver( Set<MetricsCollector> metricsExtensions, IProject project, Collection<IFile> files, StatusCollector statusCollector, IProgressMonitor monitor ) {
        computationStarted( project );

        for( IFile file : files ) {
            fileStarted( file );
            runDriverOnFile( metricsExtensions, file, statusCollector, monitor );
        }
    }

    private void runDriverOnFile( Set<MetricsCollector> metricsExtensions, IFile file, StatusCollector statusCollector, IProgressMonitor monitor ) {
        try {
            if( FileSupport.isJavaFile( file ) ) {
                new JavaFileDriver( file ).compute( metricsExtensions );
            }
        } catch( Exception ex ) {
            statusCollector.add( ex );
        } finally {
            monitor.worked( 1 );
        }
    }

    private void computationStarted( IProject project ) {
        SQI.trace( "Computation started: " + project.toString() ); //$NON-NLS-1$
    }

    private void fileStarted( IFile file ) {
        SQI.trace( "File started: " + file.getFullPath() ); //$NON-NLS-1$
        model.dropRawData( file );
    }
}
