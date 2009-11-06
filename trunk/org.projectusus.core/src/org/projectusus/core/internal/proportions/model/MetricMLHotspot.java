package org.projectusus.core.internal.proportions.model;

import org.eclipse.core.resources.IFile;

public class MetricMLHotspot implements IMetricMLHotspot {

    private final String className;
    private final String methodName;
    private final int methodLength;
    private IFile file;
    private final int sourcePosition;

    public MetricMLHotspot( String className, String methodName, int methodLength, int sourcePosition ) {
        super();
        this.className = className;
        this.methodName = methodName;
        this.methodLength = methodLength;
        this.sourcePosition = sourcePosition;
    }

    public void setFile( IFile file ) {
        this.file = file;
    }

    public int getHotness() {
        return methodLength;
    }

    public String getClassName() {
        return className;
    }

    public int getMethodLength() {
        return methodLength;
    }

    public IFile getFile() {
        return file;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getSourcePosition() {
        return sourcePosition;
    }

    @Override
    public String toString() {
        return getClassName() + "." + getMethodName() + " (ML = " + getMethodLength() + ")"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }

}
