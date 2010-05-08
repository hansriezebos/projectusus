// Copyright (c) 2009-2010 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.ui.internal.history;

import static org.projectusus.core.basis.CodeProportionKind.ACD;
import static org.projectusus.core.basis.CodeProportionKind.CC;
import static org.projectusus.core.basis.CodeProportionKind.CW;
import static org.projectusus.core.basis.CodeProportionKind.KG;
import static org.projectusus.core.basis.CodeProportionKind.ML;
import static org.projectusus.core.basis.CodeProportionKind.PC;
import static org.projectusus.core.basis.CodeProportionKind.TA;
import static org.projectusus.core.internal.UsusCorePlugin.getUsusModel;
import static org.projectusus.ui.internal.util.ISharedUsusColors.ISIS_METRIC_ACD;
import static org.projectusus.ui.internal.util.ISharedUsusColors.ISIS_METRIC_CC;
import static org.projectusus.ui.internal.util.ISharedUsusColors.ISIS_METRIC_CW;
import static org.projectusus.ui.internal.util.ISharedUsusColors.ISIS_METRIC_KG;
import static org.projectusus.ui.internal.util.ISharedUsusColors.ISIS_METRIC_ML;
import static org.projectusus.ui.internal.util.ISharedUsusColors.ISIS_METRIC_PC;
import static org.projectusus.ui.internal.util.ISharedUsusColors.ISIS_METRIC_TA;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.projectusus.core.IUsusModelListener;
import org.projectusus.core.basis.CodeProportionKind;
import org.projectusus.ui.internal.util.UsusColors;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;

public class HistoryView extends ViewPart {

    private final Map<CodeProportionKind, Color> colors = initColors();
    private Chart chart;

    @Override
    public void createPartControl( Composite parent ) {
        chart = new CheckpointsHistoryChart( parent );
        refresh();
        getUsusModel().addUsusModelListener( new IUsusModelListener() {
            public void ususModelChanged() {
                Display.getDefault().asyncExec( new Runnable() {
                    public void run() {
                        if( chart != null && !chart.isDisposed() ) {
                            refresh();
                        }
                    }
                } );
            }
        } );
    }

    @Override
    public void setFocus() {
        if( chart != null && !chart.isDisposed() ) {
            chart.setFocus();
        }
    }

    private void refresh() {
        Checkpoints2GraphicsConverter converter = getConverter();
        for( CodeProportionKind metric : CodeProportionKind.values() ) {
            updateSeries( metric, converter.get( metric ) );
        }
        chart.redraw();
    }

    private Checkpoints2GraphicsConverter getConverter() {
        return new Checkpoints2GraphicsConverter( getUsusModel().getHistory().getCheckpoints() );
    }

    private void updateSeries( CodeProportionKind metric, double[] newValue ) {
        ISeriesSet seriesSet = chart.getSeriesSet();
        cleanOldValues( seriesSet, metric );
        createSeries( seriesSet, metric, newValue );
    }

    private void createSeries( ISeriesSet seriesSet, CodeProportionKind metric, double[] values ) {
        String seriesId = metric.toString();
        ILineSeries series = (ILineSeries)seriesSet.createSeries( SeriesType.LINE, seriesId );
        series.setYSeries( values );
        series.setSymbolType( PlotSymbolType.DIAMOND );
        series.setSymbolSize( 2 );
        series.setLineWidth( 2 );
        series.setLineColor( colors.get( metric ) );
    }

    private void cleanOldValues( ISeriesSet seriesSet, CodeProportionKind metric ) {
        if( seriesSet.getSeries( metric.toString() ) != null ) {
            seriesSet.deleteSeries( metric.toString() );
        }
    }

    private Map<CodeProportionKind, Color> initColors() {
        Map<CodeProportionKind, Color> result = new HashMap<CodeProportionKind, Color>();
        result.put( TA, getColor( ISIS_METRIC_TA ) );
        result.put( CC, getColor( ISIS_METRIC_CC ) );
        result.put( PC, getColor( ISIS_METRIC_PC ) );
        result.put( ACD, getColor( ISIS_METRIC_ACD ) );
        result.put( KG, getColor( ISIS_METRIC_KG ) );
        result.put( ML, getColor( ISIS_METRIC_ML ) );
        result.put( CW, getColor( ISIS_METRIC_CW ) );
        return result;
    }

    private Color getColor( String key ) {
        return UsusColors.getSharedColors().getColor( key );
    }
}
