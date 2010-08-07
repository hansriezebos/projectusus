package org.projectusus.core.internal.proportions.rawdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.junit.Test;
import org.projectusus.core.basis.GraphNode;

public class MetricsAccessorGetAllClassRepresentersPDETest extends PDETestForMetricsComputation {

    @Test
    public void emptyProject() throws Exception {
        Set<GraphNode> representers = UsusModel.ususModel().getMetricsAccessor().getAllClassRepresenters();
        assertEquals( 0, representers.size() );
    }

    @Test
    public void oneClassInOneFile() throws Exception {
        createFileAndBuild( "_1" );
        Set<GraphNode> representers = UsusModel.ususModel().getMetricsAccessor().getAllClassRepresenters();
        assertEquals( 1, representers.size() );
        GraphNode node = representers.iterator().next();
        assertEquals( 0, node.getChildren().size() );
    }

    @Test
    public void twoClassesInOneFileKnowEachOther() throws Exception {
        createFileAndBuild( "_twoKnowEachOther" );
        Set<GraphNode> representers = UsusModel.ususModel().getMetricsAccessor().getAllClassRepresenters();
        assertEquals( 2, representers.size() );
        GraphNode node = representers.iterator().next();
        checkName( node, "Acd2", "Acd2Helper" );
        assertEquals( 1, node.getChildren().size() );
        node = representers.iterator().next();
        checkName( node, "Acd2", "Acd2Helper" );
        assertEquals( 1, node.getChildren().size() );
    }

    private void checkName( GraphNode node, String name1, String name2 ) {
        if( !node.getNodeName().equals( name1 ) && !node.getNodeName().equals( name2 ) ) {
            fail( "Falscher Name" );
        }
    }

    @Test
    public void twoClassesInOneFileKnowEachOtherOneIsRemoved() throws Exception {
        IFile file = createFileAndBuild( "_twoKnowEachOther" );
        updateFileContent( file, loadContent( "MetricsAccessor_oneKnowsTheOther.test" ) );
        buildFullyAndWait();
        Set<GraphNode> representers = UsusModel.ususModel().getMetricsAccessor().getAllClassRepresenters();
        assertEquals( 2, representers.size() );
        GraphNode node = representers.iterator().next();
        checkName( node, "Acd2", "Acd2Helper" );
        checkChildren( node, "Acd2" );
        node = representers.iterator().next();
        checkName( node, "Acd2", "Acd2Helper" );
        checkChildren( node, "Acd2" );
    }

    @Test
    public void twoClassesInTwoFilesKnowEachOtherOneIsRemoved() throws Exception {
        createFileAndBuild( "_file1Knows2" );
        IFile file = createFileAndBuild( "_file2Knows1" );

        Set<GraphNode> representers = UsusModel.ususModel().getMetricsAccessor().getAllClassRepresenters();
        assertEquals( 2, representers.size() );
        GraphNode node = representers.iterator().next();
        assertEquals( 1, node.getChildren().size() );
        node = representers.iterator().next();
        assertEquals( 1, node.getChildren().size() );

        updateFileContent( file, loadContent( "MetricsAccessor_file2Knows1Not.test" ) );
        buildFullyAndWait();

        representers = UsusModel.ususModel().getMetricsAccessor().getAllClassRepresenters();
        assertEquals( 2, representers.size() );
        node = representers.iterator().next();
        checkName( node, "MetricsAccessor_file1Knows2", "MetricsAccessor_file2Knows1" );
        checkChildren( node, "MetricsAccessor_file1Knows2" );
        node = representers.iterator().next();
        checkName( node, "MetricsAccessor_file1Knows2", "MetricsAccessor_file2Knows1" );
        checkChildren( node, "MetricsAccessor_file1Knows2" );
    }

    private void checkChildren( GraphNode node, String name ) {
        if( node.getNodeName().equals( name ) ) {
            assertEquals( 1, node.getChildren().size() );
        } else {
            assertEquals( 0, node.getChildren().size() );
        }
    }

    protected IFile createFile( String filenumber ) throws Exception {
        return super.createFile( "MetricsAccessor" + filenumber );
    }

}
