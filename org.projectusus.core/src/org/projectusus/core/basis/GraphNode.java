package org.projectusus.core.basis;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.projectusus.core.filerelations.model.Packagename;

public interface GraphNode {

    Set<? extends GraphNode> getChildren();

    String getNodeName();

    String getEdgeStartLabel();

    String getEdgeMiddleLabel();

    String getEdgeEndLabel();

    int getFilterValue();

    boolean isVisibleFor( int limit );

    boolean isPackage();

    IFile getFile();

    boolean isPackageOneOf( Collection<Packagename> packages );

    IJavaElement getNodeJavaElement();

    boolean isAtEitherEndOf( Packagename source, Packagename dest );
}
