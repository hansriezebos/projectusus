// Copyright (c) 2009 by the projectusus.org contributors
// This software is released under the terms and conditions
// of the Eclipse Public License (EPL) 1.0.
// See http://www.eclipse.org/legal/epl-v10.html for details.
package org.projectusus.ui.internal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.projectusus.core.internal.project.FindUsusProjectsPDETest;
import org.projectusus.core.internal.project.IsUsusProjectPDETest;
import org.projectusus.core.internal.proportions.CodeProportionsPDETest;
import org.projectusus.core.internal.proportions.FileChangeNotificationsPDETest;
import org.projectusus.core.internal.proportions.ProjectChangeNotificationsPDETest;
import org.projectusus.core.internal.proportions.UsusProjectNotificationsPDETest;
import org.projectusus.ui.internal.hotspots.ExtractHotspotsPagePDETest;

@RunWith(Suite.class)
@SuiteClasses({ExtractHotspotsPagePDETest.class, FindUsusProjectsPDETest.class, IsUsusProjectPDETest.class, CodeProportionsPDETest.class, FileChangeNotificationsPDETest.class, ProjectChangeNotificationsPDETest.class, UsusProjectNotificationsPDETest.class})
public class AllPDETests {

}
