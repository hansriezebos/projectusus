package org.projectusus.core.filerelations;

import java.util.Collection;

import org.jgrapht.alg.CycleDetector;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public class PackageCycleCalculator {

    private final FileRelations relations;

    public PackageCycleCalculator( FileRelations relations ) {
        this.relations = relations;
    }

    public int countPackagesInCycles() {
        Collection<FileRelation> allDirectRelations = relations.getAllDirectRelations();
        SetMultimap<Packagename, Relation<Packagename>> outgoingRelations = HashMultimap.create();
        SetMultimap<Packagename, Relation<Packagename>> incomingRelations = HashMultimap.create();
        for( FileRelation fileRelation : allDirectRelations ) {
            if( fileRelation.isCrossPackage( ) ) {
                Packagename source = fileRelation.getSourcePackage();
                Packagename target = fileRelation.getTargetPackage();
                Relation<Packagename> packageRelation = Relation.of( source, target );
                outgoingRelations.put( source, packageRelation );
                incomingRelations.put( target, packageRelation );
            }
        }
        RelationGraph<Packagename> graph = new RelationGraph<Packagename>( outgoingRelations, incomingRelations );
        return new CycleDetector<Packagename, Relation<Packagename>>( graph ).findCycles().size();
    }
}
