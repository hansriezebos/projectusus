// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.ui.internal.proportions;

import static org.projectusus.ui.internal.util.ISharedUsusImages.OBJ_INFO;
import static org.projectusus.ui.internal.util.UsusUIImages.getSharedImages;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.projectusus.core.basis.CodeProportion;
import org.projectusus.ui.internal.proportions.cockpit.CockpitCategory;

/**
 * basic capacities to display Usus Model elements; This class is intended to be subclassed in order to make a specialized tree or table label provider.
 * 
 * @author leif
 */
public abstract class UsusModelLabelProvider extends LabelProvider implements IColorProvider {

    protected String getNodeTextFor( Object element ) {
        String result = super.getText( element );
        if( element instanceof CockpitCategory ) {
            result = ((CockpitCategory)element).getLabel();
            // } else if( element instanceof YellowCountResult ) {
            // result = "Static analysis warnings";
        }
        return result;
    }

    protected Image getColumnImageFor( Object element ) {
        Image result = null;
        if( element instanceof CockpitCategory ) {
            result = ((CockpitCategory)element).getImage();
        } else if( !(element instanceof CodeProportion) ) {
            result = getSharedImages().getImage( OBJ_INFO );
            // build in later again
            // } else if( element instanceof YellowCountResult ) {
            // result = getSharedImages().getImage( OBJ_WARNINGS );
        }
        return result;
    }

    // IColorProvider
    // //////////////

    public Color getBackground( Object element ) {
        return null; // no special treatment
    }

    public Color getForeground( Object element ) {
        return null; // no special treatment
    }
}
