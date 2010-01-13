package org.projectusus.ui.dependencygraph;

import java.util.HashSet;
import java.util.Set;

import org.projectusus.core.internal.proportions.rawdata.ClassRawData;
import org.projectusus.core.internal.proportions.rawdata.WorkspaceRawData;

public class GraphModel {

	private int minimumEdges;

	private static GraphModel instance;
	private final Set<IGraphModelListener> listeners;

	private GraphModel() {
		listeners = new HashSet<IGraphModelListener>();
		minimumEdges = 2;
	}

	public static GraphModel getInstance() {
		if (instance == null) {
			instance = new GraphModel();
		}
		return instance;
	}

	public void addAcdModelListener(IGraphModelListener listener) {
		listeners.add(listener);
	}

	public void removeAcdModelListener(IGraphModelListener listener) {
		listeners.remove(listener);
	}

	public Set<ClassRawData> getRawData() {
		return WorkspaceRawData.getInstance().getAllClassRawData();
	}

	public void update() {
		notifyListeners();
	}

	private void notifyListeners() {
		for (IGraphModelListener listener : listeners) {
			listener.ususModelChanged();
		}
	}

	public void increaseLimit() {
		minimumEdges++;
		update();
	}

	public void decreaseLimit() {
		if (minimumEdges > 0) {
			minimumEdges--;
		}
		update();
	}

	public int getMinimumEdges() {
		return minimumEdges;
	}

}
