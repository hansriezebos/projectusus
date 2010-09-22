package org.projectusus.statistics;

import java.util.ArrayList;
import java.util.List;

import org.projectusus.core.basis.CodeProportion;
import org.projectusus.core.basis.CodeStatistic;
import org.projectusus.core.basis.Hotspot;
import org.projectusus.core.basis.PackageHotspot;
import org.projectusus.core.filerelations.model.Cycle;
import org.projectusus.core.filerelations.model.PackageCycles;
import org.projectusus.core.filerelations.model.PackageRelations;
import org.projectusus.core.filerelations.model.Packagename;
import org.projectusus.core.statistics.DefaultCockpitExtension;
import org.projectusus.core.statistics.visitors.PackageCountVisitor;

public class PackageCycleStatistic extends DefaultCockpitExtension {

    public PackageCycleStatistic() {
        super( "", 0 ); //$NON-NLS-1$
    }

    @Override
    public CodeProportion getCodeProportion() {
        CodeStatistic basisStatistic = getBasisStatistic();
        PackageCycles packageCycles = new PackageRelations().getPackageCycles();
        int violations = packageCycles.numberOfPackagesInAnyCycles();
        double level = calculateAverage( violations, basisStatistic.getValue() );
        return new CodeProportion( getLabel(), getDescription(), getTooltip(), violations, basisStatistic, level, createHotspots( packageCycles ) );
    }

    private List<Hotspot> createHotspots( PackageCycles packageCycles ) {
        List<Hotspot> hotspots = new ArrayList<Hotspot>();
        for( Cycle<Packagename> cycle : packageCycles.getPackageCycles() ) {
            int cyclesize = cycle.numberOfElements();
            for( Packagename pack : cycle.getElementsInCycle() ) {
                hotspots.add( new PackageHotspot( pack, cyclesize, cycle ) );
            }
        }
        return hotspots;
    }

    @Override
    public CodeStatistic getBasisStatistic() {
        return new PackageCountVisitor().visitAndReturn().getCodeStatistic();
    }

    @Override
    public int getBasis() {
        return getBasisStatistic().getValue();
    }

    @Override
    public int getViolations() {
        return new PackageRelations().getPackageCycles().numberOfPackagesInAnyCycles();
    }

    @Override
    public String getLabel() {
        return "Packages with cyclic dependencies"; //$NON-NLS-1$
    }

    @Override
    protected String getDescription() {
        return getLabel() + ": Rating function: f(value) = value"; //$NON-NLS-1$
    }

}
