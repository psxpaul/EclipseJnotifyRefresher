package com.pr.inotify;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.refresh.IRefreshMonitor;
import org.eclipse.core.resources.refresh.IRefreshResult;
import org.eclipse.core.runtime.IPath;

public class InotifyRefreshMonitor implements IRefreshMonitor {
	private HashMap<IResource, Integer> watchMap = new HashMap<IResource, Integer>();
	
	@Override
	public void unmonitor(IResource resource) {
		Integer watchId = watchMap.get(resource);
		
		if (watchId != null) {
			try {
				JNotify.removeWatch(watchId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void monitor(IResource resource, IRefreshResult result) {
		final IPath path = resource.getLocation();
		
		if(result == null || path == null) return;

		try {
			Integer newWatch = JNotify.addWatch(path.toOSString(), JNotify.FILE_ANY, true, new Listener(resource, result));
			watchMap.put(resource, newWatch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class Listener implements JNotifyListener {
		private IResource resource;
		private IRefreshResult result;
//		private FileWriter fw;

		public Listener(IResource resource, IRefreshResult result) throws IOException {
			this.resource = resource;
			this.result = result;
//			this.fw = new FileWriter(File.createTempFile("notifier", ".log", new File("/tmp")));
		}
		
/*		private void log(String l) {
			try {
				fw.write(l + "\n");
				fw.flush();
			} catch (Exception e) {}
		}*/
		
		private void refreshPath(String path) {
			File f = new File(path);
			
			if (f.isDirectory()) {
				IContainer[] containers = resource.getWorkspace().getRoot().findContainersForLocationURI(f.toURI());
				for (IContainer container : containers) {
					refresh(container);
				}
			} else {
				IFile[] files = resource.getWorkspace().getRoot().findFilesForLocationURI(f.toURI());
				for (IFile file : files) {
					refresh(file);
				}
			}
		}
		
		private void refresh(IResource resource) {
			if (result != null && !resource.isSynchronized(IResource.DEPTH_INFINITE)) {
//				log("refreshing " + resource.getFullPath() + " from result");
				result.refresh(resource);
//			} else {
//				log("result was null or not synchronized?");
			}
		}
		
		@Override
		public void fileCreated(int wd, String rootPath, String name) {
			refreshPath(rootPath + File.separatorChar + name);
		}

		@Override
		public void fileDeleted(int wd, String rootPath, String name) {
			refreshPath(rootPath + File.separatorChar  + name);
		}

		@Override
		public void fileModified(int wd, String rootPath, String name) {
			refreshPath(rootPath + File.separatorChar  + name);
		}

		@Override
		public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
			refreshPath(rootPath + File.separatorChar  + newName);
			refreshPath(rootPath + File.separatorChar  + oldName);
		}
	}
}