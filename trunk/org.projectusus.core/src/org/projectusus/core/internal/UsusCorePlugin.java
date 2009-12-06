// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal;

import static org.eclipse.core.runtime.IStatus.ERROR;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.projectusus.core.internal.coverage.LaunchObserver;
import org.projectusus.core.internal.proportions.IUsusModel;
import org.projectusus.core.internal.proportions.IUsusModelWriteAccess;
import org.projectusus.core.internal.proportions.UsusModel;
import org.projectusus.core.internal.proportions.modelcomputation.AutoComputeSetting;

public class UsusCorePlugin extends Plugin {

    // using hard-coded string instead of bundle symbolic name, since we
    // cannot rely on the bundle still being there during shutdown when
    // some jobs still run
    public static final String PLUGIN_ID = "org.projectusus.core"; //$NON-NLS-1$
    private static UsusCorePlugin plugin;
    private final LaunchObserver launchObserver = new LaunchObserver();
    private UsusModel ususModel;
    private AutoComputeSetting autoComputer;

    public static UsusCorePlugin getDefault() {
        return plugin;
    }

    public static synchronized IUsusModel getUsusModel() {
        return getDefault().ususModel;
    }

    public synchronized IUsusModelWriteAccess getUsusModelWriteAccess() {
        return ususModel;
    }

    public void setAutoCompute( boolean autoCompute ) {
        autoComputer.setAutoCompute( autoCompute );
    }

    public static void log( Exception ex ) {
        String msg = ex.getMessage() == null ? "[No details.]" : ex.getMessage(); //$NON-NLS-1$
        log( msg, ex );
    }

    public static void log( String msg, Exception ex ) {
        if( getDefault() != null ) {
            IStatus status = new Status( ERROR, PLUGIN_ID, 0, msg, ex );
            getDefault().getLog().log( status );
        }
    }

    public IEclipsePreferences getPreferences() {
        return new InstanceScope().getNode( PLUGIN_ID );
    }

    @Override
    public void start( final BundleContext context ) throws Exception {
        super.start( context );
        plugin = this;
        ususModel = new UsusModel();
        context.addBundleListener( new BundleListener() {
            public void bundleChanged( BundleEvent event ) {
                if( event.getType() == BundleEvent.STARTED ) {
                    context.removeBundleListener( this );
                    performInits();
                }
            }
        } );
    }

    @Override
    public void stop( BundleContext context ) throws Exception {
        launchObserver.dispose();
        autoComputer.dispose();
        plugin = null;
        super.stop( context );
    }

    private synchronized void performInits() {
        autoComputer = new AutoComputeSetting();
        launchObserver.connect();
    }
}
