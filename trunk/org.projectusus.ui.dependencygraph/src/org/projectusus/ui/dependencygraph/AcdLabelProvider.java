package org.projectusus.ui.dependencygraph;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.projectusus.core.internal.proportions.rawdata.ClassRawData;

public class AcdLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof ClassRawData) {
			ClassRawData acdNode = (ClassRawData) element;
			return acdNode.getClassName();
		}
		if (element instanceof EntityConnectionData) {
			return "";
		}
		throw new RuntimeException("Type not supported");
	}
}
