package org.projectusus.core.internal.proportions.rawdata;

import java.util.HashSet;
import java.util.Set;

import org.projectusus.core.basis.GraphNode;
import org.projectusus.core.filerelations.internal.metrics.BottleneckCalculator;
import org.projectusus.core.filerelations.model.ClassDescriptor;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class ClassRepresenter extends AbstractClassRepresenter {

    private Set<GraphNode> childrenCache = null;

    public static Set<GraphNode> transformToRepresenterSet( Set<ClassDescriptor> classes ) {
        Function<ClassDescriptor, ClassRepresenter> function = new Function<ClassDescriptor, ClassRepresenter>() {
            public ClassRepresenter apply( ClassDescriptor descriptor ) {
                return new ClassRepresenter( descriptor );
            }
        };
        return new HashSet<GraphNode>( Collections2.transform( classes, function ) );
    }

    public ClassRepresenter( ClassDescriptor clazz ) {
        super( clazz );
    }

    public Set<GraphNode> getChildren() {
        if( childrenCache == null ) {
            childrenCache = transformToRepresenterSet( clazz.getChildren() );
        }
        return childrenCache;
    }

    public String getNodeName() {
        return clazz.getClassname().toString();
    }

    @Override
    public boolean equals( Object obj ) {
        return obj instanceof ClassRepresenter && super.equals( obj );
    }

    @Override
    public String getEdgeEndLabel() {
        return String.valueOf( getRelationCount() );
    }

    private int getRelationCount() {
        return clazz.getCCD();
    }

    public boolean isVisibleFor( int limit ) {
        return getFilterValue() >= limit;
    }

    public int getFilterValue() {
        return BottleneckCalculator.getBottleneckCount( this.clazz );
    }

}
