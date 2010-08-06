// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal;

import static org.eclipse.core.resources.IncrementalProjectBuilder.FULL_BUILD;
import static org.eclipse.core.resources.IncrementalProjectBuilder.INCREMENTAL_BUILD;
import static org.eclipse.core.resources.ResourcesPlugin.FAMILY_AUTO_BUILD;
import static org.eclipse.core.resources.ResourcesPlugin.FAMILY_AUTO_REFRESH;
import static org.eclipse.core.resources.ResourcesPlugin.FAMILY_MANUAL_BUILD;
import static org.eclipse.core.resources.ResourcesPlugin.getWorkspace;
import static org.eclipse.core.runtime.jobs.Job.getJobManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.Before;
import org.projectusus.adapter.UsusAdapterPlugin;
import org.projectusus.core.internal.proportions.rawdata.UsusModel;

public class PDETestUsingWSProject {

    protected static final String PROJECT_NAME_1 = "p1";
    protected static final String PROJECT_NAME_2 = "p2";
    protected IProject project1;
    protected IProject project2;

    @Before
    public void setUp() throws CoreException {
        project1 = new TestProjectCreator( PROJECT_NAME_1 ).getProject();
        project2 = new TestProjectCreator( PROJECT_NAME_2 ).getProject();
        UsusAdapterPlugin.getDefault(); // to load bundle with ResourceChangeListener
    }

    @After
    public void tearDown() throws CoreException {
        project1.delete( true, new NullProgressMonitor() );
        project2.delete( true, new NullProgressMonitor() );
        UsusModel.clear();
    }

    protected void buildFullyAndWait() throws CoreException {
        getWorkspace().build( FULL_BUILD, new NullProgressMonitor() );
        System.out.print( "  Waiting for full build to complete ..." );
        waitForBuild();
    }

    protected void buildIncrementallyAndWait() throws CoreException {
        getWorkspace().build( INCREMENTAL_BUILD, new NullProgressMonitor() );
        System.out.print( "  Waiting for incremental build to complete ..." );
        waitForBuild();
    }

    private void waitForBuild() {
        boolean retry = true;
        while( retry ) {
            try {
                getJobManager().join( FAMILY_AUTO_REFRESH, new NullProgressMonitor() );
                getJobManager().join( FAMILY_AUTO_BUILD, new NullProgressMonitor() );
                getJobManager().join( FAMILY_MANUAL_BUILD, new NullProgressMonitor() );
                retry = false;
            } catch( Exception exc ) {
                // ignore and retry
            }
        }
        System.out.println( " OK." );
    }

    protected IFile createWSFile( String fileName, String content, IProject theProject ) throws CoreException {
        IFile result = theProject.getFile( fileName );
        result.create( createInputStream( content ), true, new NullProgressMonitor() );
        return result;
    }

    protected void deleteWSFile( IFile file ) throws CoreException {
        file.delete( true, new NullProgressMonitor() );
    }

    protected void updateFileContent( IFile file, String newContent ) throws CoreException {
        file.setContents( createInputStream( newContent ), true, false, new NullProgressMonitor() );
    }

    protected IFolder createWSFolder( String name, IProject theProject ) throws CoreException {
        IFolder result = theProject.getFolder( name );
        result.create( true, true, new NullProgressMonitor() );
        return result;
    }

    protected void makeUsusProject( boolean makeUsusProject, IProject theProject ) throws CoreException {
        TestProjectCreator.makeUsusProject( makeUsusProject, theProject );
    }

    private InputStream createInputStream( String content ) {
        return new ByteArrayInputStream( content.getBytes() );
    }
}
