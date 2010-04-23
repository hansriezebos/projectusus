package org.projectusus.ui.dependencygraph.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.projectusus.core.internal.proportions.rawdata.GraphNode;

public abstract class DependencyGraphModel {

    private Set<? extends GraphNode> nodes;
    private boolean changed = false;

    public boolean isChanged() {
        return nodes == null || changed;
    }

    public void resetChanged() {
        this.changed = false;
    }

    public Set<? extends GraphNode> getGraphNodes() {
        if( nodes == null ) {
            nodes = getRefreshedNodes();
        }
        return nodes;
    }

    protected abstract Set<? extends GraphNode> getRefreshedNodes();

    public void invalidate() {
        Set<? extends GraphNode> freshNodes = getRefreshedNodes();
        if( nodes != null ) {
            if( !freshNodes.equals( nodes ) ) {
                nodes = freshNodes;
                changed = true;
            }
        }
    }

    public int getMaxFilterValue() {
        if( getGraphNodes().isEmpty() ) {
            return -1;
        }
        return Collections.max( getGraphNodes(), new Comparator<GraphNode>() {

            public int compare( GraphNode node1, GraphNode node2 ) {
                return node1.getFilterValue() - node2.getFilterValue();
            }

        } ).getFilterValue();
    }
}
