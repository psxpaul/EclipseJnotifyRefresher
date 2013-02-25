package com.pr.inotify;

import java.io.File;
import java.io.FileWriter;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.refresh.IRefreshMonitor;
import org.eclipse.core.resources.refresh.IRefreshResult;
import org.eclipse.core.resources.refresh.RefreshProvider;

public class InotifyRefreshProvider extends RefreshProvider {
	private InotifyRefreshMonitor refreshMonitor;
	
	public InotifyRefreshProvider() {
		try {
			FileWriter fw = new FileWriter(new File("/tmp/notificationLog"));
			fw.write("starting inotify refresh provider");
			fw.flush();
			fw.close();
		} catch(Exception e) {
			
		}
	}
	
	@Override
	public IRefreshMonitor installMonitor(IResource resource, IRefreshResult result) {
		System.out.println("INSTALLING MONITOR!");
		
		if(refreshMonitor == null) {
			refreshMonitor = new InotifyRefreshMonitor();
		}
		
		refreshMonitor.monitor(resource, result);
		
		return refreshMonitor;
	}
}
