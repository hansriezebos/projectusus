// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.core.internal.proportions.rawdata;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.projectusus.core.filerelations.model.ClassDescriptor;
import org.projectusus.core.filerelations.model.Classname;
import org.projectusus.core.filerelations.model.Packagename;
import org.projectusus.core.internal.UsusCorePlugin;
import org.projectusus.core.internal.proportions.model.IHotspot;
import org.projectusus.core.internal.proportions.model.MetricACDHotspot;
import org.projectusus.core.internal.proportions.model.MetricKGHotspot;

class ClassRawData extends RawData<Integer, MethodRawData> {

    private final int startPosition;
    private final int lineNumber;
    private final String className;
    private ClassDescriptor descriptor;

    public ClassRawData( ITypeBinding binding, String name, int startPosition, int line ) {
        this.className = name;
        this.startPosition = startPosition;
        this.lineNumber = line;
        if( binding != null ) {
            try {
                this.descriptor = ClassDescriptor.of( binding );
            } catch( JavaModelException e ) {
                // impossible to create ClassDescriptor
            }
        }
    }

    public ClassRawData( IFile file, String packageName, String name, int startPosition, int line ) {
        this.className = name;
        this.startPosition = startPosition;
        this.lineNumber = line;
        this.descriptor = ClassDescriptor.of( file, new Classname( name ), Packagename.of( packageName ) );
    }

    // for debugging:
    @Override
    public String toString() {
        return "Class " + className + " in line " + lineNumber + " with " + getNumberOfMethods() + " methods."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    void setCCValue( MethodDeclaration node, int value ) {
        getRawData( node ).setCCValue( value );
    }

    void setCCValue( Initializer node, int value ) {
        getRawData( node ).setCCValue( value );
    }

    void setMLValue( MethodDeclaration node, int value ) {
        getRawData( node ).setMLValue( value );
    }

    void setMLValue( Initializer node, int value ) {
        getRawData( node ).setMLValue( value );
    }

    private MethodRawData getRawData( MethodDeclaration node ) {
        return getRawData( node.getStartPosition(), JDTSupport.calcLineNumber( node ), node.getName().toString() );
    }

    private MethodRawData getRawData( Initializer node ) {
        return getRawData( node.getStartPosition(), JDTSupport.calcLineNumber( node ), "initializer" ); //$NON-NLS-1$
    }

    private MethodRawData getRawData( int start, int lineNumber, String methodName ) {
        Integer startObject = new Integer( start );
        MethodRawData rawData = super.getRawData( startObject );
        if( rawData == null ) {
            rawData = new MethodRawData( start, lineNumber, className, methodName );
            super.addRawData( startObject, rawData );
        }
        return rawData;
    }

    public MethodRawData getMethodRawData( IMethod method ) {
        if( method == null ) {
            return null;
        }
        ICompilationUnit compilationUnit = JDTSupport.getCompilationUnit( method );
        if( compilationUnit == null ) {
            return null;
        }

        try {
            for( Integer start : getAllKeys() ) {
                IJavaElement foundElement = compilationUnit.getElementAt( start.intValue() );
                if( method.equals( foundElement ) ) {
                    return getRawData( start.intValue(), 0, "" ); //$NON-NLS-1$
                }
            }
        } catch( JavaModelException e ) {
            return null;
        }
        return null;
    }

    @Override
    public int getNumberOf( CodeProportionUnit unit ) {
        if( unit.isMethodKind() ) {
            return super.getNumberOf( unit );
        }
        return 1;
    }

    @Override
    public int getViolationCount( CodeProportionKind metric ) {
        if( metric.isMethodKind() ) {
            return super.getViolationCount( metric );
        }
        return metric.isViolatedBy( this ) ? 1 : 0;
    }

    @Override
    public void addToHotspots( CodeProportionKind metric, List<IHotspot> hotspots ) {
        if( metric.isMethodKind() ) {
            super.addToHotspots( metric, hotspots );
            return;
        }

        if( metric.isViolatedBy( this ) ) {
            if( metric.equals( CodeProportionKind.KG ) ) {
                hotspots.add( new MetricKGHotspot( className, getNumberOfMethods(), startPosition, lineNumber ) );
            }
            if( metric.equals( CodeProportionKind.ACD ) ) {
                hotspots.add( new MetricACDHotspot( className, getCCDResult(), startPosition, lineNumber ) );
            }
        }
    }

    public int getNumberOfMethods() {
        return getNumberOf( CodeProportionUnit.METHOD );
    }

    public int getCCDResult() {
        return UsusCorePlugin.getUsusModelMetricsWriter().getFileRelationMetrics().getCCD( descriptor );
    }

    public Set<ClassDescriptor> getChildren() {
        return UsusCorePlugin.getUsusModelMetricsWriter().getFileRelationMetrics().getChildren( descriptor );
    }

    public String getClassName() {
        return className;
    }
}
