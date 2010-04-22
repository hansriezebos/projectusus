package org.projectusus.core.filerelations;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.projectusus.core.filerelations.internal.metrics.ACDCalculator;
import org.projectusus.core.filerelations.internal.metrics.BottleneckCalculator;
import org.projectusus.core.filerelations.internal.model.FileRelations;
import org.projectusus.core.filerelations.model.ClassDescriptor;
import org.projectusus.core.filerelations.model.FileRelation;
import org.projectusus.core.filerelations.model.Packagename;
import org.projectusus.core.filerelations.model.PackagenameFactory;

public class FileRelationMetrics {

    private final FileRelations relations;
    private final ClassDescriptors classes;

    public FileRelationMetrics( FileRelations relations ) {
        this.relations = relations;
        classes = new ClassDescriptors();
    }

    public FileRelationMetrics() {
        this( new FileRelations() );
    }

    public void addFileRelation( ClassDescriptor source, ClassDescriptor target ) {
        relations.add( new FileRelation( source, target ) );
    }

    public void remove( IFile file ) {
        classes.removeAllClassesIn( file );
        relations.markAndRemoveAllRelationsStartingAt( file );
        relations.registerAllRelationsEndingAt( file );
    }

    public List<FileRelation> findRelationsThatNeedRepair() {
        return relations.extractRelationsRegisteredForRepair();
    }

    public int getCCD( ClassDescriptor descriptor ) {
        return new ACDCalculator( relations ).getCCD( descriptor );
    }

    public int getBottleneckCount( ClassDescriptor descriptor ) {
        return new BottleneckCalculator( relations ).getBottleneckCount( descriptor );
    }

    public Set<ClassDescriptor> getChildren( ClassDescriptor descriptor ) {
        return relations.getDirectRelationsFrom( descriptor );
    }

    public Set<ClassDescriptor> getAllClassDescriptors() {
        return classes.getAll();
    }

    public Set<Packagename> getAllPackages() {
        return PackagenameFactory.getAll();
    }

    public void addClass( ITypeBinding binding ) throws JavaModelException {
        classes.add( new ClassDescriptor( binding ) );
    }

    public void remove( FileRelation relation ) {
        relations.remove( relation );
    }

    public int getTransitiveParentCount( ClassDescriptor clazz ) {
        return relations.getTransitiveRelationsTo( clazz.getFile(), clazz.getClassname() ).size();
    }

    public Set<Packagename> getChildren( Packagename packagename ) {
        return relations.getDirectPackageRelationsFrom( packagename );
    }
}
