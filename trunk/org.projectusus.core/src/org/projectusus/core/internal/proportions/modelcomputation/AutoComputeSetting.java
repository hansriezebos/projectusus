// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions.modelcomputation;

import static org.eclipse.core.resources.ResourcesPlugin.getWorkspace;
import static org.projectusus.core.internal.util.UsusPreferenceKeys.AUTO_COMPUTE;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;
import org.projectusus.core.internal.UsusCorePlugin;

public class AutoComputeSetting {
    private final IResourceChangeListener resourcelistener = new RunComputationOnResourceChange();

    public AutoComputeSetting() {
        boolean autoCompute = getPrefs().getBoolean( AUTO_COMPUTE, true );
        applyAutoCompute( autoCompute );
    }

    public void setAutoCompute( boolean autoCompute ) {
        try {
            writePref( autoCompute );
        } catch( BackingStoreException bastox ) {
            UsusCorePlugin.log( bastox );
        }
        applyAutoCompute( autoCompute );
    }

    public void dispose() {
        getWorkspace().removeResourceChangeListener( resourcelistener );
    }

    private void writePref( boolean autoCompute ) throws BackingStoreException {
        IEclipsePreferences prefs = getPrefs();
        prefs.putBoolean( AUTO_COMPUTE, autoCompute );
        prefs.flush();
    }

    private void applyAutoCompute( boolean autoCompute ) {
        if( autoCompute ) {
            getWorkspace().addResourceChangeListener( resourcelistener );
            new ForcedRecompute().schedule();
        } else {
            getWorkspace().removeResourceChangeListener( resourcelistener );
        }
    }

    private IEclipsePreferences getPrefs() {
        return UsusCorePlugin.getDefault().getPreferences();
    }
}
